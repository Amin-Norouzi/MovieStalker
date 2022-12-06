package com.aminnorouzi.ms.task;

import com.aminnorouzi.ms.exception.MovieNotFoundException;
import com.aminnorouzi.ms.model.movie.Movie;
import com.aminnorouzi.ms.model.movie.Query;
import com.aminnorouzi.ms.model.movie.Search;
import com.aminnorouzi.ms.service.MovieService;
import javafx.concurrent.Task;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

@Builder
public class AdditionTask extends Task<Void> {

    private MovieService movieService;

    private Query query;
    private List<Movie> found;

    @Override
    protected Void call() throws Exception {
        try {
            Search search = movieService.search(query);
            Movie movie = movieService.getBySearch(search);

            found.add(movie);
        } catch (MovieNotFoundException ignored) {}

        return null;
    }
}
