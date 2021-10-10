package com.costa.luiz.reactive.customer.h2;

import com.costa.luiz.reactive.customer.Customer;
import com.costa.luiz.reactive.customer.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final CustomerRepository repository;

    @EventListener(ApplicationReadyEvent.class)
    public void ready() {

        var lastName = "Stahelski";
        var middleName = "Chad";
        List<Customer> listOfMovies = List.of(
                new Customer(null, "John", middleName, lastName, LocalDate.of(2020, Month.JANUARY, 2)),
                new Customer(null, "Juan", middleName, lastName, LocalDate.of(2020, Month.MARCH, 20)),
                new Customer(null, "Joan", middleName, lastName, LocalDate.of(2020, Month.APRIL, 19)),
                new Customer(null, "Joao", middleName, lastName, LocalDate.of(2021, Month.JULY, 10)));

        Flux<Customer> movies = Flux.fromIterable(listOfMovies).flatMap(repository::save);
        movies.subscribe(movie -> log.info("movie ->" + movie)); // For in-memory database
    }
}
