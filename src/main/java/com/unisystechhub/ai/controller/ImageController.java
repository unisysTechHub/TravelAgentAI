package com.unisystechhub.ai.controller;

import com.unisystechhub.ai.dto.ChatRequest;
import com.unisystechhub.ai.dto.ChatResponse;
import com.unisystechhub.ai.service.ImageGenerationService;
import com.unisystechhub.ai.service.ImageUndestandingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/images")
public class ImageController {
    private final ImageUndestandingService imageUndestandingService;
    private final ImageGenerationService imageGenerationService;

    @PostMapping("/analyse")
    ChatResponse  analyseImage(@RequestBody ChatRequest request){
        return new ChatResponse("",imageUndestandingService.imageAnalysis(request));
    }
    @GetMapping("/generate")
    ResponseEntity<byte[]> generateImage(@RequestParam String  message){
          byte[] imageBytes = imageGenerationService.generateImage(message);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(imageBytes);
    }
}
