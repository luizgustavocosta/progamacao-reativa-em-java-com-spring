package com.costa.luiz.reactive.customer;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.RouterOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Component
@RequiredArgsConstructor
public class RouteCustomer {

    private final CustomerRepository repository;

    @Bean
    @RouterOperation(operation = @Operation(operationId = "findAll", summary = "Find all customers", tags = {"Customers"},
            responses = {@ApiResponse(responseCode = "200", description = "successful operation",
                    content = @Content(schema = @Schema(implementation = Customer.class)))})
    )
    RouterFunction<ServerResponse> findAll(CustomerRepository repository) {
        return route()
                .GET("/customers", serverRequest ->
                        ServerResponse
                                .ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(repository.findAll().log(), Customer.class))
                .GET("/customers/stream", serverRequest -> ServerResponse
                        .ok()
                        .contentType(MediaType.TEXT_EVENT_STREAM)
                        .body(repository.findAll().log(), Customer.class))
                .build();
    }

    @RouterOperation(operation = @Operation(operationId = "findCustomerById", summary = "Find customer by ID", tags = {"Customers"},
            parameters = {@Parameter(in = ParameterIn.PATH, name = "id", description = "Customer Id")},
            responses = {@ApiResponse(responseCode = "200", description = "successful operation",
                    content = @Content(schema = @Schema(implementation = Customer.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid Customer ID supplied"),
                    @ApiResponse(responseCode = "404", description = "Customer not found")}))
    @Bean
    RouterFunction<ServerResponse> findById(CustomerRepository repository) {
        return route()
                .GET("/customers/{id}", serverRequest -> ServerResponse
                        .ok()
                        .contentType(MediaType.TEXT_EVENT_STREAM)
                        .body(repository.findById(Long.valueOf(serverRequest.pathVariable("id"))).log(), Customer.class))
                .build();
    }

}
