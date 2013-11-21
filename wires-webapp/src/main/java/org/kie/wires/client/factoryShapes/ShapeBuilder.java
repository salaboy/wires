package org.kie.wires.client.factoryShapes;

import javax.enterprise.event.Event;

import org.kie.wires.client.events.ShapeAddEvent;

import com.emitrom.lienzo.client.core.shape.Group;
import com.emitrom.lienzo.client.widget.LienzoPanel;
import com.google.gwt.user.client.ui.Composite;

public class ShapeBuilder extends Composite {
    
    
    public ShapeBuilder(){
    }
    
    public void newShape(Group group, final ShapeType shapeType, LienzoPanel panel, Event<ShapeAddEvent> shapeAddEvent){
        switch(shapeType){
        case LINE:
            new LineFactory(group, panel, shapeAddEvent);
            break;
        case RECTANGLE:
        	new RectangleFactory(group, panel, shapeAddEvent);
        	break;
        case CIRCLE:
        	//TODO
        	break;	
        default:
        	throw new IllegalStateException("Unrecognized shape type '" + shapeType + "'!");
        }
        
    }
    

}
