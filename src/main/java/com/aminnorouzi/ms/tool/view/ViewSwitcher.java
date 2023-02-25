package com.aminnorouzi.ms.tool.view;

import com.aminnorouzi.ms.model.movie.Movie;
import com.aminnorouzi.ms.model.user.User;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lombok.*;
import org.springframework.stereotype.Component;

@Data
@Component
@RequiredArgsConstructor
@Getter(AccessLevel.PROTECTED)
@Setter(AccessLevel.PROTECTED)
public class ViewSwitcher {

    private final ViewLoader loader;
    private final ViewCacher cacher;

    private View current;
    private View previous;

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

        if (view.equals(View.PREVIOUS)) {
            view = getPrevious();
        }

        CacheKey key = CacheKey.of(view, user, input);
        Parent root = load(key, user);

        cacheup(key, root);
        showup(view, root, input);
    }

    private Parent load(CacheKey key, User user) {
        if (cacher.contains(key)) {
            return cacher.get(key);
        }

        return loader.load(key.getView(), user, key.getInput());
    }

    private void cacheup(CacheKey key, Parent root) {
        cacher.cache(key, root);

        setPrevious(getCurrent());
        setCurrent(key.getView());
    }

    private void showup(View view, Parent root, Object input) {
        stage.getScene().setRoot(root);

        if (view.equals(View.MOVIE) && input instanceof Movie m) {
            stage.setTitle(view.getTitle() + " - " + m.getTitle());
        } else {
            stage.setTitle(view.getTitle());
        }

        stage.show();
    }

    private void cleanup() {
        setCurrent(View.getEmpty());
        cacher.cleanup();
    }
}
