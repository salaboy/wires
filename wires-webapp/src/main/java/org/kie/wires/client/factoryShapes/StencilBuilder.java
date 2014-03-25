package org.kie.wires.client.factoryShapes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.event.Event;

import org.kie.wires.core.api.events.ShapeAddEvent;
import org.kie.wires.core.client.shapes.PaletteShape;
import org.kie.wires.core.client.util.ShapeCategory;
import org.kie.wires.core.client.util.ShapeType;

import com.emitrom.lienzo.client.widget.LienzoPanel;
import com.google.gwt.user.client.ui.Composite;

public class StencilBuilder extends Composite {

    public static Map<ShapeCategory, Integer> shapesByCategory;

    public StencilBuilder() {

    }

    public StencilBuilder(Event<ShapeAddEvent> shapeAddEvent, ShapeCategory shapeCategory, LienzoPanel panel,
            List<PaletteShape> listShapes) {
        shapesByCategory = new HashMap<ShapeCategory, Integer>();
        for (ShapeType shapeType : ShapeType.values()) {
            if (shapeType.getCategory().equals(shapeCategory)) {
                this.newShape(shapeType, panel, shapeAddEvent, listShapes);
            }
        }

    }

    public void newShape(final ShapeType shapeType, LienzoPanel panel, Event<ShapeAddEvent> shapeAddEvent,
            List<PaletteShape> listShapes) {
        this.setShapesByCategory(shapeType);

        switch (shapeType) {
        case LINE:
            new LineFactory(panel, shapeAddEvent, shapesByCategory, listShapes);
            break;
        case RECTANGLE:
            new RectangleFactory(panel, shapeAddEvent, shapesByCategory, listShapes);
            break;
        // case CIRCLE:
        // new CircleFactory(panel, shapeAddEvent, shapesByCategory);
        // break;
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