package com.aminnorouzi.ms.node;

import com.aminnorouzi.ms.controller.Controller;
import com.aminnorouzi.ms.tool.image.ImageLoader;
import com.aminnorouzi.ms.tool.image.Info;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.ResourceBundle;

public class CategoryNode extends StackPane implements Loadable {

    private static final Button ICON = generateIconButton();

    private final Controller controller;
    private final String category;

    @FXML
    private Label categoryLabel;
    @FXML
    private Rectangle categoryImage;
    @FXML
    private HBox categoryPane;

    public CategoryNode(Controller controller, String category) {
        this.controller = controller;
        this.category = category;

        load(this);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Thread background = new Thread(() -> {
            String base = "/templates/image/genre/%s-genre.png";
            String path = base.formatted(category.toLowerCase()
                    .replaceAll("\\s", "-").trim());

            Image image = new Image(path, true);

            Platform.runLater(() -> {
                image.progressProperty().addListener((observable, oldValue, newValue) -> {
                    if ((Double) observable.getValue() == 1.0) {
                        categoryImage.setFill(new ImagePattern(image));
                    }
                });

//            controller.getImage().load(path, Info.GENRE_NODE_BACKDROP, categoryImage);
                categoryLabel.setText(category);
            });
        });

        background.setDaemon(true);
        background.start();
    }

    @Override
    public String getPath() {
        return "/templates/node/category-node.fxml";
    }

    @FXML
    private void onCategory(MouseEvent event) {
        if (event.getEventType().equals(MouseEvent.MOUSE_ENTERED)) {
            categoryPane.getChildren().add(ICON);
            return;
        }

        categoryPane.getChildren().remove(ICON);
    }

    private static Button generateIconButton() {
        Button button = new Button();
        button.setMinSize(24, 24);
        button.setPrefSize(24, 24);
        button.setMaxSize(24, 24);
        button.setId("icon-button");

        return button;
    }
}
