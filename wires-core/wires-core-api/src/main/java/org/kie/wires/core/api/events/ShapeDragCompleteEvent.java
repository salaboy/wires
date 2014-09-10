package org.kie.wires.core.api.events;

import java.io.Serializable;


public class ShapeDragCompleteEvent implements Serializable {
    private static final long serialVersionUID = 1854461682099520899L;

    private String shape;

    private int x;
    private int y;

    public ShapeDragCompleteEvent() {

    }

    public ShapeDragCompleteEvent( String shape,
                                   int x,
                                   int y ) {
        this.shape = shape;
        this.x = x;
        this.y = y;
    }

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
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
