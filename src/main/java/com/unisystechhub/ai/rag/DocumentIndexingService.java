package com.unisystechhub.ai.rag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DocumentIndexingService {
    private final PdfLoaderService pdfLoader;
    private final VectorStore vectorStore;
    private final JdbcTemplate jdbcTemplate;

    void indexDocuments() throws IOException {
        List<Document> documents = pdfLoader.loadDocumetns();
        TokenTextSplitter tokenTextSplitter = TokenTextSplitter.builder().build();
        List<Document> chunks = tokenTextSplitter.apply(documents);
        vectorStore.add(chunks);

        log.info("indexed {} documents into pgVector ",chunks.size());
    }

    public void deleteAllDocuments(){
        jdbcTemplate.execute("TRUNCATE TABLE  vector_store RESTART IDENTITY " );
        log.info("delete all documents from vector store");
    }

}
