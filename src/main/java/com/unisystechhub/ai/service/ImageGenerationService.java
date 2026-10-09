package com.unisystechhub.ai.service;

import io.netty.handler.codec.base64.Base64Decoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.image.Image;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageGenerationService {
    private final ImageModel imageModel;

   public byte[] generateImage( String message){

        ImagePrompt imagePrompt = new ImagePrompt(message);
         ImageResponse imageResponse = imageModel.call(imagePrompt);
        Image image = imageResponse.getResult().getOutput();
         String base64Json = image.getB64Json();
        return Base64.getDecoder().decode(base64Json);

     }
}
