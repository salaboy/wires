package org.kie.wires.client.shapes;

public interface EditableShape {
    
    void init(double x, double y);

    void showDragPoints();
    
    void showMagnetsPoints();

    void hideDragPoints();
    
    void hideMagnetPoints();
}
