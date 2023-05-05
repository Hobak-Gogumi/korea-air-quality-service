package com.example.air.infrastructure.api.busan;

import com.example.air.application.AirQualityInfo;
import com.example.air.application.util.AirQualityGradeUtil;
import com.example.air.infrastructure.api.AirQualityApiCaller;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class BusanAirQualityApiCaller implements AirQualityApiCaller {
    private final BusanAirQualityApi busanAirQualityApi;

    public BusanAirQualityApiCaller(@Value("${api.busan.base-url}") String baseUrl) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .build();

        this.busanAirQualityApi = retrofit.create(BusanAirQualityApi.class);
    }

    public AirQualityInfo getAirQuality() {
        try {
            var call = busanAirQualityApi.getAirQuality();
            var response = call.execute().body();

            if (response == null || response.getResponse() == null) {
                throw new RuntimeException("[busan] getAirQuality 응답값이 존재하지 않습니다.");
            }

            if (response.getResponse().isSuccess()) {
                log.info(response.toString());
                return convert(response);
            }

            throw new RuntimeException("[busan] getAirQuality 응답이 올바르지 않습니다. header=" + response.getResponse().getHeader());

        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("[busan] getAirQuality API error 발생! errorMessage=" + e.getMessage());
        }
    }

    public AirQualityInfo getAirQuality(String gu) {
        try {
            var call = busanAirQualityApi.getAirQuality();
            var response = call.execute().body();

            if (response == null || response.getResponse() == null) {
                throw new RuntimeException("[busan] getAirQuality 응답값이 존재하지 않습니다.");
            }

            if (response.getResponse().isSuccess()) {
                log.info(response.toString());
                return convert(response, gu);
            }

            throw new RuntimeException("[busan] getAirQuality 응답이 올바르지 않습니다. header=" + response.getResponse().getHeader());

        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("[busan] getAirQuality API error 발생! errorMessage=" + e.getMessage());
        }
    }

    // 부산시 공공 API에서 조회한 정보를 AirQualityInfo로 변환해주는 함수
    private AirQualityInfo convert(BusanAirQualityApiDto.GetAirQualityResponse response){
        List<BusanAirQualityApiDto.Row> items = response.getResponse().getBody().getItems().getItem();
        Double sidoPm10Avg = averagePm10(items);
        String sidoPm10AvgGrade = AirQualityGradeUtil.getPm10Grade(sidoPm10Avg);
        List<AirQualityInfo.GuAirQualityInfo> guList = convert(items);

        return AirQualityInfo.builder()
                .sido("부산시")
                .sidoPm10Avg(sidoPm10Avg)
                .sidoPm10AvgGrade(sidoPm10AvgGrade)
                .guList(guList)
                .build();
    }

    private AirQualityInfo convert(BusanAirQualityApiDto.GetAirQualityResponse response, String gu){
        List<BusanAirQualityApiDto.Row> items = response.getResponse().getBody().getItems().getItem();
        Double sidoPm10Avg = averagePm10(items);
        String sidoPm10AvgGrade = AirQualityGradeUtil.getPm10Grade(sidoPm10Avg);
        List<AirQualityInfo.GuAirQualityInfo> guList = convert(items, gu);

        return AirQualityInfo.builder()
                .sido("부산시")
                .sidoPm10Avg(sidoPm10Avg)
                .sidoPm10AvgGrade(sidoPm10AvgGrade)
                .guList(guList)
                .build();
    }

    private List<AirQualityInfo.GuAirQualityInfo> convert(List<BusanAirQualityApiDto.Row> items){
        List<AirQualityInfo.GuAirQualityInfo> list = new ArrayList<>();

        for(BusanAirQualityApiDto.Row item: items){
            AirQualityInfo.GuAirQualityInfo info =  AirQualityInfo.GuAirQualityInfo.builder()
                    .guName(item.getSite())
                    .pm25(Integer.parseInt(item.getPm25()))
                    .pm25Grade(AirQualityGradeUtil.getPm25Grade(Double.parseDouble(item.getPm25())))
                    .pm10(Integer.parseInt(item.getPm10()))
                    .pm10Grade(AirQualityGradeUtil.getPm10Grade(Double.parseDouble(item.getPm10())))
                    .o3(Double.parseDouble(item.getO3()))
                    .o3Grade(AirQualityGradeUtil.getO3Grade(Double.parseDouble(item.getO3())))
                    .no2(Double.parseDouble(item.getNo2()))
                    .no2Grade(AirQualityGradeUtil.getNo2Grade(Double.parseDouble(item.getNo2())))
                    .co(Double.parseDouble(item.getCo()))
                    .coGrade(AirQualityGradeUtil.getCoGrade(Double.parseDouble(item.getCo())))
                    .so2(Double.parseDouble(item.getSo2()))
                    .so2Grade(AirQualityGradeUtil.getSo2Grade(Double.parseDouble(item.getSo2())))
                    .build();

            list.add(info);
        }

        return list;
    }

    private List<AirQualityInfo.GuAirQualityInfo> convert(List<BusanAirQualityApiDto.Row> items, String gu){
        List<AirQualityInfo.GuAirQualityInfo> list = new ArrayList<>();

        for(BusanAirQualityApiDto.Row item: items){

            if(item.getSite().equals(gu)){
                AirQualityInfo.GuAirQualityInfo info =  AirQualityInfo.GuAirQualityInfo.builder()
                        .guName(item.getSite())
                        .pm25(Integer.parseInt(item.getPm25()))
                        .pm25Grade(AirQualityGradeUtil.getPm25Grade(Double.parseDouble(item.getPm25())))
                        .pm10(Integer.parseInt(item.getPm10()))
                        .pm10Grade(AirQualityGradeUtil.getPm10Grade(Double.parseDouble(item.getPm10())))
                        .o3(Double.parseDouble(item.getO3()))
                        .o3Grade(AirQualityGradeUtil.getO3Grade(Double.parseDouble(item.getO3())))
                        .no2(Double.parseDouble(item.getNo2()))
                        .no2Grade(AirQualityGradeUtil.getNo2Grade(Double.parseDouble(item.getNo2())))
                        .co(Double.parseDouble(item.getCo()))
                        .coGrade(AirQualityGradeUtil.getCoGrade(Double.parseDouble(item.getCo())))
                        .so2(Double.parseDouble(item.getSo2()))
                        .so2Grade(AirQualityGradeUtil.getSo2Grade(Double.parseDouble(item.getSo2())))
                        .build();

                list.add(info);
            }

        }

        return list;
    }

    private Double averagePm10(List<BusanAirQualityApiDto.Row> items){
        double sum = 0;

        for(BusanAirQualityApiDto.Row item : items){
            sum += Integer.parseInt(item.getPm10());
        }

        double avg = sum / items.size();

        return avg;
    }
}
