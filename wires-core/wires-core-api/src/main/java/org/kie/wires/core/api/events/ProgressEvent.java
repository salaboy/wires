package org.kie.wires.core.api.events;

import java.io.Serializable;
import java.util.List;

import com.emitrom.lienzo.client.core.shape.Shape;

public class ProgressEvent implements Serializable {
    
    
    private static final long serialVersionUID = 910177908332108118L;
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
