package org.kie.wires.core.api.events;

import java.io.Serializable;

public class ReadyShape implements Serializable {

    private static final long serialVersionUID = -1688472223390244923L;
    private String shape;

    public ReadyShape() {

    }

    public ReadyShape(String shape) {
        this.shape = shape;

    }

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

}
