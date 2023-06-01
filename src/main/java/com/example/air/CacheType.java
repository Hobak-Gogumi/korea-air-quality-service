package com.example.air;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CacheType {
    AirQuality("airQuality", 1, 10000);

    private final String cacheName;
    private final int expireAfterWrite; //만료시간
    private final int maximumSize;
}