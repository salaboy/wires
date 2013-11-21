package org.kie.wires.client.shapes;

import java.util.List;
import org.kie.wires.client.util.collision.Projection;
import org.kie.wires.client.util.collision.Vector;

public interface EditableShape {
    
    String getId();
    
    void init(double x, double y);

    void showDragPoints();
    
    void showMagnetsPoints();

    void hideDragPoints();
    
    void hideMagnetPoints();
    
    boolean collidesWith(EditableShape shape);
    
    boolean separationOnAxes(List<Vector> axes, EditableShape shape);
    
    List<Vector> getAxes();
    
    Projection project(Vector axis);
    
    boolean isBeingDragged();
    
    double getX();
    
    double getY();
    
    double getCurrentDragX();
    
    double getCurrentDragY();
    
    
}
