package com.aminnorouzi.ms.service;

import com.aminnorouzi.ms.client.MovieClient;
import com.aminnorouzi.ms.model.input.Result;
import com.aminnorouzi.ms.model.movie.*;
import com.aminnorouzi.ms.repository.MovieRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@AllArgsConstructor
@Service
public class MovieService {

    private final MovieClient movieClient;
    private final MovieRepository movieRepository;

    private static final String MOVIE_TYPE = "movie";
    private static final String SERIES_TYPE = "tv";

    public Result getMoviesFromQueries(Set<Query> queries) {
        List<Movie> found = new ArrayList<>();
        List<Query> failed = new ArrayList<>();

        queries.forEach(query -> {
            Movie movie;

            Search search = getSearch(query);
            if (search != null) {
                if (search.getMediaType().equals(MOVIE_TYPE)) {
                    movie = getMovie(search.getTmdbId(), MOVIE_TYPE);
                } else {
                    movie = getMovie(search.getTmdbId(), SERIES_TYPE);
                }

                found.add(movie);
            } else {
                failed.add(query);
            }
        });

        Result result = Result.builder()
                .found(found)
                .failed(failed)
                .build();

        log.info("Found new movies: {}", result);
        return result;
    }

    private Movie getMovie(Long tmdbId, String type) {
        return movieClient.getMovie(tmdbId, type);
    }

    private Search getSearch(Query query) {
        try {
            Optional<Search> search = getSearch(query.getTitle());
            if (!search.isPresent()) {
                search = getSearch(query.getTitle() + query.getRelease());
            }

            return search.orElseThrow(RuntimeException::new);
        } catch (RuntimeException exception) {
            return null;
        }
    }

    private Optional<Search> getSearch(String query) {
        return movieClient.getSearch(query)
                .getResults()
                .stream().findFirst();
    }
}
