package com.aminnorouzi.ms.controller;

import com.aminnorouzi.ms.configuration.ApplicationConfiguration;
import com.aminnorouzi.ms.model.movie.Movie;
import com.aminnorouzi.ms.model.movie.Query;
import com.aminnorouzi.ms.service.MovieService;
import com.aminnorouzi.ms.util.ViewSwitcher;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Slf4j
@Component
@FxmlView("/view/home-view.fxml")
public class HomeController extends Controller {

    private final MovieService movieService;

    public HomeController(ApplicationConfiguration configuration, ViewSwitcher switcher, MovieService movieService) {
        super(configuration, switcher, movieService);
        this.movieService = movieService;
    }

    @Override
    protected void configure() {
        Set<Query> queries = Set.of(
                new Query("Begin Again", "2013"),
                new Query("Blood Diamond", "2006"),
                new Query("Cape Fear", "1991"),
                new Query("Dead Poets Society", "1989"),
                new Query("Gangs of New York", "2002"),
                new Query("Mank", "2020"),
                new Query("Margin Call", "2011"),
//                new Query("My Policeman", "2022"),
                new Query("Pulp Fiction", "1994"),
                new Query("Spotlight", "2015"),
                new Query("The Martian", "2015")
        );

        List<Movie> movies = movieService.getByQueries(queries).getFound();
        getUser().setMovies(movies);

        System.out.println("Done getting movies!");
//        Movie movie = movieService.getMovieFromQuery(new Query("The Walking Dead", "2010"));
//        getUser().setMovies(List.of(movie));
    }
}
