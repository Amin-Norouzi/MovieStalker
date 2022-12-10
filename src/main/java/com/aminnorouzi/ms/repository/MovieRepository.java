package com.aminnorouzi.ms.repository;

import com.aminnorouzi.ms.model.movie.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    Optional<Movie> findByTmdbId(Long tmdbId);
}