package com.aminnorouzi.ms.controller;

import com.aminnorouzi.ms.model.user.User;
import com.aminnorouzi.ms.node.SidebarNode;
import com.aminnorouzi.ms.service.ActivityService;
import com.aminnorouzi.ms.service.LibraryService;
import com.aminnorouzi.ms.service.NotificationService;
import com.aminnorouzi.ms.tool.image.ImageService;
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

import java.util.concurrent.Callable;

@Data
@Component
@RequiredArgsConstructor
public abstract class Controller {

    @Getter(AccessLevel.NONE)
    private final ViewSwitcher switcher;

    protected final NotificationService notification;
    protected final LibraryService library;
    protected final ActivityService activity;
    protected final ImageService image;

    private View view;
    private User user;
    private Object input;

    private boolean changed = false;

    @FXML
    private StackPane root;
    @FXML
    private BorderPane content;

    @FXML
    protected void initialize() {
        if (view.isHasSidebar()) {
            content.setLeft(new SidebarNode(this));
        }

        notification.initialize(root);

        configure();
    }

    protected void execute(Callable<User> callable) {
        execute(callable, null);
    }

    protected void execute(Callable<User> callable, View view) {
        try {
            User updated = callable.call();

            setUser(updated);
            setChanged(true);

            if (view != null) {
                switchTo(view);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void switchTo(View view) {
        switchTo(view, null);
    }

    public void switchTo(View view, Object input) {
        switcher.switchTo(view, getUser(), input);
    }

    protected abstract void configure();
}
