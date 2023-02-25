package com.aminnorouzi.ms.service;

import com.aminnorouzi.ms.client.MovieClient;
import com.aminnorouzi.ms.exception.DuplicatedMovieException;
import com.aminnorouzi.ms.exception.MovieNotFoundException;
import com.aminnorouzi.ms.model.movie.*;
import com.aminnorouzi.ms.model.movie.Search.SearchResponse;
import com.aminnorouzi.ms.model.user.User;
import com.aminnorouzi.ms.repository.MovieRepository;
import com.aminnorouzi.ms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovieService {
    private final UserRepository userRepository;

    private final MovieClient movieClient;
    private final MovieRepository movieRepository;
    private final Clock clock;

    public Movie add(MovieRequest request) {
        Movie movie = find(request.getSearch());
        verify(movie.getTmdbId());

        User user = request.getUser();
        user.addMovie(movie);

        Movie added = movieRepository.save(movie);

        log.info("Added a new movie: {}", added);
        return added;
    }

    public Movie add(Movie request) {
        verify(request.getTmdbId());

        User user = request.getUser();
        user.addMovie(request);

        Movie added = movieRepository.save(request);

        log.info("Added a new movie: {}", added);
        return added;
    }

    private void verify(Long tmdbId) {
        movieRepository.findByTmdbId(tmdbId).ifPresent(m -> {
            throw new DuplicatedMovieException("This movie already exists!");
        });
    }

    public Movie find(Search search) {
        String type = search.getMediaType();

        Movie found = movieRepository.findByTmdbId(search.getTmdbId()).orElseGet(
                () -> movieClient.get(search.getTmdbId(), type));

        found.setType(Type.of(type));

        log.info("Found a new movie: {}", found);
        return found;
    }

    public MovieRecord report(User user) {
        Long userId = user.getId();
        int playlistLimit = 10;
        int genresLimit = 6;

        MovieRecord data = MovieRecord.builder()
                .total(movieRepository.countTotalMoviesByUser(userId))
                .watched(movieRepository.countWatchedMoviesByUser(userId))
                .genres(movieRepository.findMostWatchedGenresByUser(userId, genresLimit))
                .playlist(movieRepository.findAllWatchedMoviesByUser(userId, playlistLimit))
                .trending(track(LocalDate.now(clock)))
                .build();

        Boolean isAvailable = data.getPlaylist().size() == playlistLimit;
        data.setIsAvailable(isAvailable);

        log.info("Reported a movie record: {}", data);
        return data;
    }

    private List<Movie> track(LocalDate date) {
        return movieClient.trending().getResults().stream()
                .parallel()
                .filter(s -> s.getMediaType().equals("movie") ||
                        s.getMediaType().equals("tv"))
                .limit(10)
                .map(this::find)
                .toList();
    }

    public void watch(Movie request) {
        request.setWatchedAt(LocalDateTime.now());

        log.info("Watched a movie: {}", request);
    }

    public void unwatch(Movie request) {
        request.setWatchedAt(null);

        log.info("Unwatched a movie: {}", request);
    }

    public void delete(Movie movie) {
        User user = movie.getUser();
        user.removeMovie(movie);

//        movieRepository.delete(movie);

        log.info("Deleted a movie: {}", movie);
    }

    public List<Search> search(Query query) {
        List<Search> found;
        if (query.getImdb() != null) {
            String imdbId = extract(query.getImdb());
            SearchResponse response = movieClient.find(imdbId);
            if (!response.getMovies().isEmpty()) {
                found = response.getMovies();
            } else {
                found = response.getTvs();
            }
        } else {
            found = movieClient.search(query.getTitle()).getResults();
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
        Pattern pattern = Pattern.compile("(tt\\d[0-9]*)");
        Matcher matcher = pattern.matcher(url);

        if (matcher.find()) {
            return matcher.group();
        }

        throw new RuntimeException("Failed to extract imdb id!");
    }
}
