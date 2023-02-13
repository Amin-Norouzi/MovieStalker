package com.aminnorouzi.ms.node;

import com.aminnorouzi.ms.controller.Controller;
import com.aminnorouzi.ms.exception.IllegalViewException;
import com.aminnorouzi.ms.tool.view.View;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class SidebarNode extends HBox implements Initializable {

    private static final String PATH = "/templates/node/sidebar-node.fxml";
    private final Controller controller;

    @FXML
    private Button discoverButton;
    @FXML
    private Button homeButton;
    @FXML
    private Button libraryButton;

    public SidebarNode(Controller controller) {
        this.controller = controller;

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(PATH));
            fxmlLoader.setRoot(this);
            fxmlLoader.setController(this);
            fxmlLoader.load();

        } catch (Exception exception) {
            throw new IllegalViewException(exception.getMessage());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Workaround to change the button state pressed
        String id = "%s-button-clicked";
        String title = View.getTitle(controller.getView());
        switch (title.concat("Button")) {
            case "homeButton" -> homeButton.setId(String.format(id, title));
            case "discoverButton" -> discoverButton.setId(String.format(id, title));
            case "libraryButton" -> libraryButton.setId(String.format(id, title));
        }
    }

    @FXML
    private void onHome(ActionEvent event) {
        controller.switchTo(View.HOME);
    }

    @FXML
    private void onDiscover(ActionEvent event) {
        controller.switchTo(View.DISCOVER);
    }

    @FXML
    private void onLibrary(ActionEvent event) {
        controller.switchTo(View.LIBRARY);
    }

    @FXML
    private void onLogout(ActionEvent event) {
        controller.switchTo(View.SIGNIN);
    }
}
