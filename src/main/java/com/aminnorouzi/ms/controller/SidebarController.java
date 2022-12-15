package com.aminnorouzi.ms.controller;

import com.aminnorouzi.ms.configuration.ApplicationConfiguration;
import com.aminnorouzi.ms.model.View;
import com.aminnorouzi.ms.service.*;
import com.aminnorouzi.ms.util.ViewManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

@Component
@FxmlView("/view/sidebar-view.fxml")
public class SidebarController extends Controller {

    public SidebarController(ApplicationConfiguration configuration, ViewManager switcher, FileService fileService,
                             NotificationService notificationService, MovieService movieService, UserService userService,
                             LibraryService libraryService) {
        super(configuration, switcher, notificationService, movieService, fileService, userService, libraryService);
    }

    @Override
    protected void configure() {

    }

    @FXML
    private void onHome(ActionEvent event) {
        switchTo(View.HOME);
    }

    @FXML
    private void onLibrary(ActionEvent event) {
        switchTo(View.LIBRARY);
    }

    @FXML
    private void onLogout(ActionEvent event) {
        switchTo(View.SIGNIN);
    }
}
