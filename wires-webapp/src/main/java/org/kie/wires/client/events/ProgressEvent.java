package org.kie.wires.client.events;

import java.util.List;

import com.emitrom.lienzo.client.core.shape.Shape;

public class ProgressEvent {
    
    
    List<Shape<?>> shapes;
    
    public ProgressEvent(List<Shape<?>> shapes){
        this.shapes = shapes;
    }

    public List<Shape<?>> getShapes() {
        return shapes;
    }

    public void setShapes(List<Shape<?>> shapes) {
        this.shapes = shapes;
    }

    

}
