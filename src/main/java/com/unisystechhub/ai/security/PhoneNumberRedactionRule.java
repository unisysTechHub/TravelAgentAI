package com.unisystechhub.ai.security;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class PhoneNumberRedactionRule  implements RedactionRule{
    private static final Pattern PHONE_NUMBER_PATTERN =
            Pattern.compile(
                    "\\b((?:\\+?\\d{1,3}[-.\\s]?)?(?:\\(?\\d{3}\\)?[-.\\s]?)?\\d{3}[-.\\s]?\\d{4})\\b"
            );
    @Override
    public String redact(String text) {
        Matcher matcher = PHONE_NUMBER_PATTERN.matcher(text);
        StringBuilder sb = new StringBuilder();

        while (matcher.find()) {
            String phone = matcher.group(1);

            // Remove separators so we can safely get the last 4 digits
            String digits = phone.replaceAll("\\D", "");

            String last4 = digits.substring(digits.length() - 4);
            String masked = "******" + last4;

            matcher.appendReplacement(
                    sb,
                    Matcher.quoteReplacement(masked)
            );
        }

        matcher.appendTail(sb);

        return sb.toString();
    }
}
