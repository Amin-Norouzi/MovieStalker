package com.aminnorouzi.ms.controller;

import com.aminnorouzi.ms.core.ApplicationContext;
import com.aminnorouzi.ms.model.user.User;
import com.aminnorouzi.ms.service.ActivityService;
import com.aminnorouzi.ms.service.LibraryService;
import com.aminnorouzi.ms.service.NotificationService;
import com.aminnorouzi.ms.tool.view.View;
import com.aminnorouzi.ms.tool.view.ViewSwitcher;
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

    private final ApplicationContext context;

    @Getter(AccessLevel.NONE)
    private final ViewSwitcher switcher;

    protected final NotificationService notification;
    protected final LibraryService library;
    protected final ActivityService activity;

    private User user;
    private Object input;

    @FXML
    private StackPane root;

    @FXML
    protected void initialize() {
        context.load(this);

        notification.initialize(root);

        configure();
    }

//    protected void execute(Task<User> task) {
//        Thread thread = new Thread(() -> {
//            task.run();
//            Platform.runLater(() -> update(task.getValue()));
//        });
//
//        thread.setDaemon(true);
//        thread.start();
//    }
//
//    protected void update(User request) {
//        System.out.println(request.getFullName());
//        setUser(request);
//    }

    protected void switchTo(View view) {
        switchTo(view, null);
    }

    protected void switchTo(View view, Object input) {
        context.setUser(user);
        switcher.switchTo(view, input);
    }

    protected abstract void configure();
}
