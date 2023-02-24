package com.aminnorouzi.ms.function;

import com.aminnorouzi.ms.controller.Controller;
import com.aminnorouzi.ms.node.GenreNode;
import javafx.scene.Node;

import java.util.function.BiFunction;

public class GenreFunction implements BiFunction<Controller, Object, Node> {

    @Override
    public Node apply(Controller controller, Object value) {
        return new GenreNode(controller, (String) value);
    }
}
