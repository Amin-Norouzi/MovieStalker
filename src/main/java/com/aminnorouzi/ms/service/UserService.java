package com.aminnorouzi.ms.service;

import com.aminnorouzi.ms.exception.IllegalSigninException;
import com.aminnorouzi.ms.exception.IllegalSignupException;
import com.aminnorouzi.ms.exception.UserNotFoundException;
import com.aminnorouzi.ms.model.user.User;
import com.aminnorouzi.ms.model.user.UserRequest;
import com.aminnorouzi.ms.repository.UserRepository;
import com.aminnorouzi.ms.util.PasswordUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.util.ArrayList;
import java.util.prefs.Preferences;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {

    private static final Preferences preferences = Preferences.userRoot();

    private static final String AUTHENTICATED_KEY = "moviestalker-authenticated";
    private static final String USERNAME_KEY = "moviestalker-username";
    private static final String PASSWORD_KEY = "moviestalker-password";

    private final UserRepository userRepository;
    private final PasswordUtil passwordUtil;
    private final Clock clock;

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

        String password = passwordUtil.hash(request.getPassword());

        User user = User.builder()
                .username(request.getUsername())
                .password(password)
                .fullName(request.getFullName())
                .movies(new ArrayList<>())
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

    public User signin(UserRequest request) {
        User found = getByUsername(request.getUsername());
        if (!request.getAutomated()) {
            verify(request, found);
            cache(found, request);
        }

        User validated = getById(found.getId());

        log.info("Signed in for user: {}", validated);
        return validated;
    }

    private void verify(UserRequest request, User user) {
        String password = request.getPassword();
        String hashed = user.getPassword();

        boolean equals = passwordUtil.validate(password, hashed);
        if (!equals) {
            throw new IllegalSigninException("Incorrect username or password!");
        }
    }

    private void cache(User user, UserRequest request) {
        if (!request.getAuthenticated()) {
            return;
        }

        preferences.putBoolean(AUTHENTICATED_KEY, request.getAuthenticated());
        preferences.put(USERNAME_KEY, user.getUsername());
        preferences.put(PASSWORD_KEY, user.getPassword());

        log.info("Cached a user: {}", user);
    }

    public User logout(User user) {
        String username = preferences.get(USERNAME_KEY, "");
        if (user.getUsername().equals(username)) {
            preferences.remove(AUTHENTICATED_KEY);
            preferences.remove(USERNAME_KEY);
            preferences.remove(PASSWORD_KEY);
        }

        log.info("Logged out a user: {}", user);
        return user;
    }

    public User remember() {
        String username = preferences.get(USERNAME_KEY, "");
        String password = preferences.get(PASSWORD_KEY, "");

        UserRequest request;
        if (!username.equals("") && !password.equals("")) {
            request = UserRequest.builder()
                    .username(username)
                    .password(password)
                    .authenticated(true)
                    .automated(true)
                    .build();
        } else {
            request = UserRequest.of(username, password);
        }

        log.info("Remembered a user: request={}", request);
        return signin(request);
    }

    public boolean exists() {
        return preferences.getBoolean(AUTHENTICATED_KEY, false);
    }

    private User getById(Long id) {
        User found = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(String.format("User: %s does not exist", id)));

        log.info("Found an user: {}", found);
        return found;
    }

    private User getByUsername(String username) {
        User found = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(String.format("User: %s does not exist", username)));

        log.info("Found an user: {}", found);
        return found;
    }
}
