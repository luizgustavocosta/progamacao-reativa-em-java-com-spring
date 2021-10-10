package com.costa.luiz.reactive.client;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static java.util.Objects.nonNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureWireMock(port = 8081)
class AppClientTest {

    @Autowired
    ClientCustomer client;

    @Test
    void consume()  {
        WireMock.stubFor(
                WireMock.get(WireMock.urlEqualTo("/customers"))
                        .willReturn(WireMock.aResponse()
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody("[{ \"id\":\"42\", \"name\":\"Blade Runner\" , \"middleName\":\"Blade Runner\", \"lastName\":\"Blade Runner\", \"becameCustomer\":\"\" }]")
                                .withStatus(HttpStatus.OK.value())));

        Flux<ClientCustomer.CustomerResponse> response = client.findAll();

        StepVerifier.create(response)
                .expectNextMatches(customerResponse ->
                        nonNull(customerResponse) && Character.isUpperCase(customerResponse.getName().charAt(0)))
                .verifyComplete();
    }

    @Test
    void basicForFluxAndMonoUsingStepVerifier() {
        StepVerifier.create(Mono.just("Just do it")).expectNext("Just do it").verifyComplete();
        StepVerifier.create(Flux.just("Follow", "the", "flow")).expectNext("Follow", "the", "flow").verifyComplete();
        //Or
        Flux.just("Follow", "the", "flow")
                .as(StepVerifier::create)
                .expectNext("Follow")
                .expectNext("the")
                .expectNext("flow")
                .verifyComplete();

        StepVerifier.create(Flux.just("Follow", "The", "Flow"))
                .assertNext(value -> assertTrue(value.length() > 2)) //Consumer
                .consumeNextWith(value -> assertTrue(Character.isLetter(value.charAt(0)))) //Consumer
                .expectNextMatches(value -> !value.endsWith("a")) //Predicate
                .verifyComplete();
    }

    @Test
    void handlingErrorsExample() {
        var errorMessage = "Database connection not ready";

        StepVerifier.create(Flux.error(new IllegalStateException(errorMessage)))
                .expectErrorMatches(exception -> errorMessage.equals(exception.getMessage()) &&
                        exception instanceof IllegalStateException)
                .verify();
    }

    @Test
    void basicWithWireMock() {
        var body = "[{\"id\": \"42\", \"name\": \"Getting started\"}]";
        WireMock.stubFor(
                WireMock.get(WireMock.urlEqualTo("/basic"))
                        .willReturn(WireMock.aResponse()
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(body)
                                .withStatus(HttpStatus.OK.value())));

        WebTestClient webTestClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:8081")
                .build();

        WebTestClient.ResponseSpec exchange = webTestClient.get().uri("/basic").exchange();
        exchange.expectStatus().is2xxSuccessful()
                .expectBody()
                .json(body);
    }
}