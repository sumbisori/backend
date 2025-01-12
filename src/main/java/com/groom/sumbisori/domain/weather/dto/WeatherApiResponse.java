package com.groom.sumbisori.domain.weather.dto;

import java.util.List;

public record WeatherApiResponse(
        Main main,
        List<Weather> weather
) {
    public record Main(double temp) {}
    public record Weather(String icon) {}
}
