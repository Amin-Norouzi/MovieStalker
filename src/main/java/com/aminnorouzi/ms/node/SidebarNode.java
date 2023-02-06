package com.aminnorouzi.ms.node;

import com.aminnorouzi.ms.controller.Controller;
import com.aminnorouzi.ms.exception.IllegalViewException;
import com.aminnorouzi.ms.tool.view.View;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class SidebarNode extends VBox {

    private static final String PATH = "/templates/node/sidebar-node.fxml";
    private final Controller controller;

    public SidebarNode(Controller controller) {
        this.controller = controller;

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(PATH));
            fxmlLoader.setRoot(this);
            fxmlLoader.setController(this);
            fxmlLoader.load();

            // Workaround to change the button state pressed
            String title = View.getTitle(controller.getView());
            for (Node node : ((VBox) lookup("#container")).getChildren()) {
                if (node instanceof Button) {
                    String text = ((Button) node).getText().toLowerCase();
                    if (text.equals(title)) {
                        node.setId(String.format("%s-button-clicked", title));
                        break;
                    }
                }
            }
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            throw new IllegalViewException(exception.getMessage());
        }
    }

    @FXML
    private void onHome(ActionEvent event) {
        controller.switchTo(View.HOME);
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
