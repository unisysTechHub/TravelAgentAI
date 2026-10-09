package com.unisystechhub.ai.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PIIRedactionService {
    private final List<RedactionRule> redactionRules;

    public String sanitize(String text){
        log.info("++++++++++++++++++++");
        log.info("PII Reduction");
        log.info("Original request {}",text);
        String sanitized = text;
        for (RedactionRule rule : redactionRules){
           sanitized= rule.redact(sanitized);
        }
        log.info("Sanitized request {}",sanitized);

        return sanitized;
    }

}
