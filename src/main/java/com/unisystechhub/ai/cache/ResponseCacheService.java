package com.unisystechhub.ai.cache;

import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@Slf4j
@RequiredArgsConstructor
public class ResponseCacheService {
   private final Cache<String,String> airesponseCache;

   public String get(String request){
       String key = createCacheKey(request);

       String response =    airesponseCache.getIfPresent(key);
            if(response == null)
                log.info("CACHE MISS");
            else log.info("CACHE HIT");
            return response;

    }
    public void put(String request,String response){
        String key = createCacheKey(request);
        log.info("cache updated with key {}",key);
         airesponseCache.put(key,response);

    }
    String createCacheKey(String request){
        return request.toLowerCase().trim();
    }
}
