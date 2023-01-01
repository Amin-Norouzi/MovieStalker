package com.aminnorouzi.ms.tool.view;

import com.aminnorouzi.ms.core.ApplicationContext;
import com.aminnorouzi.ms.model.user.User;
import com.aminnorouzi.ms.tool.view.ViewCacher.CacheKey;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lombok.*;
import org.springframework.stereotype.Component;

@Data
@Component
@RequiredArgsConstructor
@Getter(AccessLevel.NONE)
@Setter(AccessLevel.PROTECTED)
public class ViewSwitcher {

    private final ApplicationContext context;

    private final ViewLoader viewLoader;
    private final ViewCacher viewCacher;

    private View current;

    private Scene scene;

    @Getter(AccessLevel.PRIVATE)
    private Stage stage;

    public void initialize(Stage stage) {
        if (stage != null) {
            this.stage = stage;

            this.scene = new Scene(new Pane());
            this.current = View.EMPTY;
        }
    }

    public void switchTo(View view, Object input) {
        if (current.equals(view)) return;
        if (view.equals(View.SIGNIN)) {
            cleanup();
        }

        Parent root;
        CacheKey key = setup(view, input);

        if (viewCacher.contains(key)) {
            System.out.println("loading from cache");
            root = viewCacher.get(key);
        } else {
            System.out.println("loading from loader");
            root = viewLoader.load(view);
        }

        cacheup(key, root);
        showup(view, root);
    }

    public void switchTo(View view) {
       switchTo(view, null);
    }

    private CacheKey setup(View view, Object input) {
        context.setValue(input);
        User user = context.getUser();

        return new CacheKey(view, user, input);
    }

    private void cacheup(CacheKey key, Parent root) {
        viewCacher.cache(key, root);

        setCurrent(key.getView());
    }

    private void showup(View view, Parent root) {
        scene.setRoot(root);

        stage.setTitle(view.getTitle());
        stage.setScene(scene);
        stage.show();
    }

    private void cleanup() {
        setCurrent(View.getEmpty());
        viewCacher.cleanup();
    }
}
