package com.aminnorouzi.ms.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Slf4j
@Component
@FxmlView("/view/header-view.fxml")
public class HeaderController extends Controller {

    @Override
    protected void configure() {

    }
}
