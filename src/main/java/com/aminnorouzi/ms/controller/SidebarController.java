package com.aminnorouzi.ms.controller;

import com.aminnorouzi.ms.configuration.ApplicationConfiguration;
import com.aminnorouzi.ms.model.View;
import com.aminnorouzi.ms.service.*;
import com.aminnorouzi.ms.util.ViewSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

@Component
@FxmlView("/view/sidebar-view.fxml")
public class SidebarController extends Controller {

    public SidebarController(ApplicationConfiguration configuration, ViewSwitcher switcher, FileService fileService,
                             NotificationService notificationService, MovieService movieService, UserService userService,
                             LibraryService libraryService) {
        super(configuration, switcher, notificationService, movieService, fileService, userService, libraryService);
    }

    @Override
    protected void configure() {

    }

    @FXML
    private void onHome(ActionEvent event) {
        switcher.switchTo(View.HOME, getUser());
    }

    @FXML
    private void onLibrary(ActionEvent event) {
        switcher.switchTo(View.LIBRARY, getUser());
    }

    @FXML
    private void onLogout(ActionEvent event) {
        switcher.cleanup();
        switcher.switchTo(View.SIGNIN);
    }
}
