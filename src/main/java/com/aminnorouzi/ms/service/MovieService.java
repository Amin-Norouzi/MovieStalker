package com.aminnorouzi.ms.service;

import com.aminnorouzi.ms.client.MovieClient;
import com.aminnorouzi.ms.exception.DuplicatedMovieException;
import com.aminnorouzi.ms.exception.MovieNotFoundException;
import com.aminnorouzi.ms.model.movie.*;
import com.aminnorouzi.ms.model.user.User;
import com.aminnorouzi.ms.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieClient movieClient;
    private final MovieRepository movieRepository;

    private final Map<String, String> types = Map.ofEntries(
            entry("movie", "movie"),
            entry("tv", "tv"));

    public Movie add(Request request) {
        Movie movie = request.getMovie();
        verify(movie.getTmdbId());

        User user = request.getUser();
        user.addMovie(movie);

        Movie added = movieRepository.save(movie);

        log.info("Added a new movie: {}", added);
        return movie;
    }

    private void verify(Long tmdbId) {
        movieRepository.findByTmdbId(tmdbId).ifPresent(m -> {
            throw new DuplicatedMovieException("This movie already exists!");
        });
    }

    public Movie find(Search search) {
        String type = types.get(search.getMediaType());

        Movie found = movieClient.get(search.getTmdbId(), type);
        found.setType(Type.of(type));

        log.info("Found a new movie: {}", found);
        return found;
    }

    public void watch(Movie movie) {
        movie.setWatchedAt(LocalDateTime.now());
    }

    public void unwatch(Movie movie) {
        movie.setWatchedAt(null);
    }

    public void delete(Movie movie) {
        User user = movie.getUser();
        user.removeMovie(movie);

        movieRepository.delete(movie);

        log.info("Deleted a movie: {}", movie);
    }

    public List<Search> search(Query query) {
        List<Search> result = movieClient.search(query.getTitle())
                .getResults().stream()
                .filter(s -> s.getPoster() != null)
                .filter(s -> s.getOverview() != null)
                .filter(s -> s.getReleased() != null)
                .toList();

        if (result.isEmpty()) {
            throw new MovieNotFoundException("Movie does not exist");
        }

        log.info("Searched a query: query={}, result={}", query, result);
        return result;
    }
}
