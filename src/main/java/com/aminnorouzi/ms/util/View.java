package com.aminnorouzi.ms.util;

import com.aminnorouzi.ms.controller.HomeController;
import com.aminnorouzi.ms.controller.LibraryController;
import com.aminnorouzi.ms.controller.SidebarController;

public enum View {

    HOME(HomeController.class, "/view/home-view.fxml", "Home"),
    LIBRARY(LibraryController.class, "/view/library-view.fxml", "Library"),
    MOVIE(null, "/view/movie-view.fxml", "Movie"),
    SIDEBAR(SidebarController.class, "/view/sidebar-view.fxml", "Sidebar");

    private final Class<?> controller;
    private final String path;
    private final String title;

    View(Class<?> controller, String path, String title) {
        this.controller = controller;
        this.path = path;
        this.title = title;
    }

    public Class<?> getController() {
        return controller;
    }

    public String getPath() {
        return path;
    }

    public String getTitle() {
        return title;
    }
}
