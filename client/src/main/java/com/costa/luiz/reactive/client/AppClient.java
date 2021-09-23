package com.costa.luiz.reactive.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@SpringBootApplication
public class AppClient {

    @Autowired WebClient webClient;
    @Autowired ClientCustomer clientCustomer;

    public static void main(String[] args) {
        SpringApplication.run(AppClient.class, args);
    }

    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        return builder.baseUrl("http://localhost:8081").build();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void consumeAPI() {
        clientCustomer.findAll().log().subscribe();
    }

}
