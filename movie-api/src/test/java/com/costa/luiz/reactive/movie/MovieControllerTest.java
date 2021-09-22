package com.costa.luiz.reactive.movie;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
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
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

import static com.costa.luiz.reactive.movie.MovieMock.*;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = MovieController.class)
@DisplayName("Movie Controller")
class MovieControllerTest {

    @MockBean
    private MovieService service;

    @Autowired
    private WebTestClient webClient;

    @DisplayName("Find all")
    @Test
    void findAll() {
        Mockito.when(service.findAll()).thenReturn(Flux.just(avengers(), theHulk(), chapolin()));
        webClient.get()
                .uri("/api/v1/movies")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.length()").isEqualTo(3)
                .jsonPath("$[0].id").isEqualTo(avengers().getId())
                .jsonPath("$[-1:].id").isEqualTo(chapolin().getId())
                .jsonPath("$[-1:].name").isEqualTo(chapolin().getName());
    }

    @DisplayName("Find by id")
    @Test
    void findById() {
        var id = "10";
        Mockito.when(service.findById(id)).thenReturn(Mono.just(chapolin()));
        webClient.get()
                .uri("/api/v1/movies/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .json("{\"id\":\"10\",\"name\":\"El Chapolin\",\"director\":null,\"duration\":120}");
    }

    @DisplayName("Find all as Stream")
    @Test
    void findAllStream() {
        Mockito.when(service.findAllStream()).thenReturn(Flux.just(theHulk(), avengers()));
        webClient.get()
                .uri("/api/v1/movies/stream")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(entityExchangeResult ->
                        Assertions.assertEquals("data:{\"id\":\"2\",\"name\":\"The Hulk\",\"director\":null,\"duration\":200}\n\n" +
                                "data:{\"id\":\"1\",\"name\":\"Avengers\",\"director\":null,\"duration\":100}\n\n", new String(entityExchangeResult.getResponseBody(), StandardCharsets.UTF_8)));
    }

    @DisplayName("Find by name")
    @Test
    void findByName() {
        var name = "Hulk";
        Mockito.when(service.findMovieByName(name)).thenReturn(Flux.just(theHulk()));
        webClient.get()
                .uri("/api/v1/movies/search?name={name}", name)
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(entityExchangeResult ->
                        Assertions.assertEquals("data:{\"id\":\"2\",\"name\":\"The Hulk\",\"director\":null,\"duration\":200}\n\n", new String(entityExchangeResult.getResponseBody(), StandardCharsets.UTF_8)));
    }
}