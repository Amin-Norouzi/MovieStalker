package com.aminnorouzi.ms.service;

import com.aminnorouzi.ms.model.movie.*;
import com.aminnorouzi.ms.model.user.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Comparator;
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

    public User add(Movie request) {
        Movie added = movieService.add(request);

        return userService.update(added.getUser());
    }

    public Movie find(Search search) {
        return movieService.find(search);
    }

    public List<Search> search(Query query) {
        return movieService.search(query);
    }

    public User watch(Movie movie) {
        if (movie.getWatchedAt() != null) {
            return movie.getUser();
        }

        movieService.watch(movie);
        return userService.update(movie.getUser());
    }

    public User unwatch(Movie movie) {
        if (movie.getWatchedAt() == null) {
            return movie.getUser();
        }

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

    public List<Movie> sort(List<Movie> movies) {
        return movies.stream()
                .sorted(Comparator.comparing(Movie::getCreatedAt)
                        .reversed()).toList();
    }
}
