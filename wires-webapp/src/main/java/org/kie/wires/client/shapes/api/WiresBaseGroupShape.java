/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kie.wires.client.shapes.api;

import org.kie.wires.client.shapes.api.EditableShape;
import com.emitrom.lienzo.client.core.shape.Group;
import com.emitrom.lienzo.client.core.shape.Shape;
import java.util.ArrayList;
import java.util.List;
import org.kie.wires.client.shapes.collision.api.CollidableShape;
import org.kie.wires.client.shapes.collision.api.ControlPoint;
import org.kie.wires.client.shapes.collision.api.Magnet;
import org.kie.wires.client.shapes.collision.api.StickableShape;

/**
 *
 * @author salaboy
 */
public abstract class WiresBaseGroupShape extends Group implements EditableShape, CollidableShape, StickableShape {

    private List<ControlPoint> controlPoints = new ArrayList<ControlPoint>();
    private List<Magnet> magnets = new ArrayList<Magnet>();
    

    public WiresBaseGroupShape() {
    }

    public void addControlPoint(ControlPoint cp) {
        controlPoints.add(cp);
    }

    public void addMagnet(Magnet m) {
        magnets.add(m);
    }

    public List<ControlPoint> getControlPoints() {
        return controlPoints;
    }

    public List<Magnet> getMagnets() {
        return magnets;
    }
    
    public void init(){
        
        setDraggable(true);
        for(ControlPoint cp : controlPoints){
            add((Shape)cp);
        }
        for(Magnet m : magnets){
            add((Shape)m);
        }
        
    }
    

}
