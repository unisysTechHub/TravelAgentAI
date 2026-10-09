package com.unisystechhub.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@Data
@AllArgsConstructor
public class ItineraryResponse {
    private String destination;
    private List<ItineraryDay> itineraryDays;
    private String summary;
}
