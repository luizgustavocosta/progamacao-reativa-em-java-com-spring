package com.costa.luiz.reactive.movie;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1")
@Slf4j
@RequiredArgsConstructor
public class MovieController {

    private final MovieService service;

    @GetMapping(path = "/movies")
    public Flux<Movie> findAll() {
        log.info("Find all");
        return service.findAll().log();
    }

    @GetMapping(path = "/movies/{id}")
    public Mono<Movie> findBy(@PathVariable("id") String id) {
        log.info("Searching for the id {}", id);
        return service.findById(id).log().switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    @GetMapping(path = "/movies/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Movie> findAllStream() {
        return service.findAllStream();
    }

    @GetMapping(path = "/movies/search", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Movie> findByName(@RequestParam("name") String name) {
        return Flux.from(service.findMovieByName(name)).log().switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }
}
