package com.aminnorouzi.ms.service;

import com.dlsc.gemsfx.DialogPane;
import javafx.application.Platform;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.dlsc.gemsfx.DialogPane.Type.INFORMATION;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final DialogPane pane = new DialogPane();

    public void initialize(StackPane root) {
        root.getChildren().add(pane);
    }

    public void show(String type, Object input) {
        switch (type) {
            case "info" -> showInFo((String) input);
            case "error" -> showError((String) input);
        }
    }

    public void showList(ListView<String> list, String message, Runnable runnable) {
        pane.showNode(INFORMATION, message, list).thenAccept(action -> {
            boolean selected = !list.getSelectionModel().getSelectedItem().isEmpty();
            if (action.equals(ButtonType.OK) && selected) {
                execute(runnable);
            } else {
                showError("You didn't select a movie!");
            }
        });
    }

    private void execute(Runnable runnable) {
        Thread thread = new Thread(() -> Platform.runLater(runnable));

        thread.setDaemon(true);
        thread.start();
    }

    public void showInFo(String info) {
        pane.showInformation("Information", info);
    }

    public void showError(String error) {
        pane.showError("Error", error);
    }

    public void showConfirmation(String warning, Runnable runnable) {
       pane.showConfirmation("Confirmation", warning).thenAccept(action -> {
           if (action.equals(ButtonType.YES)) {
               execute(runnable);
           }
       });
    }
}
