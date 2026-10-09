package com.unisystechhub.ai.service;

import com.openai.models.audio.AudioResponseFormat;
import com.unisystechhub.ai.dto.AudioUploadResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.audio.transcription.AudioTranscriptionResponse;
import org.springframework.ai.audio.transcription.TranscriptionModel;
import org.springframework.ai.audio.tts.TextToSpeechModel;
import org.springframework.ai.audio.tts.TextToSpeechPrompt;
import org.springframework.ai.audio.tts.TextToSpeechResponse;
import org.springframework.ai.openai.OpenAiAudioSpeechOptions;
import org.springframework.ai.openai.OpenAiAudioTranscriptionOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AudioService {
    @Value("${app.data.audio.updload-dir}")
    String uploadDir;
    private final TranscriptionModel transcriptionModel;
    private final TextToSpeechModel textToSpeechModel;
    public AudioUploadResponse store(MultipartFile file)  {
        try {
            Path audioDir = Path.of(uploadDir);
            Files.createDirectories(audioDir);
            String fileId = UUID.randomUUID().toString();
            String storeFileName = fileId+ "_"+ file.getOriginalFilename();
            Path targetPath =   audioDir.resolve(storeFileName);
            Files.copy(file.getInputStream(),targetPath);

            return  AudioUploadResponse.builder().fileId(fileId)
                    .originalFileName(file.getOriginalFilename())
                    .storedFileName(storeFileName)
                    .size(file.getSize()).build();
        }catch (Exception e){
            throw new RuntimeException();
        }

    }
    public String speechToText( String storeFileName){

        try {
            Path audioPath = Path.of(uploadDir).resolve(storeFileName);
            Resource audio =   new FileSystemResource(audioPath);
            OpenAiAudioTranscriptionOptions options =
                    OpenAiAudioTranscriptionOptions.builder()
                            .responseFormat(AudioResponseFormat.JSON).build();

            AudioTranscriptionPrompt audioTranscriptionPrompt = new AudioTranscriptionPrompt(audio,options);

          AudioTranscriptionResponse audioTranscriptionResponse = transcriptionModel.call(audioTranscriptionPrompt);
           return audioTranscriptionResponse.getResult().getOutput();

        }catch (Exception e){
            throw  new RuntimeException(e);
        }


    }
    public byte[] textToSpeech(String text){
        OpenAiAudioSpeechOptions options = OpenAiAudioSpeechOptions.builder().voice(OpenAiAudioSpeechOptions.Voice.NOVA).build();
        TextToSpeechPrompt prompt = new TextToSpeechPrompt(text,options);

        TextToSpeechResponse response =textToSpeechModel.call(prompt);

        return response.getResult().getOutput();

    }

}
