package com.aminnorouzi.ms;

import com.aminnorouzi.ms.model.User;
import com.aminnorouzi.ms.repository.UserRepository;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

@AllArgsConstructor
@SpringBootApplication
public class MovieStalkerApplication {

    private final UserRepository repository;

    public static void main(String[] args) {
        Application.launch(MovieStalkerIntegrationApplication.class, args);
    }

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

    @Bean
    CommandLineRunner commandLineRunner() {
        return args -> {
            repository.save(User.builder()
                    .username("admin")
                    .password("1234")
                    .build());

            repository.save(User.builder()
                    .username("user")
                    .password("1233")
                    .build());
        };
    }
}
