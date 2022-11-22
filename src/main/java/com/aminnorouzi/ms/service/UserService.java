package com.aminnorouzi.ms.service;

import com.aminnorouzi.ms.model.user.SigninRequest;
import com.aminnorouzi.ms.model.user.SignupRequest;
import com.aminnorouzi.ms.model.user.User;
import com.aminnorouzi.ms.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository repository;

    public User signup(SignupRequest request) {
        verifyUsername(request.getUsername());

        User user = User.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .fullName(request.getFullName())
                .build();

        User created = repository.save(user);

        log.info("Signed up new user: {}", created);
        return created;
    }

    public boolean signin(SigninRequest request) {
        User found = getUserByUsername(request.getUsername());

        boolean equals = found.getPassword()
                .equals(request.getPassword());

        log.info("Signed in user: status={}, {}", equals, found);
        return equals;
    }

    public List<User> getAll() {
        List<User> found = repository.findAll();

        log.info("Found all users: {}", found);
        return found;
    }

    public User getUserByUsername(String username) {
        return repository.findByUsername(username).orElseThrow(RuntimeException::new);
    }

    private void verifyUsername(String username) {
        boolean exists = repository.existsByUsername(username);
        if (exists) {
            throw new RuntimeException();
        }
    }
}
