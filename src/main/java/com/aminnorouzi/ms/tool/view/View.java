package com.aminnorouzi.ms.tool.view;

import com.aminnorouzi.ms.controller.*;
import lombok.Getter;

@Getter
public enum View {

    HOME(HomeController.class, "Home", "/templates/view/home-view.fxml", true),
    LIBRARY(LibraryController.class, "Library", "/templates/view/library-view.fxml", true),
    MOVIE(MovieController.class, "Movie", "/templates/view/movie-view.fxml", false),
    SIGNIN(SigninController.class, "Sign in", "/templates/view/signin-view.fxml", false),
    SIGNUP(SignupController.class, "Sign up", "/templates/view/signup-view.fxml", false),
    ADDITION(AdditionController.class, "Addition", "/templates/view/addition-view.fxml", true),

    EMPTY(null, null, null, false);

    private final Class<?> controller;
    private final String title;
    private final String path;
    private final boolean hasSidebar;

    View(Class<?> controller, String title, String path, boolean hasSidebar) {
        this.controller = controller;
        this.title = title;
        this.path = path;
        this.hasSidebar = hasSidebar;
    }

    public static View getDefault() {
        return View.SIGNIN;
    }

    public static View getEmpty() {
        return View.EMPTY;
    }
}
