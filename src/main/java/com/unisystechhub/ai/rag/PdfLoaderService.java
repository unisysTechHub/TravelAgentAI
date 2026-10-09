package com.unisystechhub.ai.rag;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Component
public class PdfLoaderService {

    @Value("${app.data.pdfs.path}")
    String pdfFolder;

    List<Document> loadDocumetns() throws IOException {
        List<Document> documents = new ArrayList<>();
        Files.list(Path.of(pdfFolder))
                .filter(path -> path.toString().endsWith(".pdf"))
                .forEach( path ->{
                            PagePdfDocumentReader reader = new PagePdfDocumentReader(new FileSystemResource(path));
                            List<Document> pdfDocuments =reader.get();

                            pdfDocuments.forEach( document -> {
                                document.getMetadata().put("source", path.getFileName().toString());

                            });
                            documents.addAll(pdfDocuments);


                        }

                        );
        return documents;
    }
}
