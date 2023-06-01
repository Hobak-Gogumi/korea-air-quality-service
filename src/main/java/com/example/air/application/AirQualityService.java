package com.example.air.application;

import com.example.air.infrastructure.api.AirQualityApiCaller;
import com.example.air.infrastructure.api.AirQualityApiCallerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AirQualityService {

    private final AirQualityApiCallerFactory airQualityApiCallerFactory;

    @Cacheable(cacheNames= "airQuality", key = "#city + #gu + T(java.time.LocalTime).now().getHour().toString()")
    public AirQualityInfo getAirQualityInfo(String city, String gu) {
        AirQualityInfo airQualityInfo;

        AirQualityApiCaller airQualityApiCaller = airQualityApiCallerFactory.createAirQualityApiCaller(city);
        if(gu != null){
            airQualityInfo = airQualityApiCaller.getAirQuality(gu);
        } else{
            airQualityInfo = airQualityApiCaller.getAirQuality();
        }

        return airQualityInfo;
    }
}
