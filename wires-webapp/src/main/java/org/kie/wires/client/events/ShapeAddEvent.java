package org.kie.wires.client.events;

import com.emitrom.lienzo.client.core.shape.Shape;


public class ShapeAddEvent {
    private Shape shape;

    private int x;
    private int y;

    public ShapeAddEvent() {

    }

    public ShapeAddEvent(Shape shape, int x, int y) {
        this.shape = shape;
        this.x = x;
        this.y = y;
    }

    public Shape getShape() {
        return shape;
    }

    public void setShape(Shape shape) {
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
