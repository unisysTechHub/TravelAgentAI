package com.unisystechhub.ai.rag;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class DocumentIndexingServicesTest {

    @Autowired
    DocumentIndexingService documentIndexingService;

    @Test
    void shouldIndexDocuments() throws IOException {
        documentIndexingService.deleteAllDocuments();
        documentIndexingService.indexDocuments();
    }
}
