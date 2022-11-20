package com.aminnorouzi.ms.model;

import com.aminnorouzi.ms.controller.*;

public enum View {

    HOME(HomeController.class, "/view/home-view.fxml", "Home"),
    LIBRARY(LibraryController.class, "/view/library-view.fxml", "Library"),
    MOVIE(null, "/view/movie-view.fxml", "Movie"),
    SIDEBAR(SidebarController.class, "/view/sidebar-view.fxml", "Sidebar"),
    SIGNIN(SigninController.class, "/view/signin-view.fxml", "Signin"),
    SIGNUP(SignupController.class, "/view/signup-view.fxml", "Signup");

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

    public static View getDefault() {
        return View.HOME;
    }
}
