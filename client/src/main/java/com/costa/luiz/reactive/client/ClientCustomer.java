package com.costa.luiz.reactive.client;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicLong;

@AllArgsConstructor
@Component
@Slf4j
public class ClientCustomer {

    @Autowired
    private final WebClient webClient;

    public Flux<CustomerResponse> findAll() {
        AtomicLong count = new AtomicLong();
        log.info("Calling the stream");

        return Flux.from(webClient.get().uri("/customers")
                .retrieve()
                .bodyToFlux(CustomerResponse.class))
                .retry(2)
                .timeout(Duration.ofSeconds(5))
                .delayElements(Duration.ofSeconds(2))
                .doOnNext(row -> count.incrementAndGet())
                .doOnComplete(() -> log.info("Received {} rows", count.get()))
                .doOnError(throwable -> {
                    log.error("******* Error to retrieve the customers *******");
                    log.error(throwable.getMessage());
                })
                .onErrorMap(throwable -> new IllegalStateException("Try again later"));
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class CustomerResponse {
        private Long id;
        private String name;
        private String middleName;
        private String lastName;
        private LocalDate becameCustomer;
    }
}
