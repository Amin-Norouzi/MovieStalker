package com.aminnorouzi.ms.controller;

import com.aminnorouzi.ms.model.user.UserRequest;
import com.aminnorouzi.ms.service.ActivityService;
import com.aminnorouzi.ms.service.LibraryService;
import com.aminnorouzi.ms.service.NotificationService;
import com.aminnorouzi.ms.tool.image.ImageService;
import com.aminnorouzi.ms.tool.view.View;
import com.aminnorouzi.ms.tool.view.ViewSwitcher;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

@Component
@FxmlView("/templates/view/signin-view.fxml")
public class SigninController extends Controller {

    @FXML
    private MFXTextField usernameField;
    @FXML
    private MFXPasswordField passwordField;
    @FXML
    private Button signinButton;

    public SigninController(ViewSwitcher switcher, NotificationService notification, LibraryService library, ActivityService activity, ImageService image) {
        super(switcher, notification, library, activity, image);
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
            execute(() -> activity.signin(request));

            switchTo(View.HOME);
        } catch (Exception exception) {
            notification.showError(exception.getMessage());
        }
    }

    @FXML
    private void onSignup(MouseEvent event) {
        switchTo(View.SIGNUP);
    }
}
