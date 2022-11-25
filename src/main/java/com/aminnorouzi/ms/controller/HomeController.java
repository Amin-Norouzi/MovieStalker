package com.aminnorouzi.ms.controller;

import com.aminnorouzi.ms.service.MovieService;
import com.aminnorouzi.ms.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Slf4j
@Component
@FxmlView("/view/home-view.fxml")
public class HomeController extends Controller {

    private final MovieService movieService;
    private final UserService userService;

    @Override
    protected void configure() {

    }
}
