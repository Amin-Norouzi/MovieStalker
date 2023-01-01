package com.aminnorouzi.ms.controller;

import com.aminnorouzi.ms.core.ApplicationContext;
import com.aminnorouzi.ms.service.LibraryService;
import com.aminnorouzi.ms.service.NotificationService;
import com.aminnorouzi.ms.util.view.View;
import com.aminnorouzi.ms.util.view.ViewSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

@Component
@FxmlView("/view/sidebar-view.fxml")
public class SidebarController extends Controller {

    public SidebarController(ApplicationContext configuration, ViewSwitcher switcher,
                           NotificationService notificationService, LibraryService libraryService) {
        super(configuration, switcher, notificationService, libraryService);
    }

    @Override
    protected void configure() {

    }

    @FXML
    private void onHome(ActionEvent event) {
        getSwitcher().switchTo(View.HOME);
    }

    @FXML
    private void onLibrary(ActionEvent event) {
        getSwitcher().switchTo(View.LIBRARY);
    }

    @FXML
    private void onLogout(ActionEvent event) {
        getSwitcher().switchTo(View.SIGNIN);
    }
}
