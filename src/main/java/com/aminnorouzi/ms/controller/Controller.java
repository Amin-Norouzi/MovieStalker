package com.aminnorouzi.ms.controller;

import com.aminnorouzi.ms.model.user.User;
import javafx.fxml.FXML;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@Component
@NoArgsConstructor
@AllArgsConstructor
public abstract class Controller {

    private User user;

    @FXML
    public void initialize() {
        configure();
    }

    protected abstract void configure();
}
