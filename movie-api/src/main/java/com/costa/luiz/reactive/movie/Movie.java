package com.costa.luiz.reactive.movie;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "movies")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Movie {

    @Id
    private String id;
    private String name;
    private String director;
    private int duration;

    @Override
    public String toString() {
        return "Movie{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", director='" + director + '\'' +
                ", duration=" + duration +
                '}';
    }
}
