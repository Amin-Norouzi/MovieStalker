package com.aminnorouzi.ms;

import com.aminnorouzi.ms.controller.HomeController;
import com.aminnorouzi.ms.util.View;
import com.aminnorouzi.ms.util.ViewSwitcher;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MovieStalkerApplication extends Application {

//    @Override
//    public void start(Stage stage) throws IOException {
//        Scene scene = new Scene(new BorderPane());
//
//        HomeController controller = new HomeController();
//        controller.init();
//
//        ViewSwitcher.init(stage, scene);
//        ViewSwitcher.switchTo(View.HOME, controller);
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//
//    @Override
//    public void init() {
//        SpringApplication.run(getClass()).getAutowireCapableBeanFactory().autowireBean(this);
//    }

    private ConfigurableApplicationContext applicationContext;

    @Override
    public void init() {
        applicationContext = new SpringApplicationBuilder(MySpringBootApplication.class).run();
    }

    @Override
    public void start(Stage stage) {
        applicationContext.publishEvent(new StageReadyEvent(stage));
    }

    @Override
    public void stop() {
        applicationContext.close();
        Platform.exit();
    }

    public static class StageReadyEvent extends ApplicationEvent {
        public StageReadyEvent(Stage stage) {
            super(stage);
        }

        public Stage getStage() {
            return ((Stage) getSource());
        }
    }
}
