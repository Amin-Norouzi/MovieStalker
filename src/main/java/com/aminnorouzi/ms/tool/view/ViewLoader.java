package com.aminnorouzi.ms.tool.view;

import com.aminnorouzi.ms.exception.IllegalViewException;
import javafx.fxml.FXMLLoader;
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

    public Parent load(View view) {
        try {
            return fxWeaver.loadView(view.getController());
        } catch (Exception exception) {
            throw new IllegalViewException("Could not load view: " + view.getTitle());
        }
    }

    public Parent load(String path) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(path));

            return fxmlLoader.load();
        } catch (Exception exception) {
            throw new IllegalViewException("Could not load path: " + path);
        }
    }
}
