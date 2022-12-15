package com.aminnorouzi.ms.service;

import com.aminnorouzi.ms.client.MovieClient;
import com.aminnorouzi.ms.exception.DuplicatedMovieException;
import com.aminnorouzi.ms.exception.MovieNotFoundException;
import com.aminnorouzi.ms.model.movie.*;
import com.aminnorouzi.ms.model.user.User;
import com.aminnorouzi.ms.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieClient movieClient;
    private final MovieRepository movieRepository;

    @Value("${movie.service.type.movie}")
    private String movieType;

    @Value("${movie.service.type.series}")
    private String seriesType;

    public Movie add(Request request) {
        Movie movie = search(request.getQuery());
        movie.setUser(request.getUser());

        Movie added = movieRepository.save(movie);

        log.info("Added a new movie: {}", added);
        return movie;
    }

    private void verify(Long tmdbId) {
        movieRepository.findByTmdbId(tmdbId).ifPresent(m -> {
            throw new DuplicatedMovieException("This movie already exists!");
        });
    }

    public void watch(Movie movie) {
        movie.setWatchedAt(LocalDateTime.now());
    }

    public void unwatch(Movie movie) {
        movie.setWatchedAt(null);
    }

    public void delete(Movie movie) {
        User user = movie.getUser();
        user.getMovies().remove(movie);

        log.info("Deleted a movie: {}", movie);
    }

    private Movie search(Query query) {
        Optional<Search> result = movieClient.getSearch(query.getTitle())
                .getResults()
                .stream().findFirst()
                .or(() -> movieClient.getSearch(query.getFullQuery())
                        .getResults()
                        .stream().findFirst());

        Search search = result
                .orElseThrow(() -> new MovieNotFoundException("Movie does not exist"));

        verify(search.getTmdbId());

        Movie movie;
        if (search.getMediaType().equals(movieType)) {
            movie = movieClient.getMovie(search.getTmdbId(), movieType);
            movie.setType(Type.MOVIE);
        } else {
            movie = movieClient.getMovie(search.getTmdbId(), seriesType);
            movie.setType(Type.SERIES);
        }

        return movie;
    }
}
