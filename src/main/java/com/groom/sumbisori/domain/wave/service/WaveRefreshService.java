package com.groom.sumbisori.domain.wave.service;

import static com.groom.sumbisori.common.constant.CacheType.WAVES;

import com.groom.sumbisori.domain.content.entity.Spot;
import com.groom.sumbisori.domain.wave.dto.WaveResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class WaveRefreshService {
    private final WaveApiClient waveApiClient;
    private final WaveResponseParser waveResponseParser;
    private final CacheManager cacheManager;

    /**
     * 매 시각 31분, 01분에 캐시 업데이트
     */
    @Async
    public void refresh(Spot spot) {
        String response = waveApiClient.fetch(spot);
        WaveResponse waveResponse = waveResponseParser.parse(response, spot);
        cacheManager.getCache(WAVES.getCacheName()).put(spot, waveResponse);
    }
}
