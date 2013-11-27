package org.kie.wires.client.factoryShapes;

import java.util.Map;

import javax.enterprise.event.Event;

import org.kie.wires.client.events.ShapeAddEvent;

import com.emitrom.lienzo.client.core.shape.Group;
import com.emitrom.lienzo.client.widget.LienzoPanel;
import com.google.gwt.user.client.ui.Composite;

public class ShapeBuilder extends Composite {

    public static Map<Integer, Integer> shapesByCategory;

    public ShapeBuilder() {
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
        for (Map.Entry<Integer, Integer> entry : shapesByCategory.entrySet()) {
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