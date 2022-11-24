package com.aminnorouzi.ms;

import com.aminnorouzi.ms.MovieStalkerApplication.MovieStalkerIntegrationApplication.StageReadyEvent;
import com.aminnorouzi.ms.model.View;
import com.aminnorouzi.ms.model.user.User;
import com.aminnorouzi.ms.util.ViewSwitcher;
import javafx.stage.Stage;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Data
@Component
@RequiredArgsConstructor
public class MovieStalkerInitializer implements ApplicationListener<StageReadyEvent> {

    private final FxWeaver fxWeaver;
    private final ViewSwitcher switcher;

    @Override
    public void onApplicationEvent(StageReadyEvent event) {
        View view = View.getDefault();
        Stage stage = getCustomizedStage(event);

        switcher.initialize(stage);
        switcher.switchTo(view, new User());
    }

    private Stage getCustomizedStage(StageReadyEvent event) {
        Stage stage = event.getStage();
        stage.centerOnScreen();
        stage.setResizable(false);

        return stage;
    }
}
