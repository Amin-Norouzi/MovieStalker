package com.aminnorouzi.ms.core;

import com.aminnorouzi.ms.tool.view.View;
import com.aminnorouzi.ms.tool.view.ViewSwitcher;
import javafx.stage.Stage;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Data
@Component
@RequiredArgsConstructor
public class PrimaryStageInitializer implements ApplicationListener<StageReadyEvent> {

    private final ViewSwitcher switcher;

    @Override
    public void onApplicationEvent(@NotNull StageReadyEvent event) {
        View view = View.getDefault();
        Stage stage = event.getDefault(event);

        switcher.initialize(stage);
        switcher.switchTo(view, null,null);
    }
}
