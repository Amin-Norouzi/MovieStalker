package com.aminnorouzi.ms.controller;

import com.aminnorouzi.ms.core.ApplicationContext;
import com.aminnorouzi.ms.model.user.User;
import com.aminnorouzi.ms.service.*;
import com.aminnorouzi.ms.util.view.ViewSwitcher;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@Component
@RequiredArgsConstructor
public abstract class Controller {

    private final ApplicationContext context;
    private final ViewSwitcher switcher;

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
        context.load(this);

        notificationService.initialize(root);

        configure();
    }

    protected abstract void configure();
}
