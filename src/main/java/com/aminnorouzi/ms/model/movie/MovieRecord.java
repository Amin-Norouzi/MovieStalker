package com.aminnorouzi.ms.model.movie;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieRecord {

    private Integer total;
    private Integer watched;
    private List<String> genres;
    private List<Movie> playlist;
    private List<Movie> added;
    private List<Movie> trending;
    private List<Movie> slider;
    private Boolean isAvailable;
}