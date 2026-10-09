package com.unisystechhub.ai.security;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class PassportNumberRedactRule implements RedactionRule{
    private static final Pattern PASSPORT_PATTERN =
            Pattern.compile("\\b([A-Z][0-9]{7})\\b");

    @Override
    public String redact(String text) {
        Matcher matcher = PASSPORT_PATTERN.matcher(text);
        StringBuilder sb = new StringBuilder();

        while (matcher.find()) {
            String passportNo = matcher.group(1);

            String masked = passportNo.charAt(0) + "*******";

            matcher.appendReplacement(
                    sb,
                    Matcher.quoteReplacement(masked)
            );
        }

        matcher.appendTail(sb);

        return sb.toString();
    }
}
