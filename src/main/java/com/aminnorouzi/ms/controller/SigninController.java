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
@FxmlView("/view/signin-view.fxml")
public class SigninController extends Controller {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    public SigninController(ApplicationConfiguration configuration, ViewManager switcher, FileService fileService,
                            NotificationService notificationService, MovieService movieService, UserService userService,
                            LibraryService libraryService) {
        super(configuration, switcher, notificationService, movieService, fileService, userService, libraryService);
    }

    @Override
    protected void configure() {

    }

    @FXML
    private void onSignin(ActionEvent event) {
        Request request = Request.builder()
                .username(usernameField.getText().toLowerCase())
                .password(passwordField.getText())
                .build();

        try {
            User found = userService.signin(request);
            if (found != null) {
                setUser(found);
                switchTo(View.HOME);
                return;
            }
        } catch (Exception ignored) {}

        System.out.println("Incorrect username or password!");
    }

    @FXML
    private void onSignup(MouseEvent event) {
        switchTo(View.SIGNUP);
    }
}
