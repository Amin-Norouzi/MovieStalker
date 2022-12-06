package com.aminnorouzi.ms.util;

import com.aminnorouzi.ms.model.View;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.springframework.stereotype.Component;

@Component
public class ViewLoader {

    public Parent load(View view) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(view.getTitle()));

            return fxmlLoader.load();
        } catch (Exception e) {
            return null;
        }
    }
}
