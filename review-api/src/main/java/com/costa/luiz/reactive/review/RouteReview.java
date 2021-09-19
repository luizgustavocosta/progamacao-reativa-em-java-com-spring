package com.costa.luiz.reactive.review;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Component
@RequiredArgsConstructor
public class RouteReview {

    private final ReviewRepository repository;

    @Bean
    RouterFunction<ServerResponse> routesGateway(ReviewRepository repository) {
        return route()
                .GET("/reviews", serverRequest -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(repository.findAll().log(), Review.class))
                .GET("/reviews/stream", serverRequest -> ServerResponse
                        .ok()
                        .contentType(MediaType.TEXT_EVENT_STREAM)
                        .body(repository.findAll().log(), Review.class))
                .GET("/reviews/live-stream", serverRequest -> ServerResponse
                        .ok()
                        .contentType(MediaType.TEXT_EVENT_STREAM)
                        .body(repository.findReviewsBy().log(), Review.class))
                .build();
    }
}
