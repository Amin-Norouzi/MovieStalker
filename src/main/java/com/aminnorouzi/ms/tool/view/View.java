package com.aminnorouzi.ms.tool.view;

import com.aminnorouzi.ms.controller.*;
import lombok.Getter;

@Getter
public enum View {

    HOME(HomeController.class, "Home", true),
    LIBRARY(LibraryController.class, "Library", true),
    MOVIE(MovieController.class, "Movie", false),
    SIGNIN(SigninController.class, "Sign in", false),
    SIGNUP(SignupController.class, "Sign up", false),
    DISCOVER(DiscoverController.class, "Discover", true),

    PREVIOUS(null, null, false),
    EMPTY(null, null, false);

    private final Class<?> controller;
    private final String title;
    private final boolean hasSidebar;

    View(Class<?> controller, String title, boolean hasSidebar) {
        this.controller = controller;
        this.title = title;
        this.hasSidebar = hasSidebar;
    }

    public static View getDefault() {
        return View.SIGNIN;
    }

    public static View getEmpty() {
        return View.EMPTY;
    }

    public static String getTitle(View view) {
        return view.getTitle().toLowerCase();
    }

    public static View of(String title) {
        switch (title.toLowerCase()) {
            case "home" -> {
                return View.HOME;
            }
            case "library" -> {
                return View.LIBRARY;
            }
            case "movie" -> {
                return View.MOVIE;
            }
            case "signin" -> {
                return View.SIGNIN;
            }
            case "signup" -> {
                return View.SIGNUP;
            }
            case "addition" -> {
                return View.DISCOVER;
            }
            default -> {
                return View.EMPTY;
            }
        }
    }
}
