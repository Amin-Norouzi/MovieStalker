package com.aminnorouzi.ms.service;

import com.aminnorouzi.ms.exception.IllegalSigninException;
import com.aminnorouzi.ms.exception.IllegalSignupException;
import com.aminnorouzi.ms.exception.MovieNotFoundException;
import com.aminnorouzi.ms.exception.UserNotFoundException;
import com.aminnorouzi.ms.model.movie.Movie;
import com.aminnorouzi.ms.model.user.SigninRequest;
import com.aminnorouzi.ms.model.user.SignupRequest;
import com.aminnorouzi.ms.model.user.UpdateRequest;
import com.aminnorouzi.ms.model.user.User;
import com.aminnorouzi.ms.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository repository;

    public User update(UpdateRequest request) {
        User user = getById(request.getId());

        if (request.getFullName() != null) {
            user.setFullName(request.getFullName());
        }
        if (request.getPassword() != null) {
            user.setPassword(request.getPassword());
        }
        if (request.getMovies() != null) {
            user.setMovies(request.getMovies());
        }

        User updated = repository.save(user);

        log.info("Updated an user: {}", updated);
        return updated;
    }

    public User signup(SignupRequest request) {
        verify(request);

        User user = User.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .fullName(request.getFullName())
                .build();

        User created = repository.save(user);

        log.info("Signed up for user: {}", created);
        return created;
    }

    private void verify(SignupRequest request) {
        User existing = getByUsername(request.getUsername());
        if (existing != null) {
            throw new IllegalSignupException("Username: {} already exists!");
        }
    }

    public User signin(SigninRequest request) {
        User found = getByUsername(request.getUsername());

        verify(request, found);

        log.info("Signed in for user: {}", found);
        return found;
    }

    private void verify(SigninRequest request, User user) {
        boolean equals = user.getPassword().equals(request.getPassword());
        if (!equals) {
            throw new IllegalSigninException("Incorrect username or password!");
        }
    }

    public User getById(Long id) {
        User found = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(String.format("User: %s does not exist", id)));

        log.info("Found a user: {}", found);
        return found;
    }

    public User getByUsername(String username) {
        User found = repository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(String.format("User: %s does not exist", username)));

        log.info("Found an user: {}", found);
        return found;
    }

    public List<User> getAll() {
        List<User> found = repository.findAll();

        log.info("Found all users: {}", found);
        return found;
    }

    public UpdateRequest build(Long id, List<Movie> movies) {
        return UpdateRequest.builder()
                .id(id)
                .movies(movies)
                .build();
    }
}
