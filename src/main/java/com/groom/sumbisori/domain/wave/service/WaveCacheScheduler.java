package com.groom.sumbisori.domain.wave.service;

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
public class WaveCacheScheduler implements ApplicationListener<ApplicationReadyEvent> {
    private final WaveRefreshService waveRefreshService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.debug("Initializing wave info for all spots");
        refreshAllSpots();
    }

    @Scheduled(cron = "0 1,31 * * * ?")
    public void refreshAllSpots() {
        log.debug("Refreshing wave info for all spots at 1 and 31 minutes past the hour");
        for (Spot spot : Spot.values()) {
            waveRefreshService.refresh(spot);
        }
    }
}
