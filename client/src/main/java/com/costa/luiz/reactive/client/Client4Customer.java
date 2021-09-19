package com.costa.luiz.reactive.client;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@SpringBootApplication
public class Client4Customer {

    @Autowired
    WebClient webClient;

    public static void main(String[] args) {
        SpringApplication.run(Client4Customer.class, args);
    }

    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        return builder.baseUrl("http://localhost:8081").build();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void ready() {
        AtomicLong count = new AtomicLong();
        webClient.get().uri("/customers")
                .retrieve()
                .bodyToMono(String.class)
                .log()
                .block();

        log.info("Calling stream");

        Flux.from(webClient.get().uri("/customers")
                .retrieve()
                .bodyToFlux(CustomerClient.class))
                .retry(2)
                .timeout(Duration.ofSeconds(10))
                .delayElements(Duration.ofSeconds(2))
                .doOnNext(row -> {
                    count.incrementAndGet();
                    log.info(row.toString());
                })
                .doOnComplete(() -> log.info("Received {} rows", count.get()))
                .doOnError(throwable -> {
                    log.error("******* Error to retrieve the customers *******");
                    log.error(throwable.getMessage());
                })
                .onErrorMap(throwable -> new IllegalStateException("Try again later"))
                .blockLast();
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class CustomerClient {
        private Long id;
        private String name;
        private String middleName;
        private String lastName;
        private LocalDate becameCustomer;
    }
}
