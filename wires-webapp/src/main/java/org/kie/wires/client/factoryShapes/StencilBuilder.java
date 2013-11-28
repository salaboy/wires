package org.kie.wires.client.factoryShapes;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.event.Event;

import org.kie.wires.client.events.ShapeAddEvent;

import com.emitrom.lienzo.client.core.shape.Group;
import com.emitrom.lienzo.client.core.shape.Layer;
import com.emitrom.lienzo.client.widget.LienzoPanel;
import com.google.gwt.user.client.ui.Composite;

public class StencilBuilder extends Composite {

    private static final int X_ACCORDION = 0;
    private static final int Y_ACCORDION = 5;
    
    public static Map<ShapeCategory, Integer> shapesByCategory;
    
    public StencilBuilder() {
        
    }

    public StencilBuilder(Event<ShapeAddEvent> shapeAddEvent, ShapeCategory shapeCategory) {
        shapesByCategory = new HashMap<ShapeCategory, Integer>();
        LienzoPanel panel = new LienzoPanel(ShapeFactoryUtil.WIDTH_PANEL, ShapeFactoryUtil.HEIGHT_PANEL);
        super.initWidget(panel);
        Layer layer = new Layer();
        panel.add(layer);
        Group group = new Group();
        group.setX(X_ACCORDION).setY(Y_ACCORDION);
        layer.add(group);
        for(ShapeType shapeType : ShapeType.values()){
            if(shapeType.getCategory().equals(shapeCategory)){
                this.newShape(group, shapeType, panel, shapeAddEvent);
            }
        }
        layer.draw();
        
    }

    public void newShape(Group group, final ShapeType shapeType, LienzoPanel panel, Event<ShapeAddEvent> shapeAddEvent) {
        this.setShapesByCategory(shapeType);
        switch (shapeType) {
        case LINE:
            new LineFactory(group, panel, shapeAddEvent, shapesByCategory);
            break;
        case RECTANGLE:
            new RectangleFactory(group, panel, shapeAddEvent, shapesByCategory);
            break;
        case CIRCLE:
            new CircleFactory(group, panel, shapeAddEvent, shapesByCategory);
            break;
        default:
            throw new IllegalStateException("Unrecognized shape type '" + shapeType + "'!");
        }

    }

    public void setShapesByCategory(ShapeType shapeType) {
        boolean exist = false;
        for (Map.Entry<ShapeCategory, Integer> entry : shapesByCategory.entrySet()) {
            if (entry.getKey().equals(shapeType.getCategory())) {
                exist = true;
                shapesByCategory.put(entry.getKey(), shapesByCategory.get(entry.getKey()) + 1);
                break;
            }
        }
        if (!exist) {
            shapesByCategory.put(shapeType.getCategory(), 1);
        }
    }
    

}