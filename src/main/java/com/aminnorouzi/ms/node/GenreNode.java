package com.aminnorouzi.ms.node;

import com.aminnorouzi.ms.controller.Controller;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class GenreNode extends HBox implements Loadable {

    private final Controller controller;
    private final String title;

    @FXML
    private Button genreButton;

    public GenreNode(Controller controller, String title) {
        this.controller = controller;
        this.title = title;

        load(this);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        genreButton.setText(title);
    }

    @Override
    public String getPath() {
        return "/templates/node/genre-node.fxml";
    }
}
