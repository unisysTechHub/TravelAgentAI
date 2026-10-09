package com.unisystechhub.ai.tools;

import com.unisystechhub.ai.model.ForecastResponse;
import com.unisystechhub.ai.model.WeatherResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
@Slf4j
public class WeatherTools {

    private final RestTemplate restTemplate;
    @Value("${app.weather.api-key}")
    String api_key;

    @Tool(description = "Get tomorrows's weather forecast for given city")
    public WeatherResult getWeather(String city, String date) {
        log.info("get weather ");
     String url =   UriComponentsBuilder.fromUriString("http://api.weatherapi.com/v1/forecast.json")
                .queryParam("key",api_key)
                .queryParam("q",city)
                .queryParam("dt",date)
                .queryParam("","").toUriString();

     try {
         ForecastResponse response =    restTemplate.getForObject(url, ForecastResponse.class);

         if( response == null){
             return  new WeatherResult(city,date,-1.0f,"weather report error");
         } else{
             String condition = response.getForecast().getForecastday().get(0).getDay().getCondition().getText();
             Float temp  = response.getForecast().getForecastday().get(0).getDay().getAvgtemp_c();
             return new WeatherResult(city,date,temp,condition);

            // return String.format( "weather in %s on %s is %.1f,%s ",city,date,temp, condition);
         }
     } catch (Exception e) {
         log.error(e.getLocalizedMessage());

       return   new WeatherResult(city,date,-1.0f,"Weather Api error " + e.getLocalizedMessage());
     }

    }
}