package com.groom.sumbisori.domain.weather.service;

import com.groom.sumbisori.domain.content.entity.Spot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
@Profile("prod")
public class WeatherCacheScheduler implements ApplicationListener<ApplicationReadyEvent> {
    private final WeatherRefreshService weatherRefreshService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info("Initializing weather info for all spots");
        refreshAllSpots();
    }

    @Scheduled(cron = "0 0 * * * ?") // 정각마다 실행
    public void refreshAllSpots() {
        log.info("Refreshing weather info for all spots started");
        for (Spot spot : Spot.values()) {
            weatherRefreshService.refresh(spot);
        }
    }
}
