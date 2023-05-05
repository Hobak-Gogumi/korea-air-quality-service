package com.example.air.infrastructure.api;

import com.example.air.application.AirQualityInfo;

public interface AirQualityApiCaller {

    AirQualityInfo getAirQuality();
    AirQualityInfo getAirQuality(String gu);

}