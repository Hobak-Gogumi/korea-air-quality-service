package com.example.air.application;

import com.example.air.infrastructure.api.busan.BusanAirQualityApiCaller;
import com.example.air.infrastructure.api.seoul.SeoulAirQualityApiCaller;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AirQualityService {
    private final SeoulAirQualityApiCaller seoulAirQualityApiCaller;
    private final BusanAirQualityApiCaller busanAirQualityApiCaller;

    public AirQualityInfo getAirQualityInfo(String city, String gu) {
        AirQualityInfo airQualityInfo;

        if(city.equals("seoul")){
            if(gu!= null){
                airQualityInfo = seoulAirQualityApiCaller.getAirQuality(gu);
            } else{
                airQualityInfo = seoulAirQualityApiCaller.getAirQuality();
            }
        } else if(city.equals("busan")){
            if(gu != null){
                airQualityInfo = busanAirQualityApiCaller.getAirQuality(gu);
            } else{
                airQualityInfo = busanAirQualityApiCaller.getAirQuality();
            }
        } else{
            throw new RuntimeException(city + " 대기질 정보는 아직 준비중입니다.");
        }
        return airQualityInfo;
    }
}
