package com.aminnorouzi.ms.tool.view;

import com.aminnorouzi.ms.controller.*;
import lombok.Getter;

@Getter
public enum View {

    HOME(HomeController.class, "Home", true, true),
    LIBRARY(LibraryController.class, "Library", true, true),
    MOVIE(MovieController.class, "Movie", false, false),
    SIGNIN(SigninController.class, "Sign in", false, false),
    SIGNUP(SignupController.class, "Sign up", false, false),
    DISCOVER(DiscoverController.class, "Discover", true, false),

    PREVIOUS(null, null, false, false),
    EMPTY(null, null, false, false);

    private final Class<?> controller;
    private final String title;
    private final Boolean hasSidebar;
    private final Boolean hasHeader;

    View(Class<?> controller, String title, boolean hasSidebar, boolean hasHeader) {
        this.controller = controller;
        this.title = title;
        this.hasSidebar = hasSidebar;
        this.hasHeader = hasHeader;
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
