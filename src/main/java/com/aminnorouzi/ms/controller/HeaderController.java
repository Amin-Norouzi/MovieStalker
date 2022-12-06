package com.aminnorouzi.ms.controller;

import com.aminnorouzi.ms.configuration.ApplicationConfiguration;
import com.aminnorouzi.ms.controller.Controller;
import com.aminnorouzi.ms.service.FileService;
import com.aminnorouzi.ms.service.MovieService;
import com.aminnorouzi.ms.service.NotificationService;
import com.aminnorouzi.ms.service.UserService;
import com.aminnorouzi.ms.util.ViewSwitcher;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@FxmlView("/view/header-view.fxml")
public class HeaderController extends Controller {

    @FXML
    private Label titleLabel;

    public HeaderController(ApplicationConfiguration configuration, ViewSwitcher switcher, FileService fileService,
                              NotificationService notificationService, MovieService movieService, UserService userService) {
        super(configuration, switcher, notificationService, movieService, fileService, userService);
    }

    @Override
    protected void configure() {
        String header = getView().getTitle();

        titleLabel.setText(header);
    }
}
