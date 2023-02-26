package com.aminnorouzi.ms.controller;

import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public interface Searchable {

    default void clear(Pane content) {
        content.getChildren().forEach(child -> {
            if (!(child instanceof TextField)) {
                content.getChildren().remove(child);
            }
        });
    }

    void search(String text);
}
