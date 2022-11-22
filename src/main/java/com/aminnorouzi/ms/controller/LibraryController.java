package com.aminnorouzi.ms.controller;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Slf4j
@Component
@FxmlView("/view/library-view.fxml")
public class LibraryController extends Controller {

    @Override
    protected void configure() {
        System.out.println("Running LibraryController");

        System.out.println("-------------------- my user --------------------");
        System.out.println(getUser().toString());
    }
}
