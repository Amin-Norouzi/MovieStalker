package com.aminnorouzi.ms.service;

import com.aminnorouzi.ms.client.MovieClient;
import com.aminnorouzi.ms.exception.DuplicatedMovieException;
import com.aminnorouzi.ms.exception.MovieNotFoundException;
import com.aminnorouzi.ms.model.movie.*;
import com.aminnorouzi.ms.model.movie.Search.SearchResponse;
import com.aminnorouzi.ms.model.user.User;
import com.aminnorouzi.ms.repository.MovieRepository;
import com.aminnorouzi.ms.repository.UserRepository;
import com.aminnorouzi.ms.util.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovieService {
    private final MovieClient movieClient;
    private final MovieRepository movieRepository;
    private final StringUtil stringUtil;
    private final Clock clock;
    private final CacheService cacheService;
    private final UserService userService;

    public void add(User user, Movie request) {
        verify(user, request.getTmdbId());

        request.setUser(user);
        user.getMovies().add(request);

        request.setCreatedAt(LocalDateTime.now(clock));

//        Movie added = movieRepository.save(request);
//
//        log.info("Added a new movie: {}", added);
//        return added;
    }

    private void verify(User user, Long tmdbId) {
        movieRepository.findByTmdbIdAndUserId(tmdbId, user.getId()).ifPresent(m -> {
            throw new DuplicatedMovieException("This movie already exists!");
        });
    }

    public Movie find(User user, Search search) {
        Long tmdbId = search.getTmdbId();
        String type = search.getMediaType();

        Movie found = movieRepository.findByTmdbIdAndUserId(tmdbId, user.getId())
                .orElse(cacheService.get(
                        tmdbId, type, () -> movieClient.get(tmdbId, type)
                ));

        if (found.getType() == null) found.setType(Type.of(type));
        if (found.getWebsite() == null) found.setWebsite(stringUtil.generateImdbUrl(found.getImdbId()));

        log.info("Found a new movie: {}", found);
        return found;
    }

    public MovieRecord report(User user) {
        Long userId = user.getId();
        int moviesLimit = 5;
        int genresLimit = 6;

        List<Movie> added = sort(user.getMovies()).stream()
                .limit(moviesLimit)
                .toList();

        List<Movie> trending = track(user);

        MovieRecord data = MovieRecord.builder()
                .total(movieRepository.countTotalMoviesByUser(userId))
                .watched(movieRepository.countWatchedMoviesByUser(userId))
                .genres(movieRepository.findMostWatchedGenresByUser(userId, genresLimit))
                .playlist(movieRepository.findAllWatchedMoviesByUser(userId, moviesLimit))
                .added(added)
                .trending(trending.subList(0, 5))
                .slider(trending.subList(5, 10))
                .build();

        Boolean isAvailable = data.getPlaylist().size() == moviesLimit
                && data.getAdded().size() == moviesLimit
                && data.getGenres().size() == genresLimit;

        data.setIsAvailable(isAvailable);

        log.info("Reported a movie record: {}", data);
        return data;
    }

    private List<Movie> track(User user) {
        Query query = Query.of(true);

        return cacheService.get(query, () -> movieClient.trending())
                .getResults().stream()
                .parallel()
                .filter(s -> s.getMediaType().equals("movie") ||
                        s.getMediaType().equals("tv"))
                .limit(10)
                .map(s -> find(user, s))
                .toList();
    }

    public void watch(Movie request) {
        request.setWatchedAt(LocalDateTime.now(clock));

        log.info("Watched a movie: {}", request);
    }

    public void unwatch(Movie request) {
        request.setWatchedAt(null);

        log.info("Unwatched a movie: {}", request);
    }

    public void delete(Movie request) {
        User user = request.getUser();

        request.setUser(null);
        user.getMovies().remove(request);

        log.info("Deleted a movie: {}", request);
    }

    public void delete(Long id) {
        if (movieRepository.existsById(id)) {
            movieRepository.deleteById(id);
        }
    }

    public List<Search> search(Query query) {
        List<Search> found;
        if (query.getImdb() != null) {
            String imdbId = extract(query.getImdb());
            query.setImdb(imdbId);

            SearchResponse response = cacheService.get(query, () -> movieClient.find(imdbId));
            if (!response.getMovies().isEmpty()) {
                found = response.getMovies();
            } else {
                found = response.getTvs();
            }
        } else {
            found = cacheService.get(query, () -> movieClient.search(query.getTitle())).getResults();
        }

        List<Search> result = found.stream()
                .filter(s -> s.getPoster() != null &&
                        s.getBackdrop() != null &&
                        s.getOverview() != null &&
                        s.getReleased() != null)
                .toList();

        if (result.isEmpty()) {
            throw new MovieNotFoundException("Movie does not exist");
        }

        log.info("Searched a query: query={}, result={}", query, result);
        return result;
    }

    private String extract(String url) {
        String id = stringUtil.extractImdbId(url);
        if (id != null) {
            return id;
        }

        throw new RuntimeException("Failed to extract imdb id!");
    }

    public List<Movie> sort(List<Movie> movies) {
        return movies.stream()
                .sorted(Comparator.comparing(Movie::getCreatedAt).reversed())
                .toList();
    }
}
