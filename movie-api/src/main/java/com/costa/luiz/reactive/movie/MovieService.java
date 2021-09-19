package com.costa.luiz.reactive.movie;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository repository;

    public Flux<Movie> findAll() {
        return repository.findAll();
    }

    public Mono<Movie> findById(String id) {
        return repository.findById(id);
    }

    public Flux<Movie> findMovieByName(String name) {
        return repository.findMovieByNameContaining(name);
    }

    public Flux<Movie> findAllStream() {
        return repository.findMoviesBy();
    }
}
