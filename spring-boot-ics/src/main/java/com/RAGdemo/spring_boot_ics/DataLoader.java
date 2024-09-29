package com.RAGdemo.spring_boot_ics;

import jakarta.annotation.PostConstruct;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

@Component
public class DataLoader {

    private final VectorStore vectorStore;

    private final JdbcClient jdbcClient;

    @Value("classpath:/Indian_Constitution.pdf")
    private Resource pdfResource;

    public DataLoader(VectorStore vectorStore, JdbcClient jdbcClient, JdbcClient jdbcClient1) {
        this.vectorStore = vectorStore;
        this.jdbcClient = jdbcClient1;
    }

    @PostConstruct
    public void init(){
        Integer count = jdbcClient.sql("Select COUNT(*) from vectore_store")
                .query(Integer.class).single();

        System.out.println("No of Records n the PG Vector Store: " + count);

//        simple check if data is present or not
        if(count == 0){
            System.out.println("Loading Indian Constitution in pdf format from pgvectore store");

//            Pdf file configuration
            PdfDocumentReaderConfig config = PdfDocumentReaderConfig.builder()
                    .withPagesPerDocument(1)
                    .build();
            PagePdfDocumentReader reader = new PagePdfDocumentReader(pdfResource,config);

            var textSplitter = new TokenTextSplitter();
            vectorStore.accept(textSplitter.apply(reader.get()));

            System.out.println("Application is ready to new Requests : ");

        }
    }

}

