package com.aminnorouzi.ms.controller;

import com.aminnorouzi.ms.configuration.ApplicationConfiguration;
import com.aminnorouzi.ms.controller.Controller;
import com.aminnorouzi.ms.model.View;
import com.aminnorouzi.ms.model.user.SigninRequest;
import com.aminnorouzi.ms.model.user.User;
import com.aminnorouzi.ms.service.FileService;
import com.aminnorouzi.ms.service.MovieService;
import com.aminnorouzi.ms.service.NotificationService;
import com.aminnorouzi.ms.service.UserService;
import com.aminnorouzi.ms.util.ViewSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

@Component
@FxmlView("/view/signin-view.fxml")
public class SigninController extends Controller {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    public SigninController(ApplicationConfiguration configuration, ViewSwitcher switcher, FileService fileService,
                            NotificationService notificationService, MovieService movieService, UserService userService) {
        super(configuration, switcher, notificationService, movieService, fileService, userService);
    }

    @Override
    protected void configure() {
        System.out.println("Running SigninController");
    }

    @FXML
    private void onSignin(ActionEvent event) {
        SigninRequest request = SigninRequest.builder()
                .username(usernameField.getText().toLowerCase())
                .password(passwordField.getText())
                .build();

        try {
            User found = userService.signin(request);
            if (found != null) {
                switcher.switchTo(View.HOME, found);
                return;
            }
        } catch (Exception ignored) {}

        System.out.println("Incorrect username or password!");
    }

    @FXML
    private void onSignup(MouseEvent event) {
        switcher.switchTo(View.SIGNUP);
    }
}
