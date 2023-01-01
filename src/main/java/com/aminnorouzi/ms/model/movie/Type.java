package com.aminnorouzi.ms.model.movie;

public enum Type {

    MOVIE,
    TV;

    public static Type of(String value) {
        return Type.valueOf(value.toUpperCase());
    }
}
