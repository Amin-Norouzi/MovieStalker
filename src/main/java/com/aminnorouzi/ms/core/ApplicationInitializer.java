package com.aminnorouzi.ms.core;

import com.aminnorouzi.ms.MovieStalkerApplication.MovieStalkerIntegrationApplication.StageReadyEvent;
import com.aminnorouzi.ms.util.view.View;
import com.aminnorouzi.ms.util.view.ViewSwitcher;
import javafx.stage.Stage;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Data
@Component
@RequiredArgsConstructor
public class ApplicationInitializer implements ApplicationListener<StageReadyEvent> {

    private final ViewSwitcher switcher;

    @Override
    public void onApplicationEvent(@NotNull StageReadyEvent event) {
        View view = View.getDefault();
        Stage stage = event.getDefault(event);

        switcher.initialize(stage);
        switcher.switchTo(view, null);
    }
}
