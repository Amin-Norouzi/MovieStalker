package com.aminnorouzi.ms.service;

import com.aminnorouzi.ms.client.MovieClient;
import com.aminnorouzi.ms.model.movie.Movie;
import com.aminnorouzi.ms.model.movie.Type;
import com.aminnorouzi.ms.repository.MovieRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class MovieService {

    private final MovieClient movieClient;
    private final MovieRepository movieRepository;

    public void doSomething() {
        try {
            Movie movie = movieClient.getMovie(437102L);
            movie.setType(Type.MOVIE);
//            Movie movie = movieClient.getSeries(1402L);
//            movie.setType(Type.SERIES);
            movie.setPoster("https://image.tmdb.org/t/p/original" + movie.getPoster());
            movie.setBackdrop("https://image.tmdb.org/t/p/original" + movie.getBackdrop());

            Movie saved = movieRepository.save(movie);
            System.out.println(saved);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
