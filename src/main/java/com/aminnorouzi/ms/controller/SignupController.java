package com.aminnorouzi.ms.controller;

import com.aminnorouzi.ms.configuration.ApplicationConfiguration;
import com.aminnorouzi.ms.model.View;
import com.aminnorouzi.ms.model.user.Request;
import com.aminnorouzi.ms.model.user.User;
import com.aminnorouzi.ms.service.*;
import com.aminnorouzi.ms.util.ViewManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

@Component
@FxmlView("/view/signup-view.fxml")
public class SignupController extends Controller {

    @FXML
    private TextField usernameField;
    @FXML
    private TextField fullNameField;
    @FXML
    private PasswordField passwordField;

    public SignupController(ApplicationConfiguration configuration, ViewManager switcher, FileService fileService,
                            NotificationService notificationService, MovieService movieService, UserService userService,
                            LibraryService libraryService) {
        super(configuration, switcher, notificationService, movieService, fileService, userService, libraryService);
    }

    @Override
    protected void configure() {

    }

    @FXML
    private void onSignup(ActionEvent event) {
        Request request = Request.builder()
                .fullName(fullNameField.getText().toLowerCase())
                .username(usernameField.getText().toLowerCase())
                .password(passwordField.getText())
                .build();

        try {
            User created = userService.signup(request);
            if (created != null) {
                switchTo(View.HOME);
                return;
            }
        } catch (RuntimeException ignored) {}

        System.out.println("Username already exists!");
    }

    @FXML
    private void onSignin(MouseEvent event) {
        switchTo(View.SIGNIN);
    }
}
