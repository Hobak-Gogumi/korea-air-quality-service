package com.example.air.infrastructure.api;

import com.example.air.infrastructure.api.busan.BusanAirQualityApiCaller;
import com.example.air.infrastructure.api.seoul.SeoulAirQualityApiCaller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AirQualityApiCallerFactory {

    @Autowired
    private SeoulAirQualityApiCaller seoulAirQualityApiCaller;
    @Autowired
    private BusanAirQualityApiCaller busanAirQualityApiCaller;

    public AirQualityApiCaller createAirQualityApiCaller(String city){
        switch(city){
            case "seoul":
                return seoulAirQualityApiCaller;
            case "busan":
                return busanAirQualityApiCaller;
            default:
                throw new RuntimeException(city + " 대기질 정보는 아직 준비중입니다.");
        }
    }
}
