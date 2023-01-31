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

    public void switchTo(View view) {
        switchTo(view, null);
    }

    public void switchTo(View view, Object input) {
        switcher.switchTo(view, getUser(), input);
    }

    protected abstract void configure();
}
