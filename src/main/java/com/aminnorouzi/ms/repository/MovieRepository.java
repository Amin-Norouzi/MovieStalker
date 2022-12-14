package com.aminnorouzi.ms.repository;

import com.aminnorouzi.ms.model.movie.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    Optional<Movie> findByTmdbId(Long tmdbId);

    @Query(nativeQuery = true, value = "SELECT genre FROM moviestalkerdb.movie_genres WHERE user_id = ?1 \n"
            + "GROUP BY genre ORDER BY COUNT(*) DESC LIMIT 1;")
    String findMostWatchedGenreByUser(Long userId);

    @Query(nativeQuery = true, value = "SELECT DISTINCT * FROM movie WHERE user_id = ?1 AND watched_at \n"
            + "IS NOT NULL ORDER BY watched_at DESC LIMIT ?2")
    List<Movie> findAllWatchedMoviesByUser(Long userId, int limit);

    @Query(nativeQuery = true, value = "SELECT DISTINCT * FROM movie WHERE user_id = ?1 AND created_at \n"
            + "IS NOT NULL ORDER BY created_at DESC LIMIT 1")
    Movie findLatestAddedMovieByUser(Long userId);

    @Query(nativeQuery = true, value = "SELECT COUNT(*) FROM movie WHERE user_id = ?1 AND watched_at \n"
            + "IS NOT NULL")
    int countWatchedMoviesByUser(Long userId);

    @Query(nativeQuery = true, value = "SELECT COUNT(*) FROM movie WHERE user_id = ?1")
    int countTotalMoviesByUser(Long userId);
}