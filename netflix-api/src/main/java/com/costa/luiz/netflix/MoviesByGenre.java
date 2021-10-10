package com.costa.luiz.netflix;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class MoviesByGenre {

    private String title;
    private int duration;
    private String genre;
    private String synopsis;
    private String thumbnail;
    private int year;
}
