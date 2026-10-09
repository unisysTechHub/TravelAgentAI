package com.unisystechhub.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ChatRequest {
    String conversationId;
    String message;
    String imageName;
}
