package com.groom.sumbisori.domain.weather.service;

import com.groom.sumbisori.common.error.GlobalErrorCode;
import com.groom.sumbisori.common.error.GlobalException;
import com.groom.sumbisori.domain.content.entity.Spot;
import com.groom.sumbisori.domain.weather.dto.WeatherApiResponse;
import com.groom.sumbisori.domain.weather.dto.WeatherResponse;
import com.groom.sumbisori.domain.weather.entity.WeatherType;
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
public class WeatherApiClient {
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather";
    private static final String PARAM_LAT = "lat";
    private static final String PARAM_LON = "lon";
    private static final String PARAM_APPID = "appid";
    private static final String PARAM_UNITS = "units";
    private static final String METRIC_UNITS = "metric";

    private final RestClient restClient;

    @Value("${api.key.weather}")
    private String apiKey;

    public WeatherResponse fetch(Spot spot) {
        String url = buildRequestUrl(spot);
        WeatherApiResponse response = callApi(spot, url);
        double temp = response.main().temp();
        WeatherType weatherType = extractWeatherType(response, spot);
        return WeatherResponse.of(temp, weatherType);
    }

    /**
     * API 호출을 위한 URL 생성
     */
    private String buildRequestUrl(Spot spot) {
        return UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .queryParam(PARAM_LAT, spot.getLatitude())
                .queryParam(PARAM_LON, spot.getLongitude())
                .queryParam(PARAM_APPID, apiKey)
                .queryParam(PARAM_UNITS, METRIC_UNITS)
                .build()
                .toUriString();
    }

    private WeatherApiResponse callApi(Spot spot, String url) {
        WeatherApiResponse response = restClient.get()
                .uri(url)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (request, clientResponse) -> {
                    log.error("External API returned error status. Spot name: {}, URL: {}", spot.getSpotName(),
                            url);
                    throw new GlobalException(GlobalErrorCode.EXTERNAL_API_ERROR);
                })
                .body(WeatherApiResponse.class);
        return response;
    }

    private WeatherType extractWeatherType(WeatherApiResponse response, Spot spot) {
        String icon = response.weather().get(0).icon();
        WeatherType weatherType = WeatherType.fromIcon(icon);
        if (weatherType == null) {
            log.error("Failed to determine WeatherType. Spot: '{}', Icon: '{}'", spot.getSpotName(), icon);
            throw new GlobalException(GlobalErrorCode.EXTERNAL_API_ERROR);
        }
        return weatherType;
    }
}
