package com.aminnorouzi.ms.controller;

import com.aminnorouzi.ms.model.user.User;
import com.aminnorouzi.ms.service.MovieService;
import com.aminnorouzi.ms.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Component
@FxmlView("/view/home-view.fxml")
public class HomeController extends Controller {

    private final MovieService movieService;
    private final UserService userService;

    @Override
    protected void configure() {
        System.out.println("Running HomeController");
//        System.out.println(getClass().getSimpleName());
//        movieService.doSomething();
        List<User> users = userService.getAll();
        users.forEach(user -> System.out.println(user));

        System.out.println("-------------------- my user --------------------");
        System.out.println(getUser().toString());
    }
}
