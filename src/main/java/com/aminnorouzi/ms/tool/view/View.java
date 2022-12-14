package com.aminnorouzi.ms.tool.view;

import com.aminnorouzi.ms.controller.impl.*;
import lombok.Getter;

@Getter
public enum View {

    HOME(HomeController.class, "Home"),
    LIBRARY(LibraryController.class, "Library"),
    MOVIE(MovieController.class, "Movie"),
    SIDEBAR(SidebarController.class, "Sidebar"),
    SIGNIN(SigninController.class, "Sign in"),
    SIGNUP(SignupController.class, "Sign up"),
    ADDITION(AdditionController.class, "Addition"),
//    OFFLINE(null, "/view/offline-view.fxml"),

    EMPTY(null, null);

    private final Class<?> controller;
    private final String title;

    View(Class<?> controller, String title) {
        this.controller = controller;
        this.title = title;
    }

    public static View getDefault() {
        return View.SIGNIN;
    }

    public static View getEmpty() {
        return View.EMPTY;
    }
}
