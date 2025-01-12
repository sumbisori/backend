package com.groom.sumbisori.domain.wave.service;

import com.groom.sumbisori.domain.content.entity.Spot;
import com.groom.sumbisori.domain.wave.dto.WaveResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WaveLookupService {
    private final WaveApiClient waveApiClient;
    private final WaveResponseParser waveResponseParser;

    @Cacheable(value = "waves", key = "#spot")
    public WaveResponse lookup(Spot spot) {
        String response = waveApiClient.fetch(spot);
        WaveResponse waveResponse = waveResponseParser.parse(response, spot);
        return waveResponse;
    }
}
