package com.costa.luiz.reactive.movie;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static com.costa.luiz.reactive.movie.MovieMock.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.verify;

@DisplayName("Movie Service")
@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

    @Mock
    MovieRepository repository;

    MovieService service;

    @BeforeEach
    void setUp() {
        service = new MovieService(repository);
    }

    @Test
    void findAll() {
        Mockito.when(repository.findAll()).thenReturn(Flux.just(avengers(), theHulk(), chapolin()));

        Flux<Movie> movies = service.findAll();

        assertNotNull(movies);
        verify(repository, atMost(1)).findAll();
        assertEquals(3, movies.count().block());
    }

    @Test
    void findById() {
        var id = "10";
        Mockito.when(repository.findById(id)).thenReturn(Mono.just(chapolin()));

        Mono<Movie> movie = service.findById(id);

        assertNotNull(movie);
        verify(repository, atMost(1)).findById(id);
        assertTrue(movie.blockOptional().isPresent());
    }

    @Test
    void findMovieByName() {
        var name = "Avengers";
        Mockito.when(repository.findMovieByNameContaining(name)).thenReturn(Flux.just(avengers()));

        Flux<Movie> moviesByName = service.findMovieByName(name);

        assertNotNull(moviesByName);
        verify(repository, atMost(1)).findMovieByNameContaining(name);
        assertEquals(avengers().getId(), Objects.requireNonNull(moviesByName.blockFirst()).getId());
    }

    @Test
    void findAllStream() {
        Mockito.when(repository.findMoviesBy()).thenReturn(Flux.just(avengers(), theHulk(), chapolin()));

        Flux<Movie> movies = service.findAllStream();

        assertNotNull(movies);
        verify(repository, atMost(1)).findMoviesBy();
        assertEquals(3, movies.count().block());
    }
}