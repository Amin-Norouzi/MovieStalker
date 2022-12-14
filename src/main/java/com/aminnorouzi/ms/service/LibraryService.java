package com.aminnorouzi.ms.service;

import com.aminnorouzi.ms.model.movie.*;
import com.aminnorouzi.ms.model.user.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class LibraryService {

    private final FileService fileService;
    private final MovieService movieService;
    private final UserService userService;

    public User add(User user, Search search) {
        MovieRequest movieRequest = MovieRequest.builder()
                .user(user)
                .search(search)
                .build();

        Movie added = movieService.add(movieRequest);

        return userService.update(added.getUser());
    }

    public List<Search> search(Query query) {
        return movieService.search(query);
    }

    public User watch(Movie movie) {
        movieService.watch(movie);

        return userService.update(movie.getUser());
    }

    public User unwatch(Movie movie) {
        movieService.unwatch(movie);

        return userService.update(movie.getUser());
    }

    public User delete(Movie movie) {
        User user = movie.getUser();
        movieService.delete(movie);

        return userService.update(user);
    }

    public MovieRecord report(User user) {
        return movieService.report(user);
    }

    public Set<Query> generate(File directory) {
        return fileService.generate(directory);
    }
}
