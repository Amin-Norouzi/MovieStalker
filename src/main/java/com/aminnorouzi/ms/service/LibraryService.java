package com.aminnorouzi.ms.service;

import com.aminnorouzi.ms.model.movie.*;
import com.aminnorouzi.ms.model.user.User;
import com.aminnorouzi.ms.model.user.UserRequest;
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

    public Movie add(User user, Search search) {
        MovieRequest movieRequest = MovieRequest.builder()
                .user(user)
                .search(search)
                .build();

        return movieService.add(movieRequest);
    }

    public List<Search> search(Query query) {
        return movieService.search(query);
    }

    public User watch(Movie movie) {
        Movie updated = movieService.watch(movie);

        return updated.getUser();
    }

    public User unwatch(Movie movie) {
        movieService.unwatch(movie);

        return movie.getUser();
    }

    public User delete(Movie movie) {
        movieService.delete(movie);

        return movie.getUser();
    }

    public MovieRecord report(User user) {
        return movieService.report(user);
    }

    public Set<Query> generate(File directory) {
        return fileService.generate(directory);
    }
}
