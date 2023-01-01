package com.aminnorouzi.ms.controller;

import com.aminnorouzi.ms.core.ApplicationContext;
import com.aminnorouzi.ms.util.view.View;
import com.aminnorouzi.ms.model.user.Request;
import com.aminnorouzi.ms.model.user.User;
import com.aminnorouzi.ms.service.*;
import com.aminnorouzi.ms.util.view.ViewSwitcher;
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

    public SignupController(ApplicationContext configuration, ViewSwitcher switcher, FileService fileService,
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
                getContext().initialize(created.getId());
                getSwitcher().switchTo(View.HOME);
            }
        } catch (RuntimeException exception) {
            System.out.println(exception.getMessage());
        }
    }

    @FXML
    private void onSignin(MouseEvent event) {
        getSwitcher().switchTo(View.SIGNIN);
    }
}
