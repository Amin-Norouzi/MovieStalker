package com.aminnorouzi.ms.service;

import com.aminnorouzi.ms.client.MovieClient;
import com.aminnorouzi.ms.exception.MovieNotFoundException;
import com.aminnorouzi.ms.model.input.Result;
import com.aminnorouzi.ms.model.movie.*;
import com.aminnorouzi.ms.model.user.User;
import com.aminnorouzi.ms.repository.MovieRepository;
import com.aminnorouzi.ms.task.AdditionTask;
import javafx.concurrent.Task;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieClient movieClient;
    private final MovieRepository movieRepository;

    private final TaskService taskService;

    @Value("${movie.service.type.movie}")
    private String movieType;

    @Value("${movie.service.type.series}")
    private String seriesType;

    public Search search(Query query) {
        Optional<Search> result = movieClient.getSearch(query.getTitle())
                .getResults()
                .stream().findFirst()
                .or(() -> movieClient.getSearch(query.getFullQuery())
                        .getResults()
                        .stream().findFirst());

        return result
                .orElseThrow(() -> new MovieNotFoundException("Movie does not exist"));
    }

    public Movie getByQuery(Query query) {
        Search search = search(query);

        Movie movie = getBySearch(search);

        log.info("Found new movie: {}", movie);
        return movie;
    }

    // each query runs in a background task
    public Result getByQueries(Set<Query> queries) {
        List<Movie> found = new ArrayList<>();
        List<Query> failed = new ArrayList<>();

        queries.forEach(query -> {
            AdditionTask task = AdditionTask.builder()
                    .movieService(this)
                    .query(query)
                    .found(found)
                    .failed(failed)
                    .build();

            taskService.run(task);
        });

        Result result = Result.builder()
                .found(found)
                .failed(failed)
                .build();

        log.info("Found new movies: {}", result);
        return result;
    }

    public Movie getById(Long id) {
        Movie found = movieRepository.findById(id)
                .orElseThrow(() -> new MovieNotFoundException(String.format("Movie: %s does not exist", id)));

        log.info("Found a movie: {}", found);
        return found;
    }

    public Movie getBySearch(Search search) {
        Long id = search.getTmdbId();
        Movie movie;

        if (search.getMediaType().equals(movieType)) {
            movie = movieClient.getMovie(id, movieType);
            movie.setType(Type.MOVIE);
        } else {
            movie = movieClient.getMovie(id, seriesType);
            movie.setType(Type.SERIES);
        }

        return movie;
    }

    public List<Movie> getAll() {
        List<Movie> found = movieRepository.findAll();

        log.info("Found all movies: {}", found);
        return found;
    }
}
