package com.aminnorouzi.ms.util;

import com.aminnorouzi.ms.MovieStalkerApplication.MovieStalkerIntegrationApplication.StageReadyEvent;
import com.aminnorouzi.ms.model.View;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Data
@Component
@RequiredArgsConstructor
public class ViewSwitcher implements ApplicationListener<StageReadyEvent> {

    private final FxWeaver fxWeaver;

    private static final Map<View, Parent> cache = new HashMap<>();

    private Scene scene;
    private Stage stage;

    private View current;

    public void switchTo(View view) {
        if (current.equals(view)) return;

        if (scene == null || stage == null) {
            System.out.println("No scene/stage was set");
            return;
        }

        Parent root;

        if (cache.containsKey(view)) {
            root = cache.get(view);
            setCurrent(view);
        } else {
            root = fxWeaver.loadView(view.getController());

            cache.put(view, root);
            setCurrent(view);
        }

        scene.setRoot(root);

        stage.setTitle(view.getTitle());
        stage.setScene(scene);
        stage.show();
    }

    private void init(Stage stage, Scene scene) {
        if (scene != null && stage != null) {
            this.stage = stage;
            this.scene = scene;
        }
    }

    @Override
    public void onApplicationEvent(StageReadyEvent event) {
        View view = View.HOME;
        Stage stage = event.getStage();
        Scene scene = new Scene(fxWeaver.loadView(view.getController()));

        init(stage, scene);
        cache.put(view, scene.getRoot());
        setCurrent(view);

        stage.setScene(scene);
        stage.setTitle(view.getTitle());
        stage.show();
    }

    public void setCurrent(View current) {
        this.current = current;
    }
}
