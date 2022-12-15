package com.aminnorouzi.ms.configuration;

import com.aminnorouzi.ms.controller.Controller;
import com.aminnorouzi.ms.model.user.User;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@RequiredArgsConstructor
public class ApplicationConfiguration {

    private User user;
    private Object input;

    public void getValues(Controller controller) {
        controller.setUser(user);
        controller.setInput(input);
    }

    public void setValues(User user, Object input) {
        if (user != null) {
            setUser(user);
        }

        setInput(input);
    }
}
