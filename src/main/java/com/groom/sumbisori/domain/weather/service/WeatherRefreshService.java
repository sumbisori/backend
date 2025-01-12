package com.groom.sumbisori.domain.weather.service;

import static com.groom.sumbisori.common.constant.CacheType.WEATHERS;

import com.groom.sumbisori.domain.content.entity.Spot;
import com.groom.sumbisori.domain.weather.dto.WeatherResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class WeatherRefreshService {
    private final WeatherApiClient weatherApiClient;
    private final CacheManager cacheManager;

    @Async
    public void refresh(Spot spot) {
        WeatherResponse weatherResponse = weatherApiClient.fetch(spot);
        cacheManager.getCache(WEATHERS.getCacheName()).put(spot, weatherResponse);
    }
}
