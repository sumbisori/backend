package com.groom.sumbisori.domain.content.controller;

import com.groom.sumbisori.domain.content.dto.response.YoutubeResponse;
import com.groom.sumbisori.domain.content.entity.Spot;
import com.groom.sumbisori.domain.content.service.YoutubeRandomPicker;
import com.groom.sumbisori.domain.wave.dto.WaveResponse;
import com.groom.sumbisori.domain.wave.service.WaveLookupService;
import com.groom.sumbisori.domain.weather.dto.WeatherResponse;
import com.groom.sumbisori.domain.weather.service.WeatherLookupService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/contents")
public class ContentController implements ContentApi {
    private final YoutubeRandomPicker youtubeRandomPicker;
    private final WaveLookupService waveLookupService;
    private final WeatherLookupService weatherLookupService;

    @Override
    @GetMapping("/youtube")
    public ResponseEntity<List<YoutubeResponse>> getYoutubeList(
            @RequestParam(required = false, defaultValue = "5") @Min(1) @Max(50) int count) {
        return ResponseEntity.ok(youtubeRandomPicker.lookup(count));
    }

    @Override
    @GetMapping("/wave")
    public ResponseEntity<WaveResponse> getWaveInfo(@RequestParam(defaultValue = "jeju-harbor") Spot spot) {
        return ResponseEntity.ok(waveLookupService.lookup(spot));
    }

    @Override
    @GetMapping("/weather")
    public ResponseEntity<WeatherResponse> getWeatherInfo(@RequestParam(defaultValue = "jeju-harbor") Spot spot) {
        return ResponseEntity.ok(weatherLookupService.lookup(spot));
    }
}
