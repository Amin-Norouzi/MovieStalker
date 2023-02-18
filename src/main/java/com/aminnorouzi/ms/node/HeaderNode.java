package com.aminnorouzi.ms.node;

import com.aminnorouzi.ms.controller.Controller;
import com.aminnorouzi.ms.tool.view.View;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class HeaderNode extends HBox implements Loadable {

    private final Controller controller;

    @FXML
    private TextField searchField;

    public HeaderNode(Controller controller) {
        this.controller = controller;

        load(this);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @Override
    public String getPath() {
        return "/templates/node/header-node.fxml";
    }

    @FXML
    private void onSearch(ActionEvent event) {
        String text = searchField.getText();
        if (!text.isBlank()) {
            controller.switchTo(View.DISCOVER, text);
            searchField.clear();
        }
    }
}
