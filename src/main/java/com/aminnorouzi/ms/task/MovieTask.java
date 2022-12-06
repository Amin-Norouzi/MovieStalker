package com.aminnorouzi.ms.task;

import com.aminnorouzi.ms.model.movie.Movie;
import com.aminnorouzi.ms.util.ComponentUtils;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;

public class MovieTask extends Task<Void> {

    private final Movie movie;
    private final TilePane gridView;

    public MovieTask(Movie movie, TilePane gridView) {
        this.movie = movie;
        this.gridView = gridView;
    }

    private ImageView imageView;

    @Override
    protected Void call() throws Exception {
        imageView = new ImageView();
        imageView.setFitHeight(300);
        imageView.setFitWidth(200);
        imageView.setImage(new Image("https://image.tmdb.org/t/p/original" + movie.getPoster()));

        return null;
    }

    @Override
    protected void succeeded() {
        super.succeeded();
        Platform.runLater(() -> {
            ComponentUtils.roundImage(imageView);
            gridView.getChildren().add(imageView);
        });
    }
}
