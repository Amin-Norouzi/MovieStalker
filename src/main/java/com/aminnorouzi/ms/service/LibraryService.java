package com.aminnorouzi.ms.service;

import com.aminnorouzi.ms.model.movie.Movie;
import com.aminnorouzi.ms.model.movie.MovieRecord;
import com.aminnorouzi.ms.model.movie.Query;
import com.aminnorouzi.ms.model.movie.Search;
import com.aminnorouzi.ms.model.user.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class LibraryService {

    private final FileService fileService;
    private final MovieService movieService;
    private final UserService userService;

    public User add(User user, Movie request) {
//        Movie added = movieService.add(user, request);
         movieService.add(user, request);

        return userService.update(user);
    }

    public Movie find(User user, Search search) {
        return movieService.find(user, search);
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

        User updated = userService.update(user);

        movieService.delete(movie.getId());

        return updated;
    }

    public MovieRecord report(User user) {
        return movieService.report(user);
    }

    public Set<Query> generate(File directory) {
        return fileService.generate(directory);
    }

    public List<Movie> sort(List<Movie> movies) {
        return movieService.sort(movies);
    }
}
