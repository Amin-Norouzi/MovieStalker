package com.aminnorouzi.ms.service;

import com.dlsc.gemsfx.DialogPane;
import com.dlsc.gemsfx.DialogPane.Type;
import javafx.scene.layout.StackPane;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final DialogPane pane = new DialogPane();

    public void initialize(StackPane root) {
        root.getChildren().add(pane);
    }

    public void show(Type type, String message) {
        pane.showInformation("Information", message);
    }
}
