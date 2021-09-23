package com.costa.luiz.reactive.review;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataMongoTest
@ActiveProfiles("test")
class ReviewRepositoryTest {

    @Autowired
    ReviewRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    void findReviewByName() {
        repository.save(Review.builder().id("42").name("Universe").build()).block();
        Flux<Review> reviews = repository.findReviewByName("Universe");

        StepVerifier
                .create(reviews)
                .assertNext(review -> {
                    assertEquals("Universe", review.getName());
                    assertEquals("42", review.getId());
                    assertNotNull(review.getId());
                    assertNull(review.getListing_Url());
                    assertNull(review.getSummary());
                })
                .expectComplete()
                .verify();
    }

    @Test
    void findReviewsBy() {
        Review dc = Review.builder().id("42").name("Universe").build();
        Review dcUniverse = Review.builder().id("3").name("DC Universe").build();
        Review marvel = Review.builder().id("1").name("Marvel").build();

        Flux<Review> reviewFlux = repository.saveAll(Flux.just(dc, dcUniverse, marvel));

        StepVerifier
                .create(Flux.from(reviewFlux).thenMany(repository.findAll().log()))
                .expectNextCount(3)
                .verifyComplete();
    }
}