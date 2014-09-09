package org.kie.wires.client.factoryShapes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.enterprise.event.Event;

import com.emitrom.lienzo.client.widget.LienzoPanel;
import com.google.gwt.user.client.ui.Composite;
import org.kie.wires.core.api.events.ShapeAddEvent;
import org.kie.wires.core.client.shapes.PaletteShape;
import org.kie.wires.core.client.util.ShapeCategory;
import org.kie.wires.core.client.util.ShapeType;

public class StencilBuilder extends Composite {

    public static Map<ShapeCategory, Integer> shapesByCategory;

    public StencilBuilder() {

    }

    public StencilBuilder( final Event<ShapeAddEvent> shapeAddEvent,
                           final ShapeCategory shapeCategory,
                           final LienzoPanel panel,
                           final List<PaletteShape> listShapes ) {
        shapesByCategory = new HashMap<ShapeCategory, Integer>();
        for ( ShapeType shapeType : ShapeType.values() ) {
            if ( shapeType.getCategory().equals( shapeCategory ) ) {
                this.newShape( shapeType, panel, shapeAddEvent, listShapes );
            }
        }
    }

    public void newShape( final ShapeType shapeType,
                          final LienzoPanel panel,
                          final Event<ShapeAddEvent> shapeAddEvent,
                          final List<PaletteShape> listShapes ) {
        this.setShapesByCategory( shapeType );

        switch ( shapeType ) {
            case LINE:
                new LineFactory( panel,
                                 shapeAddEvent,
                                 shapesByCategory,
                                 listShapes );
                break;
            case RECTANGLE:
                new RectangleFactory( panel,
                                      shapeAddEvent,
                                      shapesByCategory,
                                      listShapes );
                break;
            case CIRCLE:
                new CircleFactory( panel,
                                   shapeAddEvent,
                                   shapesByCategory,
                                   listShapes );
                break;
            default:
                throw new IllegalStateException( "Unrecognized shape type '" + shapeType + "'!" );
        }

    }

    public void setShapesByCategory( final ShapeType shapeType ) {
        boolean exist = false;
        for ( Map.Entry<ShapeCategory, Integer> entry : shapesByCategory.entrySet() ) {
            if ( entry.getKey().equals( shapeType.getCategory() ) ) {
                exist = true;
                shapesByCategory.put( entry.getKey(), shapesByCategory.get( entry.getKey() ) + 1 );
                break;
            }
        }
        if ( !exist ) {
            shapesByCategory.put( shapeType.getCategory(), 1 );
        }
    }

}