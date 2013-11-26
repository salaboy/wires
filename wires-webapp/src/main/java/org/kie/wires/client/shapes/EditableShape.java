package org.kie.wires.client.shapes;

import com.emitrom.lienzo.client.core.shape.Shape;
import java.util.List;

public interface EditableShape {
    
    String getId();
    
    void init(double x, double y);

    void showDragPoints();
    
    void showMagnetsPoints();
    
    List<Shape> getMagnets();

    void hideDragPoints();
    
    void hideMagnetPoints();
    
    boolean isBeingDragged();
    
    boolean isBeingResized();
    
    double getX();
    
    double getY();

}
