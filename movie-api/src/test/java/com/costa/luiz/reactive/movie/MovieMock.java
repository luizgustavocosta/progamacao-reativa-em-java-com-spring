package com.costa.luiz.reactive.movie;

class MovieMock {

    static Movie avengers() {
        return Movie.builder()
                .id("1")
                .name("Avengers")
                .duration(100)
                .build();
    }

    static Movie theHulk() {
        return Movie.builder()
                .id("2")
                .name("The Hulk")
                .duration(200)
                .build();
    }

    static Movie chapolin() {
        return Movie.builder()
                .id("10")
                .name("El Chapolin")
                .duration(120)
                .build();
    }
}
