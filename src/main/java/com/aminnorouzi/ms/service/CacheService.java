package com.aminnorouzi.ms.service;


import com.aminnorouzi.ms.model.movie.Cache;
import com.aminnorouzi.ms.model.movie.Movie;
import com.aminnorouzi.ms.model.movie.Query;
import com.aminnorouzi.ms.model.movie.Search.SearchResponse;
import com.aminnorouzi.ms.repository.CacheRepository;
import com.aminnorouzi.ms.util.JsonUtil;
import com.aminnorouzi.ms.util.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.util.Optional;
import java.util.function.Supplier;

@Slf4j
@Service
@RequiredArgsConstructor
public class CacheService {

    private final CacheRepository cacheRepository;
    private final JsonUtil jsonUtil;
    private final StringUtil stringUtil;
    private final Clock clock;

    @Value("${movie.client.cache.get-search}")
    private String search;

    @Value("${movie.client.cache.get-find}")
    private String find;

    @Value("${movie.client.cache.get-movie}")
    private String get;

    @Value("${movie.client.cache.get-trending}")
    private String trending;

    public Cache save(Cache request) {
        Cache cache = Cache.builder()
                .url(request.getUrl())
                .json(request.getJson())
                .expiresAt(LocalDate.now(clock).plusWeeks(1))
                .build();

        Cache saved = cacheRepository.save(cache);

        log.info("Saved new cache: {}", saved);
        return saved;
    }

    public Optional<?> find(String url, Class<?> clazz) {
        Optional<Cache> found = cacheRepository.findByUrl(url);
        if (found.isEmpty()) {
            return found;
        }

        Cache cache = found.get();
        if (!verify(cache)) {
            delete(cache);
            return Optional.empty();
        }

        Optional<?> result = Optional.of(jsonUtil.fromJson(cache.getJson(), clazz));

        log.info("Found a cache: cache={}, result={}", cache, result);
        return result;
    }

    public Movie get(Long tmdbId, String type, Supplier<Movie> supplier) {
        String url = generate(tmdbId, type);

        Optional<?> result = find(url, Movie.class);
        if (result.isEmpty()) {
            Movie movie = supplier.get();
            String json = jsonUtil.toJson(movie);

            Cache cache = Cache.of(url, json);
            save(cache);

            return movie;
        }

        return (Movie) result.get();
    }

    public SearchResponse get(Query query, Supplier<SearchResponse> supplier) {
        String url = generate(query);

        Optional<?> result = find(url, SearchResponse.class);
        if (result.isEmpty()) {
            try {
                SearchResponse response = supplier.get();
                String json = jsonUtil.toJson(response);

                Cache cache = Cache.of(url, json);
                save(cache);

                return response;
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return null;
            }

        }

        return (SearchResponse) result.get();
    }

    public boolean verify(Cache request) {
        return LocalDate.now(clock).isBefore(request.getExpiresAt());
    }

    public void delete(Cache cache) {
        cacheRepository.delete(cache);

        log.info("Deleted a cache: {}", cache);
    }

    private String generate(Long tmdbId, String type) {
        return stringUtil.format(get, tmdbId, type);
    }

    private String generate(Query query) {
        if (query.getTrending()) {
            return stringUtil.format(trending);
        }
        if (query.getImdb() != null) {
            return stringUtil.format(find, query.getImdb());
        }

        return stringUtil.format(search, query.getTitle());
    }
}
