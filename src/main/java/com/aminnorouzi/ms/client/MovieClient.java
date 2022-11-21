package com.aminnorouzi.ms.client;

import com.aminnorouzi.ms.model.movie.Movie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

import static com.aminnorouzi.ms.client.MovieClient.MovieClientFallBack;

@FeignClient(value = "movieclient", url = "${movie.client.api-url}", fallback = MovieClientFallBack.class)
public interface MovieClient {

    @GetMapping("${movie.client.api.get-movie}")
    Movie getMovie(@PathVariable Long id);

    @GetMapping("${movie.client.api.get-series}")
    Movie getSeries(@PathVariable Long id);

    @Component
    class MovieClientFallBack implements MovieClient {

        @Override
        public Movie getMovie(Long id) {
            return null;
        }

        @Override
        public Movie getSeries(Long id) {
            return null;
        }
    }
}
