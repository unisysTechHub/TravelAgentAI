package com.unisystechhub.ai.service;

import com.unisystechhub.ai.advisors.TravelRAGAdvisor;
import com.unisystechhub.ai.apigateway.AIGatewayService;
import com.unisystechhub.ai.cache.CacheDecision;
import com.unisystechhub.ai.cache.CachePolicyEngine;
import com.unisystechhub.ai.cache.ResponseCacheService;
import com.unisystechhub.ai.dto.ChatRequest;
import com.unisystechhub.ai.dto.ChatResponse;
import com.unisystechhub.ai.guardrails.GuardRailService;
import com.unisystechhub.ai.guardrails.PolicyDecission;
import com.unisystechhub.ai.security.PIIRedactionService;
import com.unisystechhub.ai.tools.ContactsTools;
import com.unisystechhub.ai.tools.WeatherTools;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TravelChatService {
    private final ChatClient chatClient;
    private  final ChatMemory chatMemory;
    private final ContactsTools contactsTools;
    private final VectorStore vectorStore;
    private final TravelRAGAdvisor travelRAGAdvisor;
    private final ToolCallbackProvider mcpToolProvider;
    private final AIGatewayService aiGatewayService;
    private final GuardRailService guardRailService;
    private final PIIRedactionService piiRedactionService;
    private final CachePolicyEngine cachePolicyEngine;
    private final ResponseCacheService responseCacheService;
    @Value("classpath:prompts/travelsystemprompts.st")
    private Resource System_Pormot;
    private final WeatherTools weatherTools;

    public Flux<String> stream(ChatRequest request){
       return aiGatewayService.stream(request.getMessage());

    }

   public ChatResponse chat(ChatRequest request){
       PromptTemplate promptTemplate = new PromptTemplate(System_Pormot);
      Message systemMessage = promptTemplate.createMessage(Map.of("attractionsPerDay",2,"foodsPerDay",2,"maxWords",50));

      PolicyDecission decision =guardRailService.validateUserRequest(request.getMessage());
      if(!decision.allow()) return new ChatResponse("",decision.reason());

     String sanitizedMessage = piiRedactionService.sanitize(request.getMessage());
     CacheDecision cacheDecision = cachePolicyEngine.evaluate(sanitizedMessage);

     if (cacheDecision.cacheable()){
         log.info("REQUEST IS CACHEABLE");
         String cachedResponse = responseCacheService.get(sanitizedMessage);
         if (cachedResponse != null )
             return new ChatResponse(request.getConversationId(),cachedResponse);

     } else{
             log.info("REQUEST is not cacheable");
         }


     Prompt prompt = new Prompt(systemMessage, new UserMessage(sanitizedMessage), new AssistantMessage("""
                        Day 1:
                         Attractions :
                            1.Senso JI  Temple
                           2. Toyco skytree
                         Food :
                            Suiss 1\s
                          \s
              \s"""));

       String conversationId = request.getConversationId() == null? UUID.randomUUID().toString() : request.getConversationId();
        String aiResponse = aiGatewayService.chat(prompt, advisorSpec -> advisorSpec.advisors(
                       MessageChatMemoryAdvisor.builder(chatMemory).build())
                        .param(ChatMemory.CONVERSATION_ID,conversationId).advisors(travelRAGAdvisor),contactsTools,weatherTools,mcpToolProvider);
//                chatClient.prompt(prompt)
//                .advisors( advisorSpec -> advisorSpec.advisors(
//                        MessageChatMemoryAdvisor.builder(chatMemory).build()
//                ).advisors(travelRAGAdvisor)
//                        .param(ChatMemory.CONVERSATION_ID,conversationId))
//                .tools(weatherTools,contactsTools,mcpToolProvider)
//                .system("""
//                        you are Travel Assistant Agent.
//                        Your responsibilites :
//                          - Help users plan trips and vacations
//                          - keep responses friendly and  concise.
//                       when creating  itineraries :
//                         - Suggest 2 attractions per day
//                         - Recommend 1 local food for each day
//                         - keep each day description should not exceed 50 words
//                       """)
//                .user(request.getMessage())
//                .call()
//                .content();

         if(cacheDecision.cacheable()){

             responseCacheService.put(sanitizedMessage,aiResponse);
         }
         return  new ChatResponse(conversationId,aiResponse);
    }
}
