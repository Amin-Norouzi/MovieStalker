package com.aminnorouzi.ms.controller;

import com.aminnorouzi.ms.node.EmptyNode;
import javafx.scene.layout.Pane;

public interface Emptiable {

    Integer NON_REMOVABLE_INDEX = 0;

    default void onEmpty(Pane content, Controller controller, String message) {
        content.getChildren().removeIf(
                child -> content.getChildren().indexOf(child) != NON_REMOVABLE_INDEX
        );

        content.getChildren().add(new EmptyNode(controller, message));
    }
}
