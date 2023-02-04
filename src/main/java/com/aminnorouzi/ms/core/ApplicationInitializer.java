package com.aminnorouzi.ms.core;

import com.aminnorouzi.ms.MovieStalkerApplication;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.ArrayList;
import java.util.List;

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
        List<String> fonts = getFonts();
        for (String font : fonts) {
            Font.loadFont(ApplicationInitializer.class
                    .getResourceAsStream("/templates/font/" + font), 52);
        }
    }

    public static List<String> getFonts() {
        List<String> fonts = new ArrayList<>();
        fonts.add("Poppins-Bold.ttf");
        fonts.add("Poppins-Medium.ttf");
        fonts.add("Poppins-Regular.ttf");
        fonts.add("Poppins-SemiBold.ttf");

        return fonts;
    }
}