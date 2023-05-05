package com.example.air.application;

import com.example.air.infrastructure.api.AirQualityApiCaller;
import com.example.air.infrastructure.api.AirQualityApiCallerFactory;
import com.example.air.infrastructure.api.busan.BusanAirQualityApiCaller;
import com.example.air.infrastructure.api.seoul.SeoulAirQualityApiCaller;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AirQualityService {
    private final SeoulAirQualityApiCaller seoulAirQualityApiCaller;
    private final BusanAirQualityApiCaller busanAirQualityApiCaller;
    private final AirQualityApiCallerFactory airQualityApiCallerFactory;

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
