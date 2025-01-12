package com.groom.sumbisori.domain.content.service;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTube.Search;
import com.google.api.services.youtube.YouTube.Videos;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.VideoListResponse;
import com.groom.sumbisori.common.constant.CacheType;
import com.groom.sumbisori.domain.content.dto.response.YoutubeResponse;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Profile("prod")
@RequiredArgsConstructor
@Slf4j
public class YoutubeService {
    private static final String SEARCH_PART = "id,snippet";
    private static final String SEARCH_FIELDS = "items(id,snippet(title,thumbnails(medium),publishedAt, channelTitle))";
    private static final String SEARCH_QUERY = "제주도 해녀체험";
    private static final String SEARCH_TYPE = "video";
    private static final boolean EMBEDDABLE = true;
    private static final long MAX_RESULTS = 50L;
    private static final String VIDEOS_PART = "statistics";
    private static final String VIDEOS_FIELDS = "items(id,statistics(viewCount))";

    private final YoutubeUpdateService youtubeUpdateService;
    private final YouTube youtubeClient;
    private final CacheManager cacheManager;

    @Value("${api.key.youtube}")
    private String apiKey;

    @Scheduled(fixedRate = 3600000) // 1시간마다 실행 (밀리초 기준)
    public void saveYoutubeCache() {
        List<SearchResult> youtubeList = getYoutubeList();
        Map<String, BigInteger> videoViews = getVideoViews(youtubeList); //video id와 해당 비디오의 조회수를 가져옴
        if (youtubeList.isEmpty() || videoViews.isEmpty()) {
            log.error("YouTube API에서 데이터를 가져오지 못했습니다.");
            return;
        }
        List<YoutubeResponse> youtubeResponses = youtubeList.stream()
                .map(result -> YoutubeResponse.from(result, videoViews.get(result.getId().getVideoId())))
                .toList();
        cacheManager.getCache(CacheType.YOUTUBES.getCacheName()).put(SimpleKey.EMPTY, youtubeResponses);
        youtubeUpdateService.update(youtubeResponses);
        log.debug("YouTube API에서 데이터를 가져와 캐시에 저장했습니다.");
    }

    private Map<String, BigInteger> getVideoViews(List<SearchResult> youtubeList) {
        List<String> videoIds = youtubeList.stream()
                .map(result -> result.getId().getVideoId())
                .toList();
        if (videoIds.isEmpty()) {
            log.error("YouTube Search API에서 비디오 ID를 가져오지 못했습니다.");
            return Collections.emptyMap();
        }
        try {
            // Videos API 요청 생성
            Videos.List videoRequest = youtubeClient.videos()
                    .list(Collections.singletonList(VIDEOS_PART))
                    .setKey(apiKey)
                    .setId(videoIds)
                    .setFields(VIDEOS_FIELDS);

            VideoListResponse videoResponse = videoRequest.execute();
            return videoResponse.getItems().stream()
                    .collect(Collectors.toMap(
                            video -> video.getId(),
                            video -> video.getStatistics().getViewCount())
                    );
        } catch (IOException e) {
            log.error("YouTube Video 조회수 API 요청 중 오류 발생", e);
        }
        return Collections.emptyMap();
    }

    private List<SearchResult> getYoutubeList() {
        try {
            // YouTube Search API 요청 생성
            Search.List search = youtubeClient.search()
                    .list(Collections.singletonList(SEARCH_PART))
                    .setKey(apiKey)
                    .setType(Collections.singletonList(SEARCH_TYPE))
                    .setQ(SEARCH_QUERY)
                    .setMaxResults(MAX_RESULTS)
                    .setFields(SEARCH_FIELDS)
                    .setVideoEmbeddable(String.valueOf(EMBEDDABLE));

            // 요청 실행 및 결과 반환
            SearchListResponse searchResponse = search.execute();
            return searchResponse.getItems();
        } catch (IOException e) {
            log.error("YouTube API 요청 중 오류 발생", e);
        }
        // 요청 실패 시 빈 리스트 반환
        return Collections.emptyList();
    }
}
