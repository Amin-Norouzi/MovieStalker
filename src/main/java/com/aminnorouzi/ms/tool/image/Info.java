package com.aminnorouzi.ms.tool.image;

import lombok.Getter;

@Getter
public enum Info {

    MOVIE_POSTER(300 * 2, 960 * 2, true, true),
    MOVIE_BACKDROP(1280, 720, true, true),

    MOVIE_NODE_POSTER(181 * 3, 236 * 3, true, true),
    SLIDER_NODE_BACKDROP(969*2, 432*2, true, false),
    SEARCH_NODE_POSTER(181 * 3, 236 * 3, true, false),
    GENRE_NODE_BACKDROP(300 * 2, 960 * 2, true, true);

    private final int width;
    private final int height;
    private final boolean cacheable;
    private final boolean savable;

    Info(int width, int height, boolean cacheable, boolean savable) {
        this.width = width;
        this.height = height;
        this.cacheable = cacheable;
        this.savable = savable;
    }
}
