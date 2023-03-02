package com.aminnorouzi.ms.controller;

import javafx.scene.layout.Pane;

public interface Searchable {

    Integer NON_REMOVABLE_INDEX = 0;

    default void clear(Pane content) {
        content.getChildren().removeIf(
                child -> content.getChildren().indexOf(child) != NON_REMOVABLE_INDEX
        );
    }

    void find(String text);
}
