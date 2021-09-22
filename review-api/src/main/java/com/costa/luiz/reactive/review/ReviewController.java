package com.costa.luiz.reactive.review;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
class ReviewController {

    final ReviewRepository repository;

    @GetMapping(path = "/reviews")
    public Flux<Review> findAll() {
        return repository.findAll().log();
    }

    @GetMapping(path = "/reviews/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Review> findAllStream() {
        return repository.findReviewsBy().log();
    }

    @GetMapping(path = "/reviews/search", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Review> findByName(@RequestParam("name") String name) {
        log.info("Let's try find by name {}", name);
        return Flux.from(repository.findReviewByName(name)).log();
    }
}

