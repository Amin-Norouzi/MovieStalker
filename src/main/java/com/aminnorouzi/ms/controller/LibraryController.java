package com.aminnorouzi.ms.controller;

import com.aminnorouzi.ms.model.View;
import com.aminnorouzi.ms.util.ViewSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Slf4j
@Component
@FxmlView("/view/library-view.fxml")
public class LibraryController extends Controller {

    private final ViewSwitcher switcher;

    @Override
    protected void configure() {

    }

    @FXML
    private void onAddition(ActionEvent event) {
        switcher.switchTo(View.ADDITION);
    }
}
