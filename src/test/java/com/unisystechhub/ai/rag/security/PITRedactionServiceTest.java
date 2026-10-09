package com.unisystechhub.ai.rag.security;

import com.unisystechhub.ai.TravelAgentSpringAiApplication;
import com.unisystechhub.ai.security.EmailRedactionRule;
import com.unisystechhub.ai.security.PassportNumberRedactRule;
import com.unisystechhub.ai.security.PhoneNumberRedactionRule;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static reactor.netty.http.HttpConnectionLiveness.log;


public class PITRedactionServiceTest {


    EmailRedactionRule emailRedactionRule= new EmailRedactionRule();

    PhoneNumberRedactionRule phoneNumberRedactionRule= new PhoneNumberRedactionRule();


    PassportNumberRedactRule passportNumberRedactRule = new PassportNumberRedactRule();

    @Test
    void shouldRedactEmail(){
        String sanitized = emailRedactionRule.redact("somename@gmail.com");
        assertEquals("s**@gmail.com", sanitized);


    }
    @Test
    void isPhoneNumberRedacted(){
       String masked = phoneNumberRedactionRule.redact("1234567890");
       log.info(masked);

       assertEquals("******7890", masked);
    }

    @Test
    void checkPassportNumberRedacted(){
       String masked = passportNumberRedactRule.redact("M1234567");

       assertEquals("M*******",masked);
    }
}
