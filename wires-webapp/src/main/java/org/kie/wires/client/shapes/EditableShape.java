package org.kie.wires.client.shapes;

import com.emitrom.lienzo.client.core.shape.Layer;

public interface EditableShape {

    String getId();

    void init(double x, double y, Layer layer);

    void showControlPoints();

    void hideControlPoints();

    boolean isBeingDragged();

    boolean isBeingResized();

    double getX();

    double getY();
    
    

}
