package com.costa.luiz.reactive.review;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.assertions.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.nio.charset.StandardCharsets;
import java.util.Collections;


@WebFluxTest
@Import(RouteReview.class)
@ExtendWith(SpringExtension.class)
@AutoConfigureJsonTesters
@DisplayName("Route Review")
class RouteReviewTest {

    private final RouteReview routeReview = new RouteReview();

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReviewRepository repository;

    Review universe = Review.builder().id("42").build();

    @BeforeEach
    void setUp() {
        Mockito.when(repository.findAll()).thenReturn(Flux.just(Review.builder().id("42").build()));
    }

    @DisplayName("Route review as Json")
    @Test
    void routeReviews() throws JsonProcessingException {
        String expectedJson = objectMapper.writeValueAsString(Collections.singleton(universe));
        WebTestClient
                .bindToRouterFunction(routeReview.routesGateway(repository))
                .build()
                .get().uri("/reviews")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .json(expectedJson);
    }

    @DisplayName("Route review as Stream")
    @Test
    void routesReviewsStream()  {
        WebTestClient
                .bindToRouterFunction(routeReview.routesGateway(repository))
                .build()
                .get().uri("/reviews/stream")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("text/event-stream;charset=UTF-8")
                .expectBody()
                .consumeWith(entityExchangeResult ->
                        Assertions.assertTrue(
                                new String(entityExchangeResult.getResponseBody(), StandardCharsets.UTF_8)
                                .contains("data:{\"id\":\"42\",\"listing_Url\":null,\"name\":null,\"summary\":null}"))
                );
    }

    @DisplayName("Route review as Live-Stream")
    @Test
    void routesReviewsLiveStream()  {
        Mockito.when(repository.findReviewsBy()).thenReturn(Flux.just(universe));
        WebTestClient
                .bindToRouterFunction(routeReview.routesGateway(repository))
                .build()
                .get().uri("/reviews/live-stream")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("text/event-stream;charset=UTF-8")
                .expectBody()
                .consumeWith(entityExchangeResult ->
                        Assertions.assertTrue(
                                new String(entityExchangeResult.getResponseBody(), StandardCharsets.UTF_8)
                                .contains("data:{\"id\":\"42\",\"listing_Url\":null,\"name\":null,\"summary\":null}"))
                );
    }

}