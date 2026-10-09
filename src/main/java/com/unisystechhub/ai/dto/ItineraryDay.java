package com.unisystechhub.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ItineraryDay {
    private List<String> attractions;
    private String food;
}
