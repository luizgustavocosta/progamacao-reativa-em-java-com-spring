package com.costa.luiz.netflix;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MoviesByGenreRepository extends ReactiveCrudRepository<MoviesByGenre, String> {
}
