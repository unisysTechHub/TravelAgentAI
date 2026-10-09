package com.unisystechhub.ai.advisors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.AdvisorChain;
import org.springframework.ai.chat.client.advisor.api.BaseAdvisor;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class TravelRAGAdvisor implements BaseAdvisor {
    private final VectorStore vectorStore;

    @Value("classpath:prompts/travel-rag-prompt.st")
    private Resource travel_rag_prompt;

    @Override
    public ChatClientRequest before(ChatClientRequest chatClientRequest, AdvisorChain advisorChain) {
        UserMessage userMessage = chatClientRequest.prompt().getUserMessage();
        String question = userMessage.getText();

        log.info("seraching vector store");
       List<Document> documents = vectorStore.similaritySearch(question);

       log.info("prepare context");

       String context = documents.stream()
                       .map(Document::getText)
               .collect(Collectors.joining(System.lineSeparator()+System.lineSeparator()));

        PromptTemplate template = new PromptTemplate(travel_rag_prompt);
       String  augumentedPrompt = template.render(Map.of("context",context,"question",question));


        return chatClientRequest.mutate().prompt(chatClientRequest.prompt().augmentSystemMessage(augumentedPrompt)).build();

    }

    @Override
    public ChatClientResponse after(ChatClientResponse chatClientResponse, AdvisorChain advisorChain) {
        return chatClientResponse;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
