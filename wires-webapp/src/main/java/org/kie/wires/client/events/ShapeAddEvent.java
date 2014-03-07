package org.kie.wires.client.events;

import com.emitrom.lienzo.client.core.shape.Shape;
import org.kie.wires.client.shapes.api.WiresBaseGroupShape;


public class ShapeAddEvent {
    private WiresBaseGroupShape shape;

    private int x;
    private int y;

    public ShapeAddEvent() {

    }

    public ShapeAddEvent(WiresBaseGroupShape shape, int x, int y) {
        this.shape = shape;
        this.x = x;
        this.y = y;
    }

    public WiresBaseGroupShape getShape() {
        return shape;
    }

    public void setShape(WiresBaseGroupShape shape) {
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
