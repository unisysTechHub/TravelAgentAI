package com.unisystechhub.ai.controller;

import com.unisystechhub.ai.dto.AudioChatResponse;
import com.unisystechhub.ai.dto.AudioUploadResponse;
import com.unisystechhub.ai.dto.ChatRequest;
import com.unisystechhub.ai.dto.ChatResponse;
import com.unisystechhub.ai.service.AudioService;
import com.unisystechhub.ai.service.TravelChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.audio.transcription.AudioTranscriptionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/audio")
public class AudioController {
    private final AudioService audioService;
    private final TravelChatService travelChatService;
    @PostMapping("/voice-assistant")
    ResponseEntity<byte[]> voiceAssistant(@RequestParam("file") MultipartFile file){
       AudioUploadResponse audioUploadResponse  =   audioService.store(file);
       String speechText =  audioService.speechToText(audioUploadResponse.getStoredFileName());
       ChatResponse chatResponse = travelChatService.chat(ChatRequest.builder().message(speechText).build());
      byte[] audio =  audioService.textToSpeech(chatResponse.getResponse());
      return  ResponseEntity.ok().header("Content-Type","audio/mpeg").body(audio);


    }

    @PostMapping("/to-speech")
    public ResponseEntity<byte[]> voiceChat(@RequestParam("message")String text)  {
        byte[] audio = audioService.textToSpeech(text);


        return ResponseEntity.ok().header("Content-Type","audio/mpeg").body(audio);
    }
    @PostMapping("/chat")
    public ResponseEntity<AudioChatResponse> voiceChat(@RequestParam("file")MultipartFile file)  {
        AudioUploadResponse response = audioService.store(file);
        String transcript =  audioService.speechToText(response.getStoredFileName());
      ChatResponse chatResponse =   travelChatService.chat(ChatRequest.builder().message(transcript).build());

        return ResponseEntity.ok(AudioChatResponse.builder().transcript(transcript).aiResponse(chatResponse.getResponse()).build());
    }
    @PostMapping("/upload")
   public ResponseEntity<AudioUploadResponse> upload(@RequestParam("file")MultipartFile file)  {
       AudioUploadResponse response = audioService.store(file);

       return  ResponseEntity.ok(response);
   }
    @PostMapping("/to-text")
    public ResponseEntity<ChatResponse> toText(@RequestParam("file")MultipartFile file)  {
        AudioUploadResponse response = audioService.store(file);
        String text =  audioService.speechToText(response.getStoredFileName());

        return ResponseEntity.ok(new ChatResponse("",text));
    }

}
