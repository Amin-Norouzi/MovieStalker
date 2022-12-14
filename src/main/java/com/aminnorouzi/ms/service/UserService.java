package com.aminnorouzi.ms.service;

import com.aminnorouzi.ms.exception.IllegalSigninException;
import com.aminnorouzi.ms.exception.IllegalSignupException;
import com.aminnorouzi.ms.exception.UserNotFoundException;
import com.aminnorouzi.ms.model.user.UserRequest;
import com.aminnorouzi.ms.model.user.User;
import com.aminnorouzi.ms.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public User update(User request) {
        User user = getById(request.getId());

        if (request.getFullName() != null &&
                !request.getFullName().equals(user.getFullName())) {
            user.setFullName(request.getFullName());
        }
        if (request.getPassword() != null &&
                !request.getPassword().equals(user.getPassword())) {
            user.setPassword(request.getPassword());
        }
        if (request.getMovies() != null &&
                !request.getMovies().equals(user.getMovies())) {
            user.setMovies(request.getMovies());
        }

        User updated = userRepository.save(user);

        log.info("Updated an user: {}", updated);
        return updated;
    }

    public User signup(UserRequest request) {
        verify(request);

        User user = User.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .fullName(request.getFullName())
                .build();

        User created = userRepository.save(user);

        log.info("Signed up for user: {}", created);
        return created;
    }

    private void verify(UserRequest request) {
        boolean existing = userRepository.existsByUsername(request.getUsername());
        if (existing) {
            throw new IllegalSignupException(String.format("Username: %s already exists!", request.getUsername()));
        }
    }

    public User signin(UserRequest userRequest) {
        User found = getByUsername(userRequest.getUsername());

        verify(userRequest, found);

        log.info("Signed in for user: {}", found);
        return found;
    }

    private void verify(UserRequest request, User user) {
        boolean equals = user.getPassword().equals(request.getPassword());
        if (!equals) {
            throw new IllegalSigninException("Incorrect username or password!");
        }
    }

    @Transactional
    public User getById(Long id) {
        User found = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(String.format("User: %s does not exist", id)));

        found.getMovies();

        log.info("Found a user: {}", found);
        return found;
    }

    public User getByUsername(String username) {
        User found = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(String.format("User: %s does not exist", username)));

        log.info("Found an user: {}", found);
        return found;
    }
}
