package com.costa.luiz.reactive.movie;

import org.springframework.data.mongodb.repository.Tailable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
interface MovieRepository extends ReactiveCrudRepository<Movie, String> {

    Flux<Movie> findMovieByNameContaining(String name);
    @Tailable
    Flux<Movie> findMoviesBy();

}
