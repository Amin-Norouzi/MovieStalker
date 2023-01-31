package com.aminnorouzi.ms.tool.view;

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

    private final ViewLoader viewLoader;
    private final ViewCacher viewCacher;

    private View current;

    @Getter(AccessLevel.PRIVATE)
    private Stage stage;

    public void initialize(Stage stage) {
        if (stage != null) {
            stage.setScene(new Scene(new Pane()));

            this.stage = stage;
            this.current = View.EMPTY;
        }
    }

    public void switchTo(View view, User user, Object input) {
        if (current.equals(view)) return;
        if (view.equals(View.SIGNIN)) {
            cleanup();
        }

        Parent root;
//        CacheKey key = CacheKey.of(view, user, input)

//        if (viewCacher.contains(key)) {
//            System.out.println(view + ": loading from cache");
//            root = viewCacher.get(key);
//        } else {
//            System.out.println(view + ": loading from loader");
            root = viewLoader.load(view, user, input);
//        }

//        cacheup(key, root);
        showup(view, root);
    }

    private void cacheup(CacheKey key, Parent root) {
        viewCacher.cache(key, root);

        setCurrent(key.getView());
    }

    private void showup(View view, Parent root) {
        stage.getScene().setRoot(root);

        stage.setTitle(view.getTitle());
        stage.show();
    }

    private void cleanup() {
        setCurrent(View.getEmpty());
        viewCacher.cleanup();
    }
}
