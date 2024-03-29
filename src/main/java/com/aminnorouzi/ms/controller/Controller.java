package com.aminnorouzi.ms.controller;

import com.aminnorouzi.ms.model.user.User;
import com.aminnorouzi.ms.node.HeaderNode;
import com.aminnorouzi.ms.node.SidebarNode;
import com.aminnorouzi.ms.service.ActivityService;
import com.aminnorouzi.ms.service.LibraryService;
import com.aminnorouzi.ms.tool.image.ImageLoader;
import com.aminnorouzi.ms.service.NotificationService;
import com.aminnorouzi.ms.tool.view.View;
import com.aminnorouzi.ms.tool.view.ViewSwitcher;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
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
public class Controller implements Switchable, Executable {

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
    private Pane content;

    @FXML
    protected void initialize() {
        if (view.getHasSidebar()) layout.setLeft(new SidebarNode(this));
        if (view.getHasHeader()) content.getChildren().add(0, new HeaderNode(this));

        configure();
    }

    protected void configure() { /* Child classes can override it to do their own configuration */}

    protected void supply(Node value) {
        content.getChildren().add(value);
    }

    protected void supply(Node value, int index) {
        content.getChildren().add(index, value);
    }

    @Override
    public void execute(Callable<User> callable, View view) {
        try {
            User updated = callable.call();
            setUser(updated);

            if (view != null) {
                switchTo(view);
            }
        } catch (Exception e) {
            notification.showError(e.getMessage());
        }
    }

    @Override
    public void switchTo(View view, Object input) {
        switcher.switchTo(view, getUser(), input);
    }
}
