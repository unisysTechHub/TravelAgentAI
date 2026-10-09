package com.unisystechhub.ai.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ForecastResponse {
    ForeCast forecast;

            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
          public static class ForeCast{
                List<ForeCastDay> forecastday;

            }
            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
           public static class ForeCastDay{
                String date;
                Day day;

            }
            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Day{
                Float avgtemp_c;
                Condition condition;

            }
            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
           public static class Condition{
                 String text;
            }
}
