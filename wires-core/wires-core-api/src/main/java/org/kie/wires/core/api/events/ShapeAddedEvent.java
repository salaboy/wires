package org.kie.wires.core.api.events;

import java.io.Serializable;

public class ShapeAddedEvent implements Serializable {

    private static final long serialVersionUID = -1688472223390244923L;
    private String shape;

    public ShapeAddedEvent() {

    }

    public ShapeAddedEvent( final String shape ) {
        this.shape = shape;
    }

    public String getShape() {
        return shape;
    }

}
