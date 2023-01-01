package com.aminnorouzi.ms.util;

import com.aminnorouzi.ms.model.movie.Movie;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class StatsUtil {

    public static Integer getWatchedCount(List<Movie> movies) {
        return Math.toIntExact(movies.stream()
                .filter(m -> m.getWatchedAt() != null)
                .count());
    }

    public static String getTopGenres(List<Movie> movies) {
        List<String> genres = new ArrayList<>();
        movies.forEach(movie -> genres.addAll(movie.getGenres()));

        return StringUtil.getMostRepeated(genres);
    }

    public static Movie getLatestMovie(List<Movie> movies) {
        return movies.get(movies.size() - 1);
    }

    public static List<Movie> getLatestWatchedMovies(List<Movie> movies) {
        return movies.stream()
                .filter(m -> m.getWatchedAt() != null)
                .sorted(Comparator.comparing(Movie::getWatchedAt).reversed())
                .limit(10)
                .toList();
    }

    public static List<Movie> getLatestAddedMovies(List<Movie> movies) {
        return movies.stream()
                .filter(m -> m.getCreatedAt() != null)
                .sorted(Comparator.comparing(Movie::getCreatedAt).reversed())
                .limit(10)
                .toList();
    }
}
