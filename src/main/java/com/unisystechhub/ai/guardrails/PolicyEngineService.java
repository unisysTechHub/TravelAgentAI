package com.unisystechhub.ai.guardrails;

import com.unisystechhub.ai.apigateway.AIGatewayService;
import com.unisystechhub.ai.dto.ChatRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class PolicyEngineService {
    private final AIGatewayService aiGatewayService;
    @Value("classpath:prompts/policy-engine-prompt.st")
    private Resource policyEnginePrompt;

    public PolicyDecission evaluateUserRequest(String request) {
        PromptTemplate promptTemplate = new PromptTemplate(policyEnginePrompt);
        Message message = promptTemplate.createMessage(Map.of("request", request));
        Prompt prompt = new Prompt(message);
        return aiGatewayService.chatForEntity(prompt, PolicyDecission.class);
    }

}
