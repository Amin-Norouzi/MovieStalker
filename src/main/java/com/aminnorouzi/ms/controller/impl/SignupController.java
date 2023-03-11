package com.aminnorouzi.ms.controller.impl;

import com.aminnorouzi.ms.controller.Controller;
import com.aminnorouzi.ms.model.user.UserRequest;
import com.aminnorouzi.ms.service.ActivityService;
import com.aminnorouzi.ms.service.LibraryService;
import com.aminnorouzi.ms.service.NotificationService;
import com.aminnorouzi.ms.tool.image.ImageLoader;
import com.aminnorouzi.ms.tool.view.View;
import com.aminnorouzi.ms.tool.view.ViewSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

@Component
@FxmlView("/templates/view/signup-view.fxml")
public class SignupController extends Controller {

    @FXML
    private TextField usernameField;
    @FXML
    private TextField fullNameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button signupButton;
    @FXML
    private CheckBox privacyBox;

    public SignupController(ViewSwitcher switcher, NotificationService notification, LibraryService library, ActivityService activity, ImageLoader image) {
        super(switcher, notification, library, activity, image);
    }

    @Override
    protected void configure() {
        signupButton.disableProperty().bind(fullNameField.textProperty().isEmpty()
                .or(usernameField.textProperty().isEmpty())
                .or(passwordField.textProperty().isEmpty())
                .or(usernameField.textProperty().length().lessThan(5))
                .or(usernameField.textProperty().length().greaterThan(16))
                .or(passwordField.textProperty().length().lessThan(4))
                .or(passwordField.textProperty().length().greaterThan(32))
                .or(privacyBox.selectedProperty().not()));
    }

    @FXML
    private void onSignup(ActionEvent event) {
        UserRequest request = UserRequest.builder()
                .fullName(fullNameField.getText().toLowerCase())
                .username(usernameField.getText().toLowerCase())
                .password(passwordField.getText())
                .build();

        execute(() -> activity.signup(request), View.HOME);
    }

    @FXML
    private void onSignin(MouseEvent event) {
        switchTo(View.SIGNIN);
    }
}
