package com.aminnorouzi.ms.controller;

import com.aminnorouzi.ms.model.user.User;
import com.aminnorouzi.ms.tool.view.View;

import java.util.concurrent.Callable;

public interface Executable {

    default void execute(Callable<User> callable) {
        execute(callable, null);
    }

    void execute(Callable<User> callable, View view);
}
