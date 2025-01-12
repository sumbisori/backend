package com.groom.sumbisori.domain.weather.dto;

import com.groom.sumbisori.domain.weather.entity.WeatherType;

public record WeatherResponse(double temperature, WeatherType weatherType) {
    public static WeatherResponse of(double temp, WeatherType weatherType) {
        return new WeatherResponse(temp, weatherType);
    }
}
