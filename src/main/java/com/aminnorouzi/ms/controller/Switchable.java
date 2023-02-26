package com.aminnorouzi.ms.controller;

import com.aminnorouzi.ms.tool.view.View;

public interface Switchable {

    default void switchTo(View view) {
        switchTo(view, null);
    }

    void switchTo(View view, Object input);
}
