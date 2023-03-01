package com.aminnorouzi.ms.function;

import com.aminnorouzi.ms.controller.Controller;
import com.aminnorouzi.ms.node.CategoryNode;
import javafx.scene.Node;

import java.util.function.BiFunction;

public class CategoryFunction implements BiFunction<Controller, Object, Node> {

    @Override
    public Node apply(Controller controller, Object value) {
        return new CategoryNode(controller, (String) value);
    }
}
