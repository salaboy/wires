package org.kie.wires.client.events;


public class ShapeAddEvent {
    private String shape;

    private int x;
    private int y;

    public ShapeAddEvent() {

    }

    public ShapeAddEvent(String shape, int x, int y) {
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
