package org.kie.wires.client.shapes;

public interface EditableShape {
    
    String getId();
    
    void init(double x, double y);

    void showDragPoints();
    
    void showMagnetsPoints();

    void hideDragPoints();
    
    void hideMagnetPoints();
    
    boolean isBeingDragged();
    
    boolean isBeingResized();
    
    double getX();
    
    double getY();

}
