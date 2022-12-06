package com.aminnorouzi.ms;

import com.aminnorouzi.ms.service.TaskService;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

@EnableScheduling
@EnableJpaAuditing
@EnableFeignClients
@SpringBootApplication
public class MovieStalkerApplication {


    public static class MovieStalkerIntegrationApplication extends Application {

        private ConfigurableApplicationContext context;

        @Override
        public void start(Stage stage) {
            context.publishEvent(new StageReadyEvent(stage));
        }

        @Override
        public void init() {
            context = new SpringApplicationBuilder(MovieStalkerApplication.class).run();
        }

        @Override
        public void stop() {
            context.close();
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

    public static void main(String[] args) {
        Application.launch(MovieStalkerIntegrationApplication.class, args);
    }
}
