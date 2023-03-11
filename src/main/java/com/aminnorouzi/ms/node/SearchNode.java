package com.aminnorouzi.ms.node;

import com.aminnorouzi.ms.controller.Controller;
import com.aminnorouzi.ms.model.movie.Movie;
import com.aminnorouzi.ms.model.movie.Search;
import com.aminnorouzi.ms.tool.image.Info;
import com.aminnorouzi.ms.tool.view.View;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import lombok.Setter;

import java.net.URL;
import java.util.ResourceBundle;

@Setter
public class SearchNode extends StackPane implements Loadable {

    private final Controller controller;
    private final Search search;

    private Movie movie;

    @FXML
    private Rectangle posterPic;
    @FXML
    private Label titleLabel;
    @FXML
    private Label yearLabel;

    public SearchNode(Controller controller, Search search) {
        this.controller = controller;
        this.search = search;

        load(this);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        controller.getImage().load(search.getPoster(), Info.SEARCH_NODE_POSTER, posterPic);

        titleLabel.setText(search.getTitle());
        yearLabel.setText(String.valueOf(search.getReleased().getYear()));
    }

    @Override
    public String getPath() {
        return "/templates/node/search-node.fxml";
    }

    @FXML
    private void onMovie(ActionEvent event) {
        setMovie(controller.getLibrary().find(controller.getUser(), search));
        controller.switchTo(View.MOVIE, movie);
    }
}