package com.aminnorouzi.ms.model.user;

import com.aminnorouzi.ms.model.movie.Movie;
import com.aminnorouzi.ms.util.StatsUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Stats {

    private Integer total;
    private Integer watched;
    private String genre;
    private Movie latest;
    private List<Movie> watchedList;
    private List<Movie> addedList;

    public static Stats of(List<Movie> movies) {
        Integer total = movies.size();
        Integer watched = StatsUtil.getWatchedCount(movies);
        String genre = StatsUtil.getTopGenres(movies);
        Movie movie = StatsUtil.getLatestMovie(movies);
        List<Movie> watchedList = StatsUtil.getLatestWatchedMovies(movies);
        List<Movie> addedList = StatsUtil.getLatestAddedMovies(movies);

        return new Stats(total, watched, genre, movie, watchedList, addedList);
    }
}
