package com.aminnorouzi.ms.controller;

import com.aminnorouzi.ms.configuration.ApplicationConfiguration;
import com.aminnorouzi.ms.model.View;
import com.aminnorouzi.ms.model.user.User;
import com.aminnorouzi.ms.service.*;
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

    private final ApplicationConfiguration configuration;
    protected final ViewSwitcher switcher;

    protected final NotificationService notificationService;
    protected final MovieService movieService;
    protected final FileService fileService;
    protected final UserService userService;
    protected final LibraryService libraryService;

    private View view;
    private User user;
    private Object input;

    @FXML
    private StackPane root;

    @FXML
    protected void initialize() {
        configuration.getChanges(this);


        notificationService.initialize(root);

        configure();
    }

    protected abstract void configure();
}
