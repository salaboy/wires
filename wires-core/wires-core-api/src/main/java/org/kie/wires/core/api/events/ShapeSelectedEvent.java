/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.kie.wires.core.api.events;

import java.io.Serializable;

import org.kie.wires.core.api.shapes.WiresBaseDynamicShape;

public class ShapeSelectedEvent implements Serializable {
    private static final long serialVersionUID = 6413916607473536300L;
    private WiresBaseDynamicShape shape;

    public ShapeSelectedEvent() {
    }

    public ShapeSelectedEvent(WiresBaseDynamicShape shape) {
        this.shape = shape;
    }

    public WiresBaseDynamicShape getShape() {
        return shape;
    }
    
}
