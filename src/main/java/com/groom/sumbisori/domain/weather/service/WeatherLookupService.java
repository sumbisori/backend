package com.groom.sumbisori.domain.weather.service;

import com.groom.sumbisori.domain.content.entity.Spot;
import com.groom.sumbisori.domain.weather.dto.WeatherResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WeatherLookupService {
    private final WeatherApiClient weatherApiClient;

    @Cacheable(value = "weathers", key = "#spot")
    public WeatherResponse lookup(Spot spot) {
        return weatherApiClient.fetch(spot);
    }
}
