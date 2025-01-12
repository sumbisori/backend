package com.groom.sumbisori.domain.wave.service;

import com.groom.sumbisori.common.error.GlobalErrorCode;
import com.groom.sumbisori.common.error.GlobalException;
import com.groom.sumbisori.domain.content.entity.Spot;
import com.groom.sumbisori.domain.wave.dto.WaveResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 외부 API로부터 전달받은 Raw(문자열) 데이터를 파싱하여 WaveResponse 객체로 변환하는 역할을 담당.
 */
@Component
@Slf4j
public class WaveResponseParser {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

    private static final int MINIMUM_LINE_LENGTH = 5;
    private static final int DATA_LINE_INDEX = 3;
    private static final int MINIMUM_FIELD_COUNT = 14;
    private static final int FIELD_WAVE_HEIGHT = 6;
    private static final int FIELD_WATER_TEMPERATURE = 10;
    private static final int FIELD_OBSERVATION_TIME = 1;

    /**
     * 응답 문자열을 분석(파싱)하여 WaveResponse 객체로 변환
     */
    public WaveResponse parse(String response, Spot spot) {
        String[] lines = response.split("\n");
        validateResponseLength(lines, spot);

        String dataLine = lines[DATA_LINE_INDEX].trim();
        String[] fields = dataLine.split(",");
        validateFieldCount(fields, dataLine, spot);

        double waveHeight = parseToDouble(fields[FIELD_WAVE_HEIGHT]);
        double waterTemperature = parseToDouble(fields[FIELD_WATER_TEMPERATURE]);
        LocalDateTime observationTime = parseToLocalDateTime(fields[FIELD_OBSERVATION_TIME]);
        return WaveResponse.of(waveHeight, waterTemperature, observationTime);
    }

    /**
     * 응답 라인 수를 검증
     */
    private void validateResponseLength(String[] lines, Spot spot) {
        if (lines.length < MINIMUM_LINE_LENGTH) {
            log.error("Invalid response length: {}. Spot: {}, lines: {}", lines.length, spot.getSpotName(), Arrays.toString(lines));
            throw new GlobalException(GlobalErrorCode.EXTERNAL_API_ERROR);
        }
    }

    /**
     * 데이터 필드 개수 검증
     */
    private void validateFieldCount(String[] fields, String rawLine, Spot spot) {
        if (fields.length < MINIMUM_FIELD_COUNT) {
            log.error(
                    "Insufficient fields in response line. Expected >= {}, Found: {}. Spot: {}, fields: {}, rawLine: '{}'",
                    MINIMUM_FIELD_COUNT, fields.length, spot.getSpotName(), Arrays.toString(fields), rawLine
            );
            throw new GlobalException(GlobalErrorCode.EXTERNAL_API_ERROR);
        }
    }

    /**
     * 문자열을 double로 변환
     */
    private double parseToDouble(String value) {
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException ex) {
            log.error("Invalid format for double value: {}", value);
            throw new GlobalException(GlobalErrorCode.EXTERNAL_API_ERROR);
        }
    }

    /**
     * 문자열을 LocalDateTime으로 변환
     */
    private LocalDateTime parseToLocalDateTime(String value) {
        try {
            return LocalDateTime.parse(value.trim(), DATE_TIME_FORMATTER);
        } catch (Exception ex) {
            log.error("Invalid date format: {}", value);
            throw new GlobalException(GlobalErrorCode.EXTERNAL_API_ERROR);
        }
    }
}
