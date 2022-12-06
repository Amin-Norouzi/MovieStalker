package com.aminnorouzi.ms.service;

import com.aminnorouzi.ms.model.movie.Movie;
import com.aminnorouzi.ms.model.movie.Query;
import com.aminnorouzi.ms.model.user.UpdateRequest;
import com.aminnorouzi.ms.model.user.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Slf4j
@Service
@AllArgsConstructor
public class LibraryService {

    private final UserService userService;
    private final MovieService movieService;

    public User watch(Long userId, Long movieId) {
        User user = userService.getById(userId);
        Movie movie = movieService.getById(movieId);

        user.getMovies().remove(movie);
        movie.setWatchedAt(LocalDateTime.now());
        user.getMovies().add(movie);

        UpdateRequest request = userService.build(user.getId(), user.getMovies());
        User updated = userService.update(request);

        log.info("Watched a new movie by user: movie={}, {}", movie, updated);
        return updated;
    }

    public User add(Long userId, Query query) {
        User user = userService.getById(userId);
        Movie movie = movieService.getByQuery(query);

        user.getMovies().add(movie);

        UpdateRequest request = userService.build(user.getId(), user.getMovies());
        User updated = userService.update(request);

        log.info("Added a new movie to user: movie={}, {}", movie, updated);
        return updated;
    }

    public void add(Long userId, Set<Query> queries) {
        User user = userService.getById(userId);
    }

    public User delete(Long userId, Long movieId) {
        User user = userService.getById(userId);
        Movie movie = movieService.getById(movieId);

        user.getMovies().remove(movie);

        UpdateRequest request = userService.build(user.getId(), user.getMovies());
        User updated = userService.update(request);

        log.info("Removed a movie from user: movie={}, {}", movie, updated);
        return updated;
    }

}
