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

import java.util.EnumMap;
import java.util.Map;

@Data
@Component
@RequiredArgsConstructor
public class ViewSwitcher implements ApplicationListener<StageReadyEvent> {

    private final FxWeaver fxWeaver;

    private static final Map<View, Parent> cache = new EnumMap<>(View.class);

    private View current;

    private Scene scene;
    private Stage stage;

    public void switchTo(View view) {
        if (current.equals(view)) return;

        Parent root;

        if (cache.containsKey(view)) {
            root = cache.get(view);
            setCurrent(view);
        } else {
            root = fxWeaver.loadView(view.getController());
            setCurrent(view);

            cache.put(view, root);
        }

        scene.setRoot(root);

        stage.setTitle(view.getTitle());
        stage.setScene(scene);
        stage.show();
    }

    public void cleanup() {
        cache.clear();
    }

    private void init(Stage stage, Scene scene) {
        if (scene != null && stage != null) {
            this.stage = stage;
            this.scene = scene;
        }
    }

    @Override
    public void onApplicationEvent(StageReadyEvent event) {
        View view = View.getDefault();
        Stage stage = event.getStage();
        Scene scene = new Scene(fxWeaver.loadView(view.getController()));

        init(stage, scene);
        setCurrent(view);

        cache.put(view, scene.getRoot());

        stage.setScene(scene);
        stage.setTitle(view.getTitle());
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.show();
    }

    public void setCurrent(View current) {
        this.current = current;
    }
}
