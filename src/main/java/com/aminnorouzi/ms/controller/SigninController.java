package com.aminnorouzi.ms.controller;

import com.aminnorouzi.ms.core.ApplicationContext;
import com.aminnorouzi.ms.model.user.User;
import com.aminnorouzi.ms.model.user.UserRequest;
import com.aminnorouzi.ms.service.LibraryService;
import com.aminnorouzi.ms.service.NotificationService;
import com.aminnorouzi.ms.util.view.View;
import com.aminnorouzi.ms.util.view.ViewSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
    @FXML
    private Button signinButton;

    public SigninController(ApplicationContext configuration, ViewSwitcher switcher,
                            NotificationService notificationService, LibraryService libraryService) {
        super(configuration, switcher, notificationService, libraryService);
    }

    @Override
    protected void configure() {
        signinButton.disableProperty().bind(usernameField.textProperty().isEmpty()
                .or(usernameField.textProperty().length().lessThan(5))
                .or(passwordField.textProperty().isEmpty())
                .or(passwordField.textProperty().length().lessThan(4)));
    }

    @FXML
    public void onSignin(ActionEvent event) {
        UserRequest request = UserRequest.builder()
                .username(usernameField.getText().toLowerCase())
                .password(passwordField.getText())
                .build();

        try {
            User found = libraryService.signin(request);

            getContext().initialize(found.getId());
            getSwitcher().switchTo(View.HOME);
        } catch (Exception exception) {
            notificationService.showError(exception.getMessage());
        }
    }

    @FXML
    private void onSignup(MouseEvent event) {
        getSwitcher().switchTo(View.SIGNUP);
    }
}
