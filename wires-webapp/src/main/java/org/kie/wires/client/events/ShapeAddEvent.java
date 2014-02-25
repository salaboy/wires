package org.kie.wires.client.events;

import com.emitrom.lienzo.client.core.shape.Shape;
import org.kie.wires.client.shapes.BaseGroupShape;


public class ShapeAddEvent {
    private BaseGroupShape shape;

    private int x;
    private int y;

    public ShapeAddEvent() {

    }

    public ShapeAddEvent(BaseGroupShape shape, int x, int y) {
        this.shape = shape;
        this.x = x;
        this.y = y;
    }

    public BaseGroupShape getShape() {
        return shape;
    }

    public void setShape(BaseGroupShape shape) {
        this.shape = shape;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
