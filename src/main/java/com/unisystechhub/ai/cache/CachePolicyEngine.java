package com.unisystechhub.ai.cache;

import com.unisystechhub.ai.apigateway.AIGatewayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class CachePolicyEngine {
    private final ChatClient chatClient;
    @Value("classpath:prompts/cache-policy-prompt.st")
    private String cachePolicyTemplate;

    public CacheDecision evaluate(String request){
        PromptTemplate promptTemplate = new PromptTemplate(cachePolicyTemplate);
       Message message = promptTemplate.createMessage(Map.of("request",request));
       Prompt prompt = new Prompt(message);
      return chatClient.prompt(prompt).call().entity(CacheDecision.class);
    }




}
