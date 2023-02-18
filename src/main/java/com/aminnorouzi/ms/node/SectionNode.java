package com.aminnorouzi.ms.node;

import com.aminnorouzi.ms.controller.Controller;
import com.aminnorouzi.ms.model.movie.Movie;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class SectionNode extends VBox implements Loadable {

    private final Controller controller;
    private final String title;
    private final Boolean hasButton;
    private final List<?> values;
    private final BiFunction<Controller, Object, Node> function;

    @FXML
    private TilePane contentPane;
    @FXML
    private Button seeButton;
    @FXML
    private Label titleLabel;

    public SectionNode(Controller controller, String title, List<?> values, BiFunction<Controller, Object, Node> function) {
        this(controller, title, true, values, function);
    }

    public SectionNode(Controller controller, String title, Boolean hasButton, List<?> values, BiFunction<Controller, Object, Node> function) {
        this.controller = controller;
        this.title = title;
        this.hasButton = hasButton;
        this.values = values;
        this.function = function;

        load(this);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (!hasButton) {
            seeButton.setVisible(false);
        }

        titleLabel.setText(title);

        Thread background = new Thread(() -> Platform.runLater(() -> {
            values.forEach(value -> {
                try {
                    Node node = function.apply(controller, value);
                    contentPane.getChildren().add(node);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }));

        background.setDaemon(true);
        background.start();
    }

    @Override
    public String getPath() {
        return "/templates/node/section-node.fxml";
    }
}
