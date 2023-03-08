package com.aminnorouzi.ms.model.movie;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Search {

    @JsonProperty("id")
    private Long tmdbId;

    @JsonProperty("media_type")
    private String mediaType;

    @JsonAlias({"name", "title"})
    private String title;

    @ToString.Exclude
    @Column(length = 2055)
    @JsonProperty("overview")
    private String overview;

    @JsonProperty("poster_path")
    private String poster;

    @JsonProperty("backdrop_path")
    private String backdrop;

    @JsonAlias({"release_date", "first_air_date"})
    private LocalDate released;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SearchResponse {

        @JsonProperty("results")
        private List<Search> results;

        @JsonProperty("movie_results")
        private List<Search> movies;

        @JsonProperty("tv_results")
        private List<Search> tvs;
    }
}
