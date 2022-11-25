package com.aminnorouzi.ms.model;

import com.aminnorouzi.ms.controller.*;
import lombok.Getter;

@Getter
public enum View {

    HOME(HomeController.class, "Home", Header.HOME),
    LIBRARY(LibraryController.class, "Library", Header.LIBRARY),
    MOVIE(null, "Movie", Header.MOVIE),
    SIDEBAR(SidebarController.class, "Sidebar", Header.NONE),
    HEADER(HeaderController.class, "Header", Header.NONE),
    SIGNIN(SigninController.class, "Sign in", Header.NONE),
    SIGNUP(SignupController.class, "Sign up", Header.NONE),
    ADDITION(AdditionController.class, "Addition", Header.ADDITION),
    RESULT(ResultController.class, "Result", Header.RESULT),

    EMPTY(null, null, Header.NONE);

    private final Class<?> controller;
    private final String title;
    private final Header header;

    View(Class<?> controller, String title, Header header) {
        this.controller = controller;
        this.title = title;
        this.header = header;
    }

    public static View getDefault() {
        return View.HOME;
    }

    public static View getEmpty() {
        return View.EMPTY;
    }

    public static boolean isNoneHeader(View view) {
        return view.getHeader().equals(Header.NONE);
    }
}
