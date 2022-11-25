package com.aminnorouzi.ms.controller;

import com.aminnorouzi.ms.model.input.Result;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Slf4j
@Component
@FxmlView("/view/result-view.fxml")
public class ResultController extends Controller {

    @FXML
    private TextArea foundArea;

    @FXML
    private TextArea failedArea;

    @Override
    protected void configure() {
        Result result = (Result) getInput();

        result.getFound()
                .forEach(found -> foundArea.appendText(found.toString() + "\n"));

        result.getFailed()
                .forEach(failed -> failedArea.appendText(failed.toString()+ "\n"));
    }
}
