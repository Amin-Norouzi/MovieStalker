package com.aminnorouzi.ms.service;

import com.aminnorouzi.ms.client.MovieClient;
import com.aminnorouzi.ms.exception.MovieNotFoundException;
import com.aminnorouzi.ms.model.movie.Movie;
import com.aminnorouzi.ms.model.movie.Search;
import com.aminnorouzi.ms.model.movie.Type;
import com.aminnorouzi.ms.repository.MovieRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class MovieService {

    private final MovieClient movieClient;
    private final MovieRepository movieRepository;

    public void doSomething() {
        try {
            Movie movie = getMovie(1402L, "tv")
                    .orElseThrow(() -> new MovieNotFoundException("Movie not found"));
//            movie.setType(Type.MOVIE);
            movie.setType(Type.SERIES);
            movie.setPoster("https://image.tmdb.org/t/p/original" + movie.getPoster());
            movie.setBackdrop("https://image.tmdb.org/t/p/original" + movie.getBackdrop());

            Movie saved = movieRepository.save(movie);
            System.out.println(saved);
        } catch (Exception e) {
            System.out.println("error");
            System.out.println(e.getMessage());
        }
    }

    private Optional<Movie> getMovie(Long tmdbId, String type) {
        try {
            return Optional.of(movieClient.getMovie(tmdbId, type));
        } catch (RuntimeException exception) {
            return Optional.empty();
        }
    }

    private Optional<Search> getSearch(String query) {
        try {
            return movieClient.getSearch(query)
                    .getResults()
                    .stream().findFirst();
        } catch (RuntimeException exception) {
            return Optional.empty();
        }
    }
}
