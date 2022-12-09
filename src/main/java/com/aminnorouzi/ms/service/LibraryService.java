package com.aminnorouzi.ms.service;

import com.aminnorouzi.ms.model.movie.Movie;
import com.aminnorouzi.ms.model.movie.Query;
import com.aminnorouzi.ms.model.movie.Request;
import com.aminnorouzi.ms.model.user.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class LibraryService {

    private final UserService userService;
    private final MovieService movieService;

    public Movie add(User user, Query query) {
        Request request = new Request(user, query);
        Movie movie = movieService.add(request);

        user.getMovies().add(movie);

        User updated = userService.update(user);
        log.info("Added a new movie: movie={}, user={}", movie, updated);

        return movie;
    }

    public void watch(Movie movie) {
        movieService.watch(movie);

        User updated = userService.update(movie.getUser());
        log.info("Watched a movie: movieId={}, user={}", movie.getId(), updated);
    }

    public void unwatch(Movie movie) {
        movieService.unwatch(movie);

        User updated = userService.update(movie.getUser());
        log.info("Unwatched a movie: movieId={}, user={}", movie.getId(), updated);
    }

    public void delete(Movie movie) {
        movieService.delete(movie);

        User updated = userService.update(movie.getUser());
        log.info("Deleted a movie: movieId={}, user={}", movie.getId(), updated);
    }
}
