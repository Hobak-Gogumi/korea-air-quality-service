package com.example.air.infrastructure.api.busan;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Objects;

public class BusanAirQualityApiDto {
    @Getter
    @Setter
    @ToString
    public static class GetAirQualityResponse {
        @JsonProperty("getAirQualityInfoClassifiedByStation")
        private Response response;
    }

    @Getter
    @Setter
    @ToString
    public static class Response {
        private Header header;
        private Body body;

        public boolean isSuccess() {
            if (Objects.equals(header.getCode(), "00")) {
                return true;
            }
            return false;
        }
    }

    @Getter
    @Setter
    @ToString
    public static class Header {
        @JsonProperty("resultCode")
        private String code;
        @JsonProperty("resultMsg")
        private String message;
    }

    @Getter
    @Setter
    @ToString
    public static class Body{
        private Items items;
        private Integer numOfRows;
        private Integer pageNo;
        private Integer totalCount;
    }

    @Getter
    @Setter
    @ToString
    public static class Items{
        private List<Row> item;
    }

    @Getter
    @Setter
    @ToString
    public static class Row {
        private String site;
        private String areaIndex;
        @JsonProperty("controlnumber")
        private String measurementTime;
        private String repItem;
        private String repVal;
        private String repCai;
        private String so2;
        private String so2Cai;
        private String no2;
        private String no2Cai;
        private String o3;
        private String o3Cai;
        private String co;
        private String coCai;
        private String pm25;
        private String pm25Cai;
        private String pm10;
        private String pm10Cai;
    }
}
