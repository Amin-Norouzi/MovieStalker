package com.aminnorouzi.ms.function;

import com.aminnorouzi.ms.controller.Controller;
import com.aminnorouzi.ms.model.movie.Movie;
import com.aminnorouzi.ms.node.MovieNode;
import javafx.scene.Node;

import java.util.function.BiFunction;

public class MovieFunction implements BiFunction<Controller, Object, Node> {

    @Override
    public Node apply(Controller controller, Object value) {
        return new MovieNode(controller, (Movie) value);
    }
}
