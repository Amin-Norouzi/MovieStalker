package com.aminnorouzi.ms.node;

import com.aminnorouzi.ms.controller.Controller;
import com.aminnorouzi.ms.model.movie.Movie;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class SectionNode extends VBox implements Loadable {

    private final Controller controller;
    private final String title;
    private final List<Movie> content;

    @FXML
    private TilePane contentPane;
    @FXML
    private Button seeButton;
    @FXML
    private Label titleLabel;

    public SectionNode(Controller controller, String title, List<Movie> content) {
        this.controller = controller;
        this.title = title;
        this.content = content;

        load(this);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Thread background = new Thread(() -> Platform.runLater(() -> {
            titleLabel.setText(title);

            content.forEach(movie -> contentPane.getChildren().add(new MovieNode(controller, movie)));
        }));

        background.setDaemon(true);
        background.start();
    }

    @Override
    public String getPath() {
        return "/templates/node/section-node.fxml";
    }
}
