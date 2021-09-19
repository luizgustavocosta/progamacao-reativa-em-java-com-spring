package com.costa.luiz.reactive.review;

import org.springframework.data.mongodb.repository.Tailable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ReviewRepository extends ReactiveCrudRepository<Review, String> {

    Flux<Review> findReviewByName(String name);

    @Tailable
    Flux<Review> findReviewsBy();

}