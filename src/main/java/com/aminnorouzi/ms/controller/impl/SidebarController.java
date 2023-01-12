package com.aminnorouzi.ms.controller.impl;

import com.aminnorouzi.ms.controller.Controller;
import com.aminnorouzi.ms.core.ApplicationContext;
import com.aminnorouzi.ms.service.ActivityService;
import com.aminnorouzi.ms.service.LibraryService;
import com.aminnorouzi.ms.service.NotificationService;
import com.aminnorouzi.ms.tool.view.View;
import com.aminnorouzi.ms.tool.view.ViewSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

@Component
@FxmlView("/templates/view/sidebar-view.fxml")
public class SidebarController extends Controller {

    public SidebarController(ApplicationContext context, ViewSwitcher switcher, NotificationService notification,
                            LibraryService library, ActivityService activity) {
        super(context, switcher, notification, library, activity);
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
