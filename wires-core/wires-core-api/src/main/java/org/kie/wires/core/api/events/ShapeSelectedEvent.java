/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.kie.wires.core.api.events;

import java.io.Serializable;

import org.kie.wires.core.api.shapes.WiresBaseGroupShape;


public class ShapeSelectedEvent implements Serializable {
    private static final long serialVersionUID = 6413916607473536300L;
    private WiresBaseGroupShape shape;

    public ShapeSelectedEvent() {
    }

    public ShapeSelectedEvent(WiresBaseGroupShape shape) {
        this.shape = shape;
    }

    public WiresBaseGroupShape getShape() {
        return shape;
    }
    
}
