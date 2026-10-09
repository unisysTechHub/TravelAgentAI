package com.unisystechhub.ai.service;

import com.unisystechhub.ai.apigateway.AIGatewayService;
import com.unisystechhub.ai.dto.ItineraryRequest;
import com.unisystechhub.ai.dto.ItineraryResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;
import java.util.logging.Logger;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItineraryService {
   private final ChatClient chatClient;
   private final AIGatewayService aiGatewayService;
   @Value("classpath:prompts/travelsystemprompts.st")
   private Resource systemPromptTemplate;
   private ObjectMapper objectMapper = new ObjectMapper();

   public ItineraryResponse prepareItinerary(ItineraryRequest request){
             PromptTemplate promptTemplate = new PromptTemplate(systemPromptTemplate);
          Message message =   promptTemplate.createMessage(Map.of("attractionsPerDay",2,"foodsPerDay",2,"maxWords",50));

          String userMessage = "Create {days} days itinererary to {destination}";
          Message input = new PromptTemplate(userMessage).createMessage(Map.of("days",request.getDays(),"destination",request.getDestination()));

          Prompt prompt = new Prompt(message, input);
return aiGatewayService.chatForEntity(prompt,ItineraryResponse.class);
       // return   chatClient.prompt(prompt).call().entity(ItineraryResponse.class);

   }
    public   ItineraryResponse improveItinerary(ItineraryResponse response){
        String itineraryString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(response);
        log.info("draft Itinerary : {}", itineraryString);
        String message = """
                 Review following itinerary
                 check:
                  -Logical city progress
                  - near by attractions
                  - Remove unncessarry travel
                 Return improved itinerary using same JSON structure
                 Itinerary:
                  {itinerary}\s
               \s""";
            Message reviewMessage = new PromptTemplate(message).createMessage(Map.of("itinerary",itineraryString));
            Prompt prompt = new Prompt(reviewMessage);
            return aiGatewayService.chatForEntity(prompt,ItineraryResponse.class);

       //  return    chatClient.prompt(prompt).call().entity(ItineraryResponse.class);

    }
}
