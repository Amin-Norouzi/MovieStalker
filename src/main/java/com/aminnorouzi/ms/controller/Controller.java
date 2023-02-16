package com.aminnorouzi.ms.controller;

import com.aminnorouzi.ms.model.movie.Movie;
import com.aminnorouzi.ms.model.user.User;
import com.aminnorouzi.ms.node.SidebarNode;
import com.aminnorouzi.ms.service.ActivityService;
import com.aminnorouzi.ms.service.LibraryService;
import com.aminnorouzi.ms.tool.image.ImageLoader;
import com.aminnorouzi.ms.tool.notification.NotificationService;
import com.aminnorouzi.ms.tool.view.Switchable;
import com.aminnorouzi.ms.tool.view.View;
import com.aminnorouzi.ms.tool.view.ViewSwitcher;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.Callable;

@Data
@Component
@RequiredArgsConstructor
public class Controller implements Switchable {

    @Getter(AccessLevel.NONE)
    private final ViewSwitcher switcher;

    protected final NotificationService notification;
    protected final LibraryService library;
    protected final ActivityService activity;
    protected final ImageLoader image;

    private View view;
    private User user;
    private Object input;

    @FXML
    private StackPane root;
    @FXML
    private BorderPane layout;

    @FXML
    protected void initialize() {
        if (view.isHasSidebar()) {
            layout.setLeft(new SidebarNode(this));
        }

        notification.initialize(root);

        configure();
    }

    protected void configure() { /* Child classes can override it to do their own configuration */}

    protected void execute(Callable<User> callable) {
        execute(callable, null);
    }

    protected void execute(Callable<User> callable, View view) {
        try {
            User updated = callable.call();
            setUser(updated);

            if (view != null) {
                switchTo(view);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void switchTo(View view) {
        switchTo(view, null);
    }

    @Override
    public void switchTo(View view, Object input) {
        if (input instanceof Long value) {
            input = getUser().getMovies().stream()
                    .filter(m -> m.getId().equals(value))
                    .findFirst().get();
        }

        switcher.switchTo(view, getUser(), input);
    }
}
