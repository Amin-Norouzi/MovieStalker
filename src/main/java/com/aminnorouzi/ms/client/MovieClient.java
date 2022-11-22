package com.aminnorouzi.ms.client;

import com.aminnorouzi.ms.model.movie.Movie;
import com.aminnorouzi.ms.model.movie.Search.SearchResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "movieclient", url = "${movie.client.api-url}")
public interface MovieClient {

    @GetMapping("${movie.client.api.get-search}")
    SearchResponse getSearch(@PathVariable String query);

    @GetMapping("${movie.client.api.get-movie}")
    Movie getMovie(@PathVariable Long tmdbId, @PathVariable String type);
}
