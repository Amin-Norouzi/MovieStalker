package com.aminnorouzi.ms.controller;

import com.aminnorouzi.ms.configuration.ApplicationConfiguration;
import com.aminnorouzi.ms.model.View;
import com.aminnorouzi.ms.model.input.Input;
import com.aminnorouzi.ms.model.user.User;
import com.aminnorouzi.ms.service.FileService;
import com.aminnorouzi.ms.service.MovieService;
import com.aminnorouzi.ms.service.NotificationService;
import com.aminnorouzi.ms.service.UserService;
import com.aminnorouzi.ms.util.ViewSwitcher;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@Component
@RequiredArgsConstructor
public abstract class Controller {

    protected final ApplicationConfiguration configuration;
    protected final ViewSwitcher switcher;

    protected final NotificationService notificationService;
    protected final MovieService movieService;
    protected final FileService fileService;
    protected final UserService userService;

    protected View view;
    protected User user;
    protected Input input;

    @FXML
    protected StackPane root;

    @FXML
    public void initialize() {
        setView(configuration.getView());
        setUser(configuration.getUser());
        setInput(configuration.getInput());

        notificationService.initialize(root);

        configure();
    }

    protected abstract void configure();
}
