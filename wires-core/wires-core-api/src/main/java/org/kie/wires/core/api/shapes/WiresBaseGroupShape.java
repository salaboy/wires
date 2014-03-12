/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kie.wires.core.api.shapes;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.event.Event;

import org.kie.wires.core.api.collision.CollidableShape;
import org.kie.wires.core.api.collision.ControlPoint;
import org.kie.wires.core.api.collision.Magnet;
import org.kie.wires.core.api.collision.StickableShape;
import org.kie.wires.core.api.events.ShapeSelectedEvent;

import com.emitrom.lienzo.client.core.shape.Group;
import com.emitrom.lienzo.client.core.shape.Shape;


public abstract class WiresBaseGroupShape extends Group implements EditableShape, CollidableShape, StickableShape {

    private List<ControlPoint> controlPoints = new ArrayList<ControlPoint>();
    private List<Magnet> magnets = new ArrayList<Magnet>();
    private Event<ShapeSelectedEvent> selected;

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

    public Event<ShapeSelectedEvent> getSelected() {
        return selected;
    }

    public void setSelected(Event<ShapeSelectedEvent> selected) {
        this.selected = selected;
    }
    
    
    

}
