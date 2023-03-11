package com.aminnorouzi.ms.node;

import com.aminnorouzi.ms.controller.Controller;
import com.aminnorouzi.ms.model.movie.Movie;
import com.aminnorouzi.ms.tool.image.Info;
import com.aminnorouzi.ms.tool.view.View;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.ResourceBundle;

public class MovieNode extends StackPane implements Loadable {

    private final Controller controller;
    private final Movie movie;

    @FXML
    private Rectangle posterPic;
    @FXML
    private Label genreLabel;
    @FXML
    private Label titleLabel;
    @FXML
    private Label yearLabel;

    public MovieNode(Controller controller, Movie movie) {
        this.controller = controller;
        this.movie = movie;

        load(this);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        controller.getImage().load(movie.getPoster(), Info.MOVIE_NODE_POSTER, posterPic);

        titleLabel.setText(movie.getTitle());
        yearLabel.setText(String.valueOf(movie.getReleased().getYear()));
        genreLabel.setText(movie.getGenres().get(0));
    }

    @Override
    public String getPath() {
        return "/templates/node/movie-node.fxml";
    }

    @FXML
    private void onMovie(ActionEvent event) {
        controller.switchTo(View.MOVIE, movie);
    }
}

