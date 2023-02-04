package com.aminnorouzi.ms.event;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public interface MouseEventHandler {

    EventHandler<MouseEvent> onMouseClicked();
    EventHandler<MouseEvent> onMouseEntered();
    EventHandler<MouseEvent> onMouseExited();
}
