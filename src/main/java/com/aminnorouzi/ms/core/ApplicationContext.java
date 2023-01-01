package com.aminnorouzi.ms.core;

import com.aminnorouzi.ms.controller.Controller;
import com.aminnorouzi.ms.model.user.User;
import com.aminnorouzi.ms.service.UserService;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;

@Setter
@Configuration
@RequiredArgsConstructor
@Getter(AccessLevel.NONE)
public class ApplicationContext {

    private final UserService userService;

    private Long id;
    private User user;
    private Object value;

    public void initialize(long id) {
        this.id = id;
    }

    public void load(Controller controller) {
        if (id != null) {
            user = userService.getById(id);
            controller.setUser(user);
            controller.setInput(value);
        }
    }

    public User user() {
        return this.user;
    }
}
