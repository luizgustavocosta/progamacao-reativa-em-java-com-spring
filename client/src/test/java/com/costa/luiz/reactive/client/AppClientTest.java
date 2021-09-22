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
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.LocalDate;

import static java.util.Objects.nonNull;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureWireMock(port = 8081)
class AppClientTest {

    @Autowired
    ClientCustomer client;

    @Test
    void consume() {
        WireMock.stubFor(
                WireMock.get(WireMock.urlEqualTo("/customers"))
                        .willReturn(WireMock.aResponse()
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody("[{ \"id\":\"42\", \"name\":\"Blade Runner\" , \"middleName\":\"Blade Runner\", \"lastName\":\"Blade Runner\", \"becameCustomer\":\"" + LocalDate.now() + "\" }]")
                                .withStatus(HttpStatus.OK.value())));

        Flux<ClientCustomer.CustomerResponse> response = client.findAll();

        StepVerifier.create(response)
                .expectNextMatches(customerResponse ->
                        nonNull(customerResponse) && Character.isUpperCase(customerResponse.getName().charAt(0)))
                .verifyComplete();
    }
}