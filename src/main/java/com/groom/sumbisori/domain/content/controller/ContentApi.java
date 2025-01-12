package com.groom.sumbisori.domain.content.controller;

import static com.groom.sumbisori.common.error.GlobalErrorCode.Const.EXTERNAL_API_ERROR;

import com.groom.sumbisori.common.error.GlobalErrorCode;
import com.groom.sumbisori.common.springdoc.ApiExceptionExplanation;
import com.groom.sumbisori.common.springdoc.ApiResponseExplanations;
import com.groom.sumbisori.domain.content.dto.response.YoutubeResponse;
import com.groom.sumbisori.domain.content.entity.Spot;
import com.groom.sumbisori.domain.wave.dto.WaveResponse;
import com.groom.sumbisori.domain.weather.dto.WeatherResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "contents", description = "콘텐츠 API")
public interface ContentApi {
    @Operation(summary = "유튜브 영상 목록 조회")
    ResponseEntity<List<YoutubeResponse>> getYoutubeList(
            @Parameter(description = "조회 갯수 (기본값: 5, 최소: 1, 최대값: 50)", example = "5")
            @RequestParam(required = false, defaultValue = "5") @Min(1) @Max(50) int count);

    @Operation(summary = "해양 정보 조회")
    @ApiResponseExplanations(
            errors = {
                    @ApiExceptionExplanation(value = GlobalErrorCode.class, constant = EXTERNAL_API_ERROR, name = "외부 api 호출 실패"),
            }
    )
    ResponseEntity<WaveResponse> getWaveInfo(
            @RequestParam(defaultValue = "jeju-harbor") Spot spot);

    @Operation(summary = "날씨 정보 조회")
    @ApiResponseExplanations(
            errors = {
                    @ApiExceptionExplanation(value = GlobalErrorCode.class, constant = EXTERNAL_API_ERROR, name = "외부 api 호출 실패"),
            }
    )
    ResponseEntity<WeatherResponse> getWeatherInfo(
            @RequestParam(defaultValue = "jeju-harbor") Spot spot);

}
