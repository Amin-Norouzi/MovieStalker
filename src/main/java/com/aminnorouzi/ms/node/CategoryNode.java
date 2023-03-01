package com.aminnorouzi.ms.node;

import com.aminnorouzi.ms.controller.Controller;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.ResourceBundle;

public class CategoryNode extends StackPane implements Loadable {

    private final Controller controller;
    private final String genre;

    @FXML
    private Label genreLabel;
    @FXML
    private Rectangle genrePic;

    public CategoryNode(Controller controller, String genre) {
        this.controller = controller;
        this.genre = genre;

        load(this);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Thread background = new Thread(() -> Platform.runLater(() -> {
//            ImageInfo backdropInfo = new ImageInfo(movie.getPoster(), 181*4, 236*4, true);
//            controller.getImage().load(backdropInfo).thenAccept(image -> {
//                genrePic.setFill(new ImagePattern(image));
//            });

            genreLabel.setText(genre);
        }));

        background.setDaemon(true);
        background.start();
    }

    @Override
    public String getPath() {
        return "/templates/node/category-node.fxml";
    }
}
