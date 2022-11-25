package com.aminnorouzi.ms.util;

import com.aminnorouzi.ms.controller.Controller;
import com.aminnorouzi.ms.model.View;
import com.aminnorouzi.ms.model.input.Input;
import com.aminnorouzi.ms.model.user.User;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

@Data
@Component
@RequiredArgsConstructor
public class ViewSwitcher {

    private static final Map<View, Parent> cache = new EnumMap<>(View.class);

    private final FxWeaver fxWeaver;

    private View current;

    private Scene scene;
    private Stage stage;

    public void initialize(Stage stage) {
        if (stage != null) {
            Scene scene = new Scene(new Pane());
            View current = View.EMPTY;

            this.stage = stage;
            this.scene = scene;
            this.current = current;
        }
    }

    public void switchTo(View view, User user) {
        if (current.equals(view)) return;

        Parent root;

        if (cache.containsKey(view)) {
            root = cache.get(view);
        } else {
            setup(view, user, null);
            root = fxWeaver.loadView(view.getController());
        }

        cacheup(view, root);
        showup(view, root);
    }

    public void switchTo(View view, Input input) {
        if (current.equals(view)) return;

        Parent root;

        if (cache.containsKey(view)) {
            root = cache.get(view);
        } else {
            setup(view, null, input);
            root = fxWeaver.loadView(view.getController());
        }

        cacheup(view, root);
        showup(view, root);
    }

    public void switchTo(View view) {
        if (current.equals(view)) return;

        Parent root;

        if (cache.containsKey(view)) {
            root = cache.get(view);
        } else {
            setup(view, null, null);
            root = fxWeaver.loadView(view.getController());
        }

        cacheup(view, root);
        showup(view, root);
    }

    private void cacheup(View view, Parent root) {
        if (cache.containsKey(view)) {
            setCurrent(view);
            return;
        }

        cache.put(view, root);
    }

    private void showup(View view, Parent root) {
        scene.setRoot(root);

        stage.setTitle(view.getTitle());
        stage.setScene(scene);
        stage.show();
    }

    private void setup(View view, User user, Input input) {
        Controller controller = (Controller) fxWeaver.getBean(view.getController());
        controller.setView(view);

        if (!View.isNoneHeader(view)) {
            Controller subController = (Controller) fxWeaver.getBean(View.HEADER.getController());
            subController.setView(view);
        }

        if (user != null) {
            controller.setUser(user);
        }
        if (input != null) {
            controller.setInput(input);
        }
    }

    public void cleanup() {
        setCurrent(View.getEmpty());
        cache.clear();
    }


}
