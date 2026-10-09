package com.unisystechhub.ai.controller;

import com.unisystechhub.ai.dto.ChatRequest;
import com.unisystechhub.ai.dto.ChatResponse;
import com.unisystechhub.ai.dto.ItineraryRequest;
import com.unisystechhub.ai.dto.ItineraryResponse;
import com.unisystechhub.ai.service.ItineraryService;
import com.unisystechhub.ai.service.TravelChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ChatController {
    final private TravelChatService travelChatService;
    final private ItineraryService itineraryService;
    final private ChatMemory chatMemory;
    @PostMapping("/chat/stream")
    Flux<String> chatStream(@RequestBody ChatRequest request){
        return travelChatService.stream(request);
    }
    @PostMapping("/chat")
    ChatResponse chat(@RequestBody ChatRequest request){
        return travelChatService.chat(request);
    }

    @PostMapping("/itinerary")
    ItineraryResponse generateItinerary(@RequestBody ItineraryRequest request){
         ItineraryResponse itineraryResponse = itineraryService.prepareItinerary(request);
        return itineraryService.improveItinerary(itineraryResponse);
    }

    @GetMapping("/memory")
    List<Message> fetchMemory(@RequestParam String conversationId){
        return  chatMemory.get(conversationId);
    }
}
