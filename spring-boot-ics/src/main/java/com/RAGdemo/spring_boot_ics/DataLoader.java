package com.RAGdemo.spring_boot_ics;

import jakarta.annotation.PostConstruct;
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

        System.out.println("No of Records n the PG Vectore Store: " + count);

        if(count == 0){
            System.out.println("Loading Indian Constitution in pdf format from pgvectore store");
        }
    }

}

