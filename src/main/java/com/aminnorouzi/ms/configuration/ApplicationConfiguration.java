package com.aminnorouzi.ms.configuration;

import com.aminnorouzi.ms.model.View;
import com.aminnorouzi.ms.model.input.Input;
import com.aminnorouzi.ms.model.user.User;
import lombok.Data;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class ApplicationConfiguration {

    private View view;
    private User user;
    private Object input;
}
