package org.kie.wires.client.factoryShapes;

import java.util.List;
import java.util.Map;
import javax.enterprise.event.Event;

import com.emitrom.lienzo.client.core.event.NodeMouseDownEvent;
import com.emitrom.lienzo.client.core.event.NodeMouseDownHandler;
import com.emitrom.lienzo.client.core.shape.Circle;
import com.emitrom.lienzo.client.core.shape.Rectangle;
import com.emitrom.lienzo.client.core.shape.Shape;
import com.emitrom.lienzo.client.widget.LienzoPanel;
import org.kie.wires.core.api.events.ShapeAddEvent;
import org.kie.wires.core.client.shapes.PaletteShape;
import org.kie.wires.core.client.util.ShapeCategory;
import org.kie.wires.core.client.util.ShapeType;
import org.kie.wires.core.client.util.ShapesUtils;

public class CircleFactory extends ShapeFactory<Circle> {

    private static String DESCRIPTION = "Circle";

    private static int shapes;

    public CircleFactory() {
    }

    public CircleFactory( final LienzoPanel panel,
                          final Event<ShapeAddEvent> shapeAddEvent,
                          final Map<ShapeCategory, Integer> shapesByCategory,
                          final List<PaletteShape> listShapes ) {
        super( panel,
               shapeAddEvent );
        shapes = shapesByCategory.get( this.getCategory() );
        super.drawBoundingBox( listShapes,
                               shapes,
                               DESCRIPTION );
    }

    @Override
    protected Shape<Circle> drawShape() {
        final Circle circle = new Circle( 15 );
        setAttributes( circle,
                       this.getX(),
                       this.getY() );
        shape.setShape( circle );
        return circle;
    }

    @Override
    protected void addShapeHandlers( final Shape<Circle> shape ) {
        shape.addNodeMouseDownHandler( getNodeMouseDownEvent() );
    }

    @Override
    protected void addBoundingHandlers( final Rectangle boundingBox ) {
        boundingBox.addNodeMouseDownHandler( getNodeMouseDownEvent() );
    }

    @Override
    protected NodeMouseDownHandler getNodeMouseDownEvent() {
        NodeMouseDownHandler nodeMouseDownHandler = new NodeMouseDownHandler() {
            public void onNodeMouseDown( NodeMouseDownEvent event ) {
                final Circle floatingShape = new Circle( 10 );
                setAttributes( floatingShape,
                               getFloatingX(),
                               getFloatingY() );
                setFloatingPanel( floatingShape,
                                  "WiresCircle",
                                  40,
                                  70,
                                  event,
                                  null );
            }

        };

        return nodeMouseDownHandler;
    }

    private void setAttributes( final Circle circle,
                                final double x,
                                final double y ) {
        circle.setX( x )
                .setY( y )
                .setStrokeColor( ShapesUtils.RGB_STROKE_SHAPE )
                .setStrokeWidth( ShapesUtils.RGB_STROKE_WIDTH_SHAPE )
                .setFillColor( ShapesUtils.RGB_FILL_SHAPE )
                .setDraggable( false );
    }

    private double getX() {
        return 24 + super.calculateX( shapes );
    }

    private double getY() {
        return 19 + super.calculateY( shapes );
    }

    private double getFloatingX() {
        return 0;
    }

    private double getFloatingY() {
        return 0;
    }

    @Override
    protected ShapeCategory getCategory() {
        return ShapeType.CIRCLE.getCategory();
    }

}
