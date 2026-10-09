package com.unisystechhub.ai.apigateway;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
@Slf4j
public class AIGatewayService {
    private final ChatClient chatClient;

    public String chat(Prompt prompt, Consumer<ChatClient.AdvisorSpec>advisorSpecConsumer,Object... tools){

        log.info("AI Gateway AI chat request before hit LLM ");
        long startTime=System.currentTimeMillis();
        String  message = chatClient.prompt(prompt).advisors(advisorSpecConsumer)
                .tools(tools)
                .call()
                .content();
        long duration = System.currentTimeMillis() - startTime;
        log.info("AI Gateway request completed in {} ms ",duration);

        return  message;

    }
    public <T> T chatForEntity(Prompt prompt,Class<T> responseType ){
        log.info("AI Gateway AI with Entity response  ");
        long startTime=System.currentTimeMillis();
        T  message = chatClient.prompt(prompt)
                .call()
                .entity(responseType);
        long duration = System.currentTimeMillis() - startTime;
        log.info("AI Gateway with Entity  request completed in {} ms ",duration);

        return  message;
    }

  public Flux<String> stream(String message){
      log.info("AI Gateway AI chat stream  ");
      long startTime=System.currentTimeMillis();
    Flux<String> stream = chatClient.prompt().user(message).stream().content();
      long duration = System.currentTimeMillis() - startTime;
      log.info("AI Gateway with Entity  request completed in {} ms ",duration);
      return stream;
  }
}
