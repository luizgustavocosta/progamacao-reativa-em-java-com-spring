package com.costa.luiz.reactive.review;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.nio.charset.StandardCharsets;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = ReviewController.class)
class ReviewControllerTest {

    @MockBean
    private ReviewRepository repository;

    @Autowired
    private WebTestClient webClient;

    @Test
    void findAll() {
        Mockito.when(repository.findAll()).thenReturn(Flux.just(review42(), review10()));
        webClient.get()
                .uri("/api/v1/reviews")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.length()").isEqualTo(2)
                .jsonPath("$[0].id").isEqualTo("42")
                .jsonPath("$[-1:].id").isEqualTo("10");
    }

    @Test
    void findAllStream() {
        Mockito.when(repository.findReviewsBy()).thenReturn(Flux.empty());
        webClient.get()
                .uri("/api/v1/reviews/stream")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void findByName() {
        var name = "Amazing";
        Mockito.when(repository.findReviewByName(name)).thenReturn(Flux.just(review10()));
        webClient.get()
                .uri("/api/v1/reviews/search?name={name}", name)
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(entityExchangeResult ->
                        Assertions.assertTrue(
                                new String(entityExchangeResult.getResponseBody(), StandardCharsets.UTF_8)
                                        .contains("data:{\"id\":\"10\",\"listing_Url\":null,\"name\":\"Amazing\",\"summary\":null}")));
    }

    Review review42() {
        return Review.builder()
                .id("42")
                .name("Universe answer")
                .build();
    }

    Review review10() {
        return Review.builder()
                .id("10")
                .name("Amazing")
                .build();
    }
}