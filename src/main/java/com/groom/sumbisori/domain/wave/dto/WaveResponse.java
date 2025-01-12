package com.groom.sumbisori.domain.wave.dto;

import java.time.LocalDateTime;

public record WaveResponse(double waveHeight, double waterTemperature, LocalDateTime observationTime) {
    public static WaveResponse of(double waveHeight, double waterTemperature, LocalDateTime observationTime) {
        return new WaveResponse(waveHeight, waterTemperature, observationTime);
    }
}
