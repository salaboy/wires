package org.kie.wires.client.factoryShapes;

import java.util.Map;

import javax.enterprise.event.Event;

import org.kie.wires.client.events.ShapeAddEvent;

import com.emitrom.lienzo.client.core.event.NodeMouseDownHandler;
import com.emitrom.lienzo.client.core.shape.Circle;
import com.emitrom.lienzo.client.core.shape.Group;
import com.emitrom.lienzo.client.core.shape.Rectangle;
import com.emitrom.lienzo.client.core.shape.Shape;
import com.emitrom.lienzo.client.widget.LienzoPanel;

public class CircleFactory extends ShapeFactory<Circle> {
    
    private static String DESCRIPTION = "Circle";
    
    
    public CircleFactory(){
    }
    
    public CircleFactory(Group group, LienzoPanel panel, Event<ShapeAddEvent> shapeAddEvent
            , Map<Integer, Integer> shapesByCategory){
        super(panel, shapeAddEvent);
        this.drawBoundingBox(group);
    }

    @Override
    protected void drawBoundingBox(Group group) {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected Shape<Circle> drawShape() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected void addShapeHandlers(Shape<Circle> shape, Group group) {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void addBoundingHandlers(Rectangle boundingBox, Group group) {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected NodeMouseDownHandler getNodeMouseDownEvent(Group group) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected int getCategory() {
        // TODO Auto-generated method stub
        return ShapeType.CIRCLE.getCategory();
    }
    

    

   

    
    

   

}
