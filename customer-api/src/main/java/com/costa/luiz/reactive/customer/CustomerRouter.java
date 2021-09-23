package com.costa.luiz.reactive.customer;

import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.springdoc.webflux.core.fn.SpringdocRouteBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.parameter.Builder.parameterBuilder;
import static org.springdoc.core.fn.builders.requestbody.Builder.requestBodyBuilder;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RequestPredicates.contentType;

@Component
public class CustomerRouter {

    @Bean
    RouterFunction<ServerResponse> customerRoutes(CustomerRepository repository) {
        var tagValue = "Customers";
        var api = "customers";
        return SpringdocRouteBuilder.route().GET(api,
                accept(APPLICATION_JSON), serverRequest ->
                        ServerResponse
                                .ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(repository.findAll().log(), Customer.class), ops -> ops.tag(tagValue)
                        .operationId("Get all customers").response(responseBuilder().responseCode("200"))).build()
                .and(SpringdocRouteBuilder.route().GET(api + "/stream",
                        accept(APPLICATION_JSON), serverRequest ->
                                ServerResponse
                                        .ok()
                                        .contentType(MediaType.TEXT_EVENT_STREAM)
                                        .body(repository.findAll().log(), Customer.class), ops -> ops.tag(tagValue)
                                .operationId("Get all customers in Stream mode").response(responseBuilder().responseCode("200"))).build())
                .and(SpringdocRouteBuilder.route().POST(api, accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                        servletRequest -> ServerResponse
                                .ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(servletRequest.bodyToMono(Customer.class)
                                        .map(customer -> {
                                            customer.setId(null);
                                            return customer;
                                        })
                                        .flatMap(repository::save), Customer.class),
                        ops -> ops.tag(tagValue)
                                .operationId("New customer")
                                .requestBody(requestBodyBuilder().implementation(Customer.class))
                                .response(responseBuilder().responseCode(HttpStatus.OK.getReasonPhrase()).implementation(String.class))).build())

                .and(SpringdocRouteBuilder.route().PUT(api, accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                        servletRequest -> ServerResponse
                                .ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(servletRequest.bodyToMono(Customer.class)
                                        .flatMap(repository::save), Customer.class),
                        ops -> ops.tag(tagValue)
                                .operationId("Change customer")
                                .requestBody(requestBodyBuilder().implementation(Customer.class))
                                .response(responseBuilder().responseCode(HttpStatus.OK.getReasonPhrase()).implementation(String.class))).build())

                .and(SpringdocRouteBuilder.route().DELETE(api + "/{id}",
                        accept(APPLICATION_JSON), serverRequest -> ServerResponse
                                .ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(repository.deleteById(Long.valueOf(serverRequest.pathVariable("id"))).log(), Customer.class)
                                .switchIfEmpty(ServerResponse.notFound().build()),
                        operationsConsumer -> operationsConsumer.tag(tagValue)
                                .operationId("Customers by id")
                                .parameter(parameterBuilder().in(ParameterIn.PATH).name("id").implementation(Long.class))
                                .response(responseBuilder().responseCode(HttpStatus.OK.getReasonPhrase())
                                        .implementationArray(Customer.class))).build())

                .and(SpringdocRouteBuilder.route().GET(api + "/{id}",
                        accept(APPLICATION_JSON), serverRequest -> ServerResponse
                                .ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(repository.findById(Long.valueOf(serverRequest.pathVariable("id")))
                                        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found")))
                                        .log(), Customer.class),
                        operationsConsumer -> operationsConsumer.tag(tagValue)
                                .operationId("Customers by id")
                                .parameter(parameterBuilder().in(ParameterIn.PATH).name("id").implementation(Long.class))
                                .response(responseBuilder().responseCode(HttpStatus.OK.getReasonPhrase())
                                        .implementationArray(Customer.class))).build());

    }
}
