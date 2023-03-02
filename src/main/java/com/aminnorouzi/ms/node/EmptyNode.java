package com.aminnorouzi.ms.node;

import com.aminnorouzi.ms.controller.Controller;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class EmptyNode extends HBox implements Loadable {

    private final Controller controller;
    private final String message;

    @FXML
    private Label messageLabel;

    public EmptyNode(Controller controller, String message) {
        this.controller = controller;
        this.message = message;

        load(this);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        messageLabel.setText(message);
    }

    @Override
    public String getPath() {
        return "/templates/node/empty-node.fxml";
    }
}
