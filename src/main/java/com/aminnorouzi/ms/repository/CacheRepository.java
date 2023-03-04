package com.aminnorouzi.ms.repository;

import com.aminnorouzi.ms.model.movie.Cache;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CacheRepository extends JpaRepository<Cache, Long> {

    Optional<Cache> findByUrl(String url);
}
