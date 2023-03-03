package com.aminnorouzi.ms.core;

import com.aminnorouzi.ms.MovieStalkerApplication;
import com.aminnorouzi.ms.model.user.User;
import com.aminnorouzi.ms.service.ActivityService;
import com.aminnorouzi.ms.tool.view.View;
import com.aminnorouzi.ms.tool.view.ViewSwitcher;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.List;
import java.util.Objects;

public class ApplicationInitializer extends Application {

    private ConfigurableApplicationContext context;

    @Override
    public void start(Stage stage) {
        context.publishEvent(new StageReadyEvent(stage));
    }

    @Override
    public void init() {
        context = new SpringApplicationBuilder(MovieStalkerApplication.class).run();
        loadFonts();
    }

    @Override
    public void stop() {
        context.close();
        Platform.exit();
    }

    public static void loadFonts() {
        List<String> fonts = List.of("Poppins-Bold.ttf", "Poppins-Medium.ttf", "Poppins-Regular.ttf",
                "Poppins-SemiBold.ttf");
        for (String font : fonts) {
            Font.loadFont(ApplicationInitializer.class
                    .getResourceAsStream("/templates/font/" + font), 52);
        }
    }

    @Component
    @RequiredArgsConstructor
    public static class PrimaryStageInitializer implements ApplicationListener<StageReadyEvent> {

        private final ActivityService activity;
        private final ViewSwitcher switcher;

        @Override
        public void onApplicationEvent(StageReadyEvent event) {
            Stage stage = event.getDefault(event);

            stage.getIcons().add(new Image(Objects.requireNonNull(PrimaryStageInitializer.class
                    .getResourceAsStream("/templates/image/application-icon.png"))));

            if (Taskbar.isTaskbarSupported()) {
                var taskbar = Taskbar.getTaskbar();

                if (taskbar.isSupported(Taskbar.Feature.ICON_IMAGE)) {
                    final Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
                    var dockIcon = defaultToolkit.getImage(getClass().
                            getResource("/templates/image/application-icon.png"));
                    taskbar.setIconImage(dockIcon);
                }

            }

            switcher.initialize(stage);

            if (activity.exists()) {
                try {
                    User user = activity.remember();
                    switcher.switchTo(View.HOME, user, null);

                    return;
                } catch (Exception ignored) {}
            }

            switcher.switchTo(View.getDefault(), null, null);
        }
    }
}