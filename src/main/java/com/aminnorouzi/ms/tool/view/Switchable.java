package com.aminnorouzi.ms.tool.view;

public interface Switchable {

    default void switchTo(View view) {
        switchTo(view, null);
    }

    void switchTo(View view, Object input);
}
