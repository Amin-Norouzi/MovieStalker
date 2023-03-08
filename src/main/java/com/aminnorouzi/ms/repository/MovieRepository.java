package com.aminnorouzi.ms.repository;

import com.aminnorouzi.ms.model.movie.Movie;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    @Query("select m from Movie m where m.tmdbId = ?1 and m.user.id = ?2")
    Optional<Movie> findByTmdbIdAndUserId(Long tmdbId, Long userId);

    @Query(nativeQuery = true, value = "SELECT DISTINCT genre FROM moviestalkerdb.movie_genres WHERE user_id = ?1 \n"
            + "GROUP BY genre ORDER BY COUNT(*) DESC LIMIT ?2")
    List<String> findMostWatchedGenresByUser(Long userId, int limit);

    @Query(nativeQuery = true, value = "SELECT DISTINCT * FROM movie WHERE user_id = ?1 AND watched_at \n"
            + "IS NOT NULL ORDER BY watched_at DESC LIMIT ?2")
    List<Movie> findAllWatchedMoviesByUser(Long userId, int limit);

    @Query(nativeQuery = true, value = "SELECT COUNT(*) FROM movie WHERE user_id = ?1 AND watched_at \n"
            + "IS NOT NULL")
    int countWatchedMoviesByUser(Long userId);

    @Query(nativeQuery = true, value = "SELECT COUNT(*) FROM movie WHERE user_id = ?1")
    int countTotalMoviesByUser(Long userId);

    @Override
    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "delete from Movie m where m.id = ?1")
    void deleteById(@NotNull Long id);
}
