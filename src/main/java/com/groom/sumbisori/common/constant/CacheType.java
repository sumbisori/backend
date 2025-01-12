package com.groom.sumbisori.common.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CacheType {
    PLACES("places", 60 * 60 * 24 * 30, 20),          // 30일
    PLACE_DETAILS("placeDetails", 60 * 60 * 24 * 30, 20), // 30일
    YOUTUBES("youtubes", 60 * 60 * 24, 60),           // 24시간
    WAVES("waves", 60 * 62, 20),                      // 1시간 2분
    WEATHERS("weathers", 60 * 90, 20);                // 1시간 30분

    private final String cacheName;
    private final int expireSeconds;
    private final int maxSize;
}
