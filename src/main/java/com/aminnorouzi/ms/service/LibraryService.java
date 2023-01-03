package com.aminnorouzi.ms.service;

import com.aminnorouzi.ms.model.movie.*;
import com.aminnorouzi.ms.model.user.User;
import com.aminnorouzi.ms.model.user.UserRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@AllArgsConstructor
public class LibraryService {

    /*
        TODO: separate the functionality of user and movie into two separate services.
     */

    private final FileService fileService;
    private final UserService userService;
    private final MovieService movieService;

    public User signin(UserRequest request) {
        return userService.signin(request);
    }

    public User signup(UserRequest request) {
        return userService.signup(request);
    }

    public Movie add(User user, Movie movie) {
        MovieRequest movieRequest = new MovieRequest(user, movie);
        movieService.add(movieRequest);

        User updated = userService.update(user);
        log.info("Added a new movie: movie={}, user={}", movie, updated);

        return movie;
    }

    public List<File> read(File directory) {
        return fileService.read(directory);
    }

    public Set<Query> convert(List<File> files) {
        return fileService.convert(files);
    }

    public Movie find(Search search) {
        return movieService.find(search);
    }

    public List<Search> search(Query query) {
        return movieService.search(query);
    }

    public MovieRecord records(User user) {
        return movieService.records(user);
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
