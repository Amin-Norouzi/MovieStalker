package com.aminnorouzi.ms.function;

import com.aminnorouzi.ms.controller.Controller;
import com.aminnorouzi.ms.model.movie.Search;
import com.aminnorouzi.ms.node.SearchNode;
import javafx.scene.Node;
import lombok.RequiredArgsConstructor;

import java.util.function.BiFunction;

@RequiredArgsConstructor
public class SearchFunction implements BiFunction<Controller, Object, Node> {

    @Override
    public Node apply(Controller controller, Object value) {
        return new SearchNode(controller, (Search) value);
    }
}