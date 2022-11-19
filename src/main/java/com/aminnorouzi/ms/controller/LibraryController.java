package com.aminnorouzi.ms.controller;

import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@FxmlView("/view/library-view.fxml")
public class LibraryController extends Controller {

    @Override
    protected void configure() {
//        System.out.println(getClass().getSimpleName());
    }

//    @Override
//    public void initialize(URL url, ResourceBundle resourceBundle) {
//        FXMLLoader fxmlLoader = new FXMLLoader(LibraryController.class
//                .getResource("/view/sidebar-view.fxml"));
//        VBox vBox;
//        try {
//            vBox = fxmlLoader.load();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        root.setLeft(vBox);
//
//        Button libraryButton = (Button) vBox.getChildren().get(2);
//        libraryButton.styleProperty().set("-fx-border-color: blue;");
//
//        HomeController controller = new HomeController();
//
//        Button homeButton = (Button) vBox.getChildren().get(1);
//        homeButton.setOnAction(e -> ViewSwitcher.switchTo(View.HOME, controller));
//
//        service.doSomething();
//    }
}
