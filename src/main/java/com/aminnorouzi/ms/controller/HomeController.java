package com.aminnorouzi.ms.controller;

import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@FxmlView("/view/home-view.fxml")
public class HomeController extends Controller {

    @Override
    protected void configure() {
//        System.out.println(getClass().getSimpleName());
    }

    //   @FXML
//   public void initialize() {
//      log.error("Initialization");
//
//      FXMLLoader fxmlLoader = new FXMLLoader(HomeController.class
//              .getResource("/view/sidebar-view.fxml"));
//      VBox vBox;
//      try {
//         vBox = fxmlLoader.load();
//      } catch (IOException e) {
//         throw new RuntimeException(e);
//      }
//      root.setLeft(vBox);
//
//      Button homeButton = (Button) vBox.getChildren().get(1);
//      homeButton.styleProperty().set("-fx-border-color: blue;");
//
//      LibraryController controller = new LibraryController();
//
//      Button libraryButton = (Button) vBox.getChildren().get(2);
//      libraryButton.setOnAction(e -> ViewSwitcher.switchTo(View.LIBRARY, controller));
//
//      root.setCenter(new Label("Home"));
//
//      service.doSomething();
//   }
}
