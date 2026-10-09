package com.unisystechhub.ai.service;

import com.unisystechhub.ai.dto.ChatRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.content.Media;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import java.io.FileInputStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageUndestandingService {
    private final ChatClient chatClient;
    @Value("${app.data.images.path}")
    private  String imagesFolder;

    public String imageAnalysis(ChatRequest request){
         String imageName = request.getImageName();
         String userMessage = request.getMessage();

      return   chatClient.prompt().user( spec -> {
             Resource image = new FileSystemResource(imagesFolder+imageName);
             spec.text(userMessage).media(MimeTypeUtils.IMAGE_JPEG,image);
         }).call().content();



    }

}
