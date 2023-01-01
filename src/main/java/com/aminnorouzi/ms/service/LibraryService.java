package com.aminnorouzi.ms.service;

import com.aminnorouzi.ms.model.movie.Movie;
import com.aminnorouzi.ms.model.movie.Query;
import com.aminnorouzi.ms.model.movie.Request;
import com.aminnorouzi.ms.model.movie.Search;
import com.aminnorouzi.ms.model.user.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class LibraryService {

    private final UserService userService;
    private final MovieService movieService;

    public Movie add(User user, Movie movie) {
        Request request = new Request(user, movie);
        movieService.add(request);

        User updated = userService.update(user);
        log.info("Added a new movie: movie={}, user={}", movie, updated);

        return movie;
    }

    public Movie find(Search search) {
        return movieService.find(search);
    }

    public List<Search> search(Query query) {
        return movieService.search(query);
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
        User user = movie.getUser();

        movieService.delete(movie);

        User updated = userService.update(user);
        log.info("Deleted a movie: movieId={}, user={}", movie.getId(), updated);
    }
}
