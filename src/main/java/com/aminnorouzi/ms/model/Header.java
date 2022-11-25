package com.aminnorouzi.ms.model;

import lombok.Getter;

@Getter
public enum Header {

    HOME("Home", false),
    LIBRARY("Library", true),
    MOVIE("Movie", true),
    ADDITION("Addition", false),
    RESULT("Result", false),

    NONE(null, false);

    private final String title;
    private final Boolean hasButton;

    Header(String title, boolean hasButton) {
        this.title = title;
        this.hasButton = hasButton;
    }
}
