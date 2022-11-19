package com.aminnorouzi.ms.controller;

import com.aminnorouzi.ms.util.ViewSwitcher;
import javafx.fxml.FXML;
import lombok.*;
import org.springframework.stereotype.Component;

@Data
@Component
@AllArgsConstructor
public abstract class Controller {

    @FXML
    public void initialize() {
        configure();
    }

    protected abstract void configure();
}
