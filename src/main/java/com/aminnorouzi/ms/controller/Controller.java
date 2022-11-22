package com.aminnorouzi.ms.controller;

import com.aminnorouzi.ms.model.user.User;
import com.aminnorouzi.ms.util.ViewSwitcher;
import javafx.fxml.FXML;
import lombok.*;
import net.bytebuddy.implementation.bind.annotation.IgnoreForBinding;
import org.springframework.data.annotation.Transient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.InitBinder;

import javax.persistence.ManyToOne;

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
