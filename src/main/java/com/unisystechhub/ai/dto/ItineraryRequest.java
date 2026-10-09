package com.unisystechhub.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ItineraryRequest {
    private String destination;
    private int days;

}
