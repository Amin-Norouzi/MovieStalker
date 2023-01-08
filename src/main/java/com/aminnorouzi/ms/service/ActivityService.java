package com.aminnorouzi.ms.service;

import com.aminnorouzi.ms.model.user.User;
import com.aminnorouzi.ms.model.user.UserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final UserService userService;

    public User signin(UserRequest request) {
        return userService.signin(request);
    }

    public User signup(UserRequest request) {
        return userService.signup(request);
    }


}
