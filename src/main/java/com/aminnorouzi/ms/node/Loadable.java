package com.aminnorouzi.ms.node;

import com.aminnorouzi.ms.exception.IllegalViewException;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

public interface Loadable extends Initializable {

    String getPath();

    default void load(Loadable loadable) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(getPath()));
            fxmlLoader.setRoot(loadable);
            fxmlLoader.setController(loadable);
            fxmlLoader.load();

        } catch (Exception exception) {
            throw new IllegalViewException(exception.getMessage());
        }
    }
}
