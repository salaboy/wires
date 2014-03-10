/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.kie.wires.client.events;

import org.kie.wires.client.shapes.api.WiresBaseGroupShape;

/**
 *
 * @author salaboy
 */
public class ShapeSelectedEvent {
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
