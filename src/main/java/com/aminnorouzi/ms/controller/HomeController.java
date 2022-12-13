package com.aminnorouzi.ms.controller;

import com.aminnorouzi.ms.configuration.ApplicationConfiguration;
import com.aminnorouzi.ms.service.*;
import com.aminnorouzi.ms.util.ViewSwitcher;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

@Component
@FxmlView("/view/home-view.fxml")
public class HomeController extends Controller {

    public HomeController(ApplicationConfiguration configuration, ViewSwitcher switcher, FileService fileService,
                          NotificationService notificationService, MovieService movieService, UserService userService,
                          LibraryService libraryService) {
        super(configuration, switcher, notificationService, movieService, fileService, userService, libraryService);
    }

    @Override
    protected void configure() {

    }
}
