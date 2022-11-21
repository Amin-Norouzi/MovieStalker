package com.aminnorouzi.ms.service;

import com.aminnorouzi.ms.model.user.SigninRequest;
import com.aminnorouzi.ms.model.user.SignupRequest;
import com.aminnorouzi.ms.model.user.User;
import com.aminnorouzi.ms.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository repository;

    private User create(SignupRequest request) {
        User user = User.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .build();

        User created = repository.save(user);
        return created;
    }

    public boolean signin(SigninRequest request) {
        User found = getUserByUsername(request.getUsername());
        return found.getPassword().equals(request.getPassword());
    }

    public User signup(SignupRequest request) {
        verifyUsername(request.getUsername());
        return create(request);
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
