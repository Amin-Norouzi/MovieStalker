package com.aminnorouzi.ms.controller;

import com.aminnorouzi.ms.model.user.SignupRequest;
import com.aminnorouzi.ms.model.user.User;
import com.aminnorouzi.ms.model.View;
import com.aminnorouzi.ms.service.UserService;
import com.aminnorouzi.ms.util.ViewSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import lombok.RequiredArgsConstructor;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
@FxmlView("/view/signup-view.fxml")
public class SignupController extends Controller {

    private final UserService userService;
    private final ViewSwitcher switcher;

    @FXML
    private TextField usernameField;
    @FXML
    private TextField fullNameField;
    @FXML
    private PasswordField passwordField;

    @Override
    protected void configure() {
        System.out.println("Running SignupController");

        System.out.println("-------------------- my user --------------------");
        System.out.println(getUser().toString());
    }

    @FXML
    private void onSignup(ActionEvent event) {
        SignupRequest request = SignupRequest.builder()
                .fullName(fullNameField.getText().toLowerCase())
                .username(usernameField.getText().toLowerCase())
                .password(passwordField.getText())
                .build();

        try {
            User created = userService.signup(request);
            if (created != null) {
                switcher.switchTo(View.HOME);
                return;
            }
        } catch (RuntimeException ignored) {}

        System.out.println("Username already exists!");
    }

    @FXML
    private void onSignin(MouseEvent event) {
        switcher.switchTo(View.SIGNIN);
    }
}
