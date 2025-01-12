package com.groom.sumbisori.domain.wave.service;

import com.groom.sumbisori.common.error.GlobalErrorCode;
import com.groom.sumbisori.common.error.GlobalException;
import com.groom.sumbisori.domain.content.entity.Spot;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Slf4j
@RequiredArgsConstructor
public class WaveApiClient {
    private static final String BASE_URL = "https://apihub.kma.go.kr/api/typ01/url/sea_obs.php";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
    private static final String PARAM_TM = "tm";
    private static final String PARAM_STN = "stn";
    private static final String PARAM_AUTH_KEY = "authKey";

    private final RestClient restClient;

    @Value("${api.key.wave}")
    private String apiKey;

    /**
     * 외부 API를 호출
     */
    public String fetch(Spot spot) {
        String requestUrl = buildRequestUrl(spot);
        log.debug("Calling external Wave API. Spot name: {}", spot.getSpotName());

        return restClient.get()
                .uri(requestUrl)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (request, clientResponse) -> {
                    log.error("External API returned error status. Spot name: {}, URL: {}", spot.getSpotName(),
                            requestUrl);
                    throw new GlobalException(GlobalErrorCode.EXTERNAL_API_ERROR);
                })
                .body(String.class);
    }

    /**
     * API 호출을 위한 URL 생성
     */
    private String buildRequestUrl(Spot spot) {
        return UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .queryParam(PARAM_TM, LocalDateTime.now().format(DATE_TIME_FORMATTER))
                .queryParam(PARAM_STN, spot.getSpotId())
                .queryParam(PARAM_AUTH_KEY, apiKey)
                .build()
                .toUriString();
    }
}
