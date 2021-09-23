package com.costa.luiz.reactive.customer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;

@WebFluxTest
@Import(CustomerRouter.class)
@ExtendWith(SpringExtension.class)
@AutoConfigureJsonTesters
@DisplayName("Customer Route")
@ActiveProfiles("test")
class CustomerRouterTest {

    String api = "/customers";

    CustomerRouter config = new CustomerRouter();

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CustomerRepository repository;

    Customer customer = Customer.builder().id(1L).name("Homer").build();

    @BeforeEach
    void setUp() {
        Mockito.when(repository.findAll()).thenReturn(Flux.just(customer));
    }

    @DisplayName("Stream")
    @Test
    void routeStream() {
        WebTestClient
                .bindToRouterFunction(config.customerRoutes(repository))
                .build()
                .get().uri(api + "/stream")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("text/event-stream;charset=UTF-8")
                .expectBody()
                .consumeWith(entityExchangeResult -> {
                    Assertions.assertEquals("data:{\"id\":1,\"name\":\"Homer\",\"middleName\":null,\"lastName\":null,\"becameCustomer\":null}\n\n",
                            new String(entityExchangeResult.getResponseBody(), StandardCharsets.UTF_8));
                });
    }

    @DisplayName("All")
    @Test
    void routeAll() throws JsonProcessingException {
        WebTestClient
                .bindToRouterFunction(config.customerRoutes(repository))
                .build()
                .get().uri(api)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .json(objectMapper.writeValueAsString(Collections.singleton(customer)));
    }

    @DisplayName("New")
    @Test
    void routeNew() throws JsonProcessingException {
        Mockito.when(repository.save(any(Customer.class)))
                .thenReturn(Mono.just(Customer.builder().id(2L).name("BoJack").build()));
        Customer customerRequest = Customer.builder().name("BoJack").build();
        WebTestClient
                .bindToRouterFunction(config.customerRoutes(repository))
                .build()
                .post().uri(api)
                .bodyValue(customerRequest)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON);
        Mockito.verify(repository, Mockito.atMostOnce()).save(customerRequest);
    }

    @DisplayName("Delete")
    @Test
    void routeDelete(){
        var id = 42L;
        Mockito.when(repository.deleteById(id)).thenReturn(Mono.empty());
        WebTestClient
                .bindToRouterFunction(config.customerRoutes(repository))
                .build()
                .delete().uri(api + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON);
        Mockito.verify(repository, Mockito.atMostOnce()).deleteById(id);
    }
}