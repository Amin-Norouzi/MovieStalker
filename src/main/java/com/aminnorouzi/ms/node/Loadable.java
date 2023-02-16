package com.aminnorouzi.ms.node;

import com.aminnorouzi.ms.exception.IllegalViewException;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;

public interface Loadable extends Initializable {

    String getPath();

    default void load(Node node) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(getPath()));
            fxmlLoader.setRoot(node);
            fxmlLoader.setController(node);
            fxmlLoader.load();

        } catch (Exception exception) {
            throw new IllegalViewException(exception.getMessage());
        }
    }
}
