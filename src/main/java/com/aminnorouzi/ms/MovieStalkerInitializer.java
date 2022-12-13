package com.aminnorouzi.ms;

import com.aminnorouzi.ms.MovieStalkerApplication.MovieStalkerIntegrationApplication.StageReadyEvent;
import com.aminnorouzi.ms.configuration.ApplicationConfiguration;
import com.aminnorouzi.ms.model.View;
import com.aminnorouzi.ms.util.ViewSwitcher;
import javafx.stage.Stage;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Data
@Component
@RequiredArgsConstructor
public class MovieStalkerInitializer implements ApplicationListener<StageReadyEvent> {

    private final ApplicationConfiguration configuration;
    private final ViewSwitcher switcher;

    @Override
    public void onApplicationEvent(@NotNull StageReadyEvent event) {
        View view = View.getDefault();
        Stage stage = getDefaultStage(event);

        switcher.initialize(stage);
        switcher.switchTo(view);
    }

    private Stage getDefaultStage(StageReadyEvent event) {
        Stage stage = event.getStage();
        stage.centerOnScreen();
        stage.setResizable(false);

        return stage;
    }
}
