package com.groom.sumbisori.domain.weather.entity;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@AllArgsConstructor
@Slf4j
public enum WeatherType {
    CLEAR_SKY("01"),
    FEW_CLOUDS("02"),
    SCATTERED_CLOUDS("03"),
    BROKEN_CLOUDS("04"),
    SHOWER_RAIN("09"),
    RAIN("10"),
    THUNDERSTORM("11"),
    SNOW("13"),
    MIST("50");

    private static final Map<String, WeatherType> ICON_CODE_MAP =
            Arrays.stream(values())
                    .collect(Collectors.toMap(WeatherType::getIconCode, type -> type));

    private final String iconCode;

    public static WeatherType fromIcon(String icon) {
        if(icon == null || icon.length() < 2) {
            log.error("Invalid icon code: {}", icon);
            return null;
        }
        String iconNumber = icon.substring(0, 2); // "01d" -> "01"
        WeatherType weatherType = ICON_CODE_MAP.get(iconNumber);
        return weatherType;
    }

}
