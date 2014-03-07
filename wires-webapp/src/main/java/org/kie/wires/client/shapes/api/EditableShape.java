package org.kie.wires.client.shapes.api;


public interface EditableShape {

    String getId();

    void init(double x, double y);

    void showControlPoints();

    void hideControlPoints();

    boolean isBeingDragged();

    boolean isBeingResized();

    double getX();

    double getY();
    
    

}
