package com.aminnorouzi.ms.controller;

import com.aminnorouzi.ms.configuration.ApplicationConfiguration;
import com.aminnorouzi.ms.model.View;
import com.aminnorouzi.ms.model.input.Input;
import com.aminnorouzi.ms.model.user.User;
import com.aminnorouzi.ms.service.MovieService;
import com.aminnorouzi.ms.util.ViewSwitcher;
import javafx.fxml.FXML;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@Component
@RequiredArgsConstructor
public abstract class Controller {

    private final ApplicationConfiguration configuration;
    private final ViewSwitcher switcher;
    private final MovieService movieService;

    private View view;
    private User user;
    private Input input;

    @FXML
    public void initialize() {
        setView(configuration.getView());
        setUser(configuration.getUser());
        setInput(configuration.getInput());

        configure();
    }

    protected abstract void configure();
}
