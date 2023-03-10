package com.aminnorouzi.ms.service;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    public void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message);
        alert.show();
    }

    public void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message);
        alert.show();
    }

    public void showWarning(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING, message);
        alert.show();
    }

    public void showConfirmation(String message, Runnable runnable) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message);
        alert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == ButtonType.YES) {
                try {
                    runnable.run();
                } catch (Exception e) {
                    showError(e.getMessage());
                }
            }
        });
    }
}
