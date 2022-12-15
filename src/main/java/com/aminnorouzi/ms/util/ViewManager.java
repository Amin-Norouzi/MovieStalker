package com.aminnorouzi.ms.util;

import com.aminnorouzi.ms.configuration.ApplicationConfiguration;
import com.aminnorouzi.ms.model.View;
import com.aminnorouzi.ms.util.CacheManager.CacheKey;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.stereotype.Component;

@Data
@Component
@RequiredArgsConstructor
public class ViewManager {

    private final ApplicationConfiguration configuration;
    private final FxWeaver fxWeaver;

    private final CacheManager cacheManager;

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

    public void switchTo(View view) {
        if (current.equals(view)) return;
        if (view.equals(View.SIGNIN)) {
            cleanup();
        }

        Parent root;

        Object input = configuration.getInput();
        CacheKey key = new CacheKey(view, input);

        if (cacheManager.contains(key)) {
            root = cacheManager.get(key);
        } else {
            root = fxWeaver.loadView(view.getController());
        }

        cacheup(key, root);
        showup(view, root);
    }

    private void cacheup(CacheKey key, Parent root) {
        cacheManager.cache(key, root);

        setCurrent(key.getView());
    }

    private void showup(View view, Parent root) {
        scene.setRoot(root);

        stage.setTitle(view.getTitle());
        stage.setScene(scene);
        stage.show();
    }

    public void cleanup() {
        setCurrent(View.getEmpty());
        cacheManager.cleanup();
    }
}
