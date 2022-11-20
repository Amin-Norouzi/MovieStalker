package com.aminnorouzi.ms.controller;

import com.aminnorouzi.ms.model.View;
import com.aminnorouzi.ms.util.ViewSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
@FxmlView("/view/sidebar-view.fxml")
public class SidebarController extends Controller {

    private final ViewSwitcher switcher;

    @Override
    protected void configure() {
//        System.out.println(getClass().getSimpleName());
    }

    @FXML
    private void onHome(ActionEvent event) {
        switcher.switchTo(View.HOME);
    }

    @FXML
    private void onLibrary(ActionEvent event) {
        switcher.switchTo(View.LIBRARY);
    }

    @FXML
    private void onLogout(ActionEvent event) {
        switcher.cleanup();
        switcher.switchTo(View.SIGNIN);
    }
}
