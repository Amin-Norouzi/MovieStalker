package com.aminnorouzi.ms;

import com.aminnorouzi.ms.core.ApplicationInitializer;
import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableFeignClients
@SpringBootApplication
public class MovieStalkerApplication {

    public static void main(String[] args) {
        Application.launch(ApplicationInitializer.class, args);
    }
}
