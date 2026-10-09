package com.unisystechhub.ai.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class EmailRedactionRule implements RedactionRule{
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile(
                    "([A-Za-z0-9])[A-Za-z0-9._%+-]*(@[A-Za-z0-9.-]+\\.[A-Za-z]{2,})"
            );
    @Override
    public String redact(String text) {
        Matcher matcher = EMAIL_PATTERN.matcher(text);
        StringBuffer stringBuffer = new StringBuffer();

        while (matcher.find()) {
            matcher.appendReplacement(
                    stringBuffer,
                    Matcher.quoteReplacement(
                            matcher.group(1) + "**" + matcher.group(2)
                    )
            );
        }

        matcher.appendTail(stringBuffer);

        return stringBuffer.toString();
    }
}
