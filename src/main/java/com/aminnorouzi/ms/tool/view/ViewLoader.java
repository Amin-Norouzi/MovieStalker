package com.aminnorouzi.ms.tool.view;

import com.aminnorouzi.ms.controller.Controller;
import com.aminnorouzi.ms.exception.IllegalViewException;
import com.aminnorouzi.ms.model.user.User;
import javafx.scene.Parent;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.stereotype.Component;

@Data
@Component
@RequiredArgsConstructor
public class ViewLoader {

    private final FxWeaver fxWeaver;

    public Parent load(View view, User user, Object input) {
        try {
            Controller controller = (Controller) fxWeaver.getBean(view.getController());
            controller.setView(view);
            controller.setUser(user);
            controller.setInput(input);

            return fxWeaver.loadView(view.getController());
        } catch (Exception exception) {
            throw new IllegalViewException("Could not load view: " + view.getTitle());
        }
    }
}
