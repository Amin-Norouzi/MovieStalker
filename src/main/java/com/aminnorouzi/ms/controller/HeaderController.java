package com.aminnorouzi.ms.controller;

import com.aminnorouzi.ms.model.Header;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Slf4j
@Component
@FxmlView("/view/header-view.fxml")
public class HeaderController extends Controller {

    @FXML
    private Label titleLabel;

    @FXML
    private Button button;

    @Override
    protected void configure() {
        Header header = getView().getHeader();

        titleLabel.setText(header.getTitle());
        if (!header.getHasButton()) {
            button.setVisible(false);
        }
    }
}
