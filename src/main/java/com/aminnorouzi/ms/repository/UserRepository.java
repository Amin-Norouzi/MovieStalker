package com.aminnorouzi.ms.repository;

import com.aminnorouzi.ms.model.user.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @NotNull
    @Override
    @Query("select u from User u left join fetch u.movies where u.id = :id")
    Optional<User> findById(@NotNull Long id);

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);
}
