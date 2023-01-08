package com.aminnorouzi.ms.controller.impl;

import com.aminnorouzi.ms.controller.Controller;
import com.aminnorouzi.ms.core.ApplicationContext;
import com.aminnorouzi.ms.model.user.User;
import com.aminnorouzi.ms.model.user.UserRequest;
import com.aminnorouzi.ms.service.ActivityService;
import com.aminnorouzi.ms.service.LibraryService;
import com.aminnorouzi.ms.service.NotificationService;
import com.aminnorouzi.ms.tool.view.View;
import com.aminnorouzi.ms.tool.view.ViewSwitcher;
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

    public SignupController(ApplicationContext context, ViewSwitcher switcher, NotificationService notification,
                            LibraryService library, ActivityService activity) {
        super(context, switcher, notification, library, activity);
    }


    @Override
    protected void configure() {

    }

    @FXML
    private void onSignup(ActionEvent event) {
        UserRequest userRequest = UserRequest.builder()
                .fullName(fullNameField.getText().toLowerCase())
                .username(usernameField.getText().toLowerCase())
                .password(passwordField.getText())
                .build();

        try {
            User created = activity.signup(userRequest);

            getContext().initialize(created.getId());
            switchTo(View.HOME);
        } catch (RuntimeException exception) {
            notification.showError(exception.getMessage());
        }
    }

    @FXML
    private void onSignin(MouseEvent event) {
        switchTo(View.SIGNIN);
    }
}
