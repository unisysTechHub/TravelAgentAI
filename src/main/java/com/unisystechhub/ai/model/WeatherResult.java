package com.unisystechhub.ai.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class WeatherResult {
   private final String city;
   private final String date;
   private final Float temperature;
    private final String condition;

}
