package com.aminnorouzi.ms.model.movie;

public enum Type {

    MOVIE("Movie"),
    SERIES("Series");

    private final String name;

    Type(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
