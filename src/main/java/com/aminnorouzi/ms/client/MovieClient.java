package com.aminnorouzi.ms.client;

import com.aminnorouzi.ms.model.movie.Movie;
import com.aminnorouzi.ms.model.movie.Search.SearchResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "movieclient", url = "${movie.client.api-url}")
public interface MovieClient {

    @GetMapping("${movie.client.api.get-search}")
    SearchResponse search(@PathVariable("query") String query);

    @GetMapping("${movie.client.api.get-find}")
    SearchResponse find(@PathVariable("imdbId") String imdbId);

    @GetMapping("${movie.client.api.get-movie}")
    Movie get(@PathVariable("tmdbId") Long tmdbId, @PathVariable("type") String type);

    @GetMapping("${movie.client.api.get-trending}")
    SearchResponse trending();
}
