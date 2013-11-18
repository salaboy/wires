package org.kie.wires.client.factoryShapes;

import com.emitrom.lienzo.client.core.shape.Group;
import com.emitrom.lienzo.client.core.shape.Rectangle;
import com.emitrom.lienzo.client.core.shape.Shape;

public class RectangleFactory extends ShapeFactory<Rectangle> {

    private static String description = "Rectangle";

    public RectangleFactory() {

    }

    @Override
    public void drawBoundingBox(Group group) {
        // TODO Auto-generated method stub

    }

    @Override
    public Shape<Rectangle> drawShape() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void createDescription(Group group) {
        // TODO Auto-generated method stub

    }

    @Override
    public void addHandlers(Shape<Rectangle> shape, Group group) {
        // TODO Auto-generated method stub

    }

}
