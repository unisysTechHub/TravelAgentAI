package com.unisystechhub.ai.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class CacheConfig {

    @Bean
    Cache<String,String> airesponseCache(){
        return Caffeine.newBuilder().maximumSize(500).expireAfterWrite(Duration.ofHours(1)).recordStats().build();
    }
}
