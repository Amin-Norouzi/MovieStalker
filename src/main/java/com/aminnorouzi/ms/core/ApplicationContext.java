package com.aminnorouzi.ms.core;

import com.aminnorouzi.ms.controller.Controller;
import com.aminnorouzi.ms.controller.SidebarController;
import com.aminnorouzi.ms.model.user.User;
import com.aminnorouzi.ms.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@RequiredArgsConstructor
public class ApplicationContext {

    private final UserService userService;

    private Long id;
    private User user;
    private Object value;

    public void initialize(long id) {
        this.user = userService.getById(id);
        this.id = id;
    }

    public void load(Controller controller) {
        if (id != null && !(controller instanceof SidebarController)) {
            user = userService.getById(id);
            controller.setUser(user);
            controller.setInput(value);
        }
    }
}
