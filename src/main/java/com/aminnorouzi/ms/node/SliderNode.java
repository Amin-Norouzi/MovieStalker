package com.aminnorouzi.ms.node;

import com.aminnorouzi.ms.controller.Controller;
import com.aminnorouzi.ms.model.movie.Movie;
import com.aminnorouzi.ms.tool.image.Info;
import com.aminnorouzi.ms.tool.view.View;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import lombok.Setter;

import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.ResourceBundle;

@Setter
public class SliderNode extends StackPane implements Loadable {

    private static final Integer FIRST_INDEX = 0;
    private static final Integer LAST_INDEX = 4;

    private final Controller controller;
    private final List<Movie> movies;

    private Thread thread;
    private Movie current;
    private boolean reset = false;

    @FXML
    private Rectangle backdropPic;
    @FXML
    private Label titleLabel;
    @FXML
    private HBox pageContainer;

    public SliderNode(List<Movie> movies, Controller controller) {
        this.controller = controller;
        this.movies = movies;
        this.current = movies.get(0);

        load(this);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Thread background = new Thread(() -> {
            while (true) {
                show(current);
                delay();

                if (reset) {
                    delay();
                    reset = false;
                }

                setCurrent(next());
            }
        });

        background.setName("background-task-slider");
        background.setDaemon(true);

        setThread(background);
        thread.start();
    }

    @Override
    public String getPath() {
        return "/templates/node/slider-node.fxml";
    }

    @FXML
    private void onWatch(MouseEvent event) {
        thread.interrupt();
        controller.switchTo(View.MOVIE, current);
    }

    @FXML
    private void onPrevious(ActionEvent event) {
        int nextIndex = movies.indexOf(current) - 1;
        if (nextIndex >= FIRST_INDEX) {
            show(movies.get(nextIndex));
        } else {
            show(movies.get(LAST_INDEX));
        }

        reset = true;
    }

    @FXML
    private void onNext(ActionEvent event) {
        int nextIndex = movies.indexOf(current) + 1;
        if (nextIndex <= LAST_INDEX) {
            show(movies.get(nextIndex));
        } else {
            show(movies.get(FIRST_INDEX));
        }

        reset = true;
    }

    private void show(Movie movie) {
        Platform.runLater(() -> {
            setCurrent(movie);
            // TODO Auto-generated method stub
            backdropPic.setFill(Color.GRAY);

            controller.getImage().load(movie.getBackdrop(), Info.SLIDER_NODE_BACKDROP).thenAccept(image -> {
                backdropPic.setFill(new ImagePattern(image));
            });

            String title = movie.getTitle() + " " + movie.getReleased().getYear();
            titleLabel.setText(title);

            update(movie);
        });
    }

    private void update(Movie movie) {
        ObservableList<Node> nodes = pageContainer.getChildren();

        int index = movies.indexOf(movie);

        for (Node node : nodes) {
            Rectangle page = (Rectangle) node;
            if (nodes.indexOf(page) == index) {
                page.setOpacity(1);
            } else {
                page.setOpacity(0.5);
            }
        }
    }

    private Movie next() {
        int nextIndex = movies.indexOf(current) + 1;
        if (nextIndex <= LAST_INDEX) {
            return movies.get(nextIndex);
        }

        return movies.get(FIRST_INDEX);
    }

    private void delay() {
        try {
            if (Thread.currentThread().getName().equals("background-task-slider")) {
                Thread.sleep(Duration.ofSeconds(5));
            }
        } catch (InterruptedException ignored) {
        }
    }
}
