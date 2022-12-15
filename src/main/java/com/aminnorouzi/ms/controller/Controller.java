package com.aminnorouzi.ms.controller;

import com.aminnorouzi.ms.configuration.ApplicationConfiguration;
import com.aminnorouzi.ms.model.View;
import com.aminnorouzi.ms.model.user.User;
import com.aminnorouzi.ms.service.*;
import com.aminnorouzi.ms.util.ViewManager;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@Component
@RequiredArgsConstructor
public abstract class Controller {

    @Getter(AccessLevel.NONE)
    private final ApplicationConfiguration configuration;

    @Getter(AccessLevel.NONE)
    private final ViewManager switcher;

    protected final NotificationService notificationService;
    protected final MovieService movieService;
    protected final FileService fileService;
    protected final UserService userService;
    protected final LibraryService libraryService;

    private User user;
    private Object input;

    @FXML
    private StackPane root;

    @FXML
    protected void initialize() {
        configuration.getValues(this);

        notificationService.initialize(root);

        configure();
    }

    protected abstract void configure();

    protected void switchTo(View view) {
        switchTo(view, null);
    }

    protected void switchTo(View view, Object input) {
        configuration.setValues(user, input);

        switcher.switchTo(view);
    }
}
