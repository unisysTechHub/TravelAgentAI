package com.unisystechhub.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.scheduling.support.SimpleTriggerContext;

@Data
@AllArgsConstructor
@Builder
public class AudioUploadResponse {
    private String fileId;
    private String originalFileName;
    private String storedFileName;
    private String contentType;
    private long size;


}
