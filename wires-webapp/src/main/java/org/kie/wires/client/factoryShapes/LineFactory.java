package org.kie.wires.client.factoryShapes;

import java.util.Map;

import javax.enterprise.event.Event;

import org.kie.wires.core.api.events.ShapeAddEvent;
import org.kie.wires.core.client.util.ShapeCategory;
import org.kie.wires.core.client.util.ShapesUtils;

import com.emitrom.lienzo.client.core.event.NodeMouseDownEvent;
import com.emitrom.lienzo.client.core.event.NodeMouseDownHandler;
import com.emitrom.lienzo.client.core.shape.Group;
import com.emitrom.lienzo.client.core.shape.Line;
import com.emitrom.lienzo.client.core.shape.Rectangle;
import com.emitrom.lienzo.client.core.shape.Shape;
import com.emitrom.lienzo.client.core.types.DragBounds;
import com.emitrom.lienzo.client.widget.LienzoPanel;

public class LineFactory extends ShapeFactory<Line> {

    private static String DESCRIPTION = "Line";

    private static int shapes;

    public LineFactory() {
    }

    public LineFactory(Group group, LienzoPanel panel, Event<ShapeAddEvent> shapeAddEvent, 
            Map<ShapeCategory, Integer> shapesByCategory) {
        super(panel, shapeAddEvent);
        shapes = shapesByCategory.get(this.getCategory());
        this.drawBoundingBox(group);
        // layersScreen.initDrawLayer(ShapeType.LINE);
        
    }

    @Override
    public void drawBoundingBox(Group group) {
        this.addBoundingHandlers(super.createBoundingBox(group, shapes), group);
        this.addShapeHandlers(drawShape(), group);
        group.add(super.createDescription(DESCRIPTION, shapes));
    }

    @Override
    public Shape<Line> drawShape() {
        Line editableLine = new Line(this.getX1(), this.getY1(), this.getX2(), this.getY2());
        
        editableLine.setDragBounds(new DragBounds(150, 260, 150, 150));
        editableLine.setStrokeColor(ShapesUtils.RGB_STROKE_SHAPE).setStrokeWidth(ShapesUtils.RGB_STROKE_WIDTH_LINE)
                .setDraggable(false);
        return editableLine;
    }

    @Override
    public void addShapeHandlers(Shape<Line> shape, Group group) {
        shape.addNodeMouseDownHandler(getNodeMouseDownEvent(group));
        group.add(shape);

    }

    @Override
    protected void addBoundingHandlers(Rectangle boundingBox, Group group) {
        boundingBox.addNodeMouseDownHandler(getNodeMouseDownEvent(group));
    }

    @Override
    protected NodeMouseDownHandler getNodeMouseDownEvent(final Group group) {
        NodeMouseDownHandler nodeMouseDownHandler = new NodeMouseDownHandler() {
            public void onNodeMouseDown(NodeMouseDownEvent event) {
                final Line floatingShape = new Line(getFloatingX1(), getFloatingY1(), getFloatingX2(),
                        getFloatingY2());
                floatingShape.setStrokeColor(ShapesUtils.RGB_STROKE_SHAPE)
                        .setStrokeWidth(ShapesUtils.RGB_STROKE_WIDTH_LINE).setDraggable(false);
                setFloatingPanel(floatingShape, "WiresLine", 30, 30, event, null);
                // layersScreen.initDrawLayer(ShapeType.LINE);
            }
        };

        return nodeMouseDownHandler;

    }

    private double getX1() {
        return 12 + super.calculateX(shapes);
    }

    private double getY1() {
        return 8 + super.calculateY(shapes);
    }

    private double getX2() {
        return 42 + super.calculateX(shapes);
    }

    private double getY2() {
        return 30 + super.calculateY(shapes);
    }

    private double getFloatingX1() {
        return 0;
    }

    // this value must be calculated
    private double getFloatingY1() {
        return 0;
    }

    // this value must be calculated
    private double getFloatingX2() {
        return 30;
    }

    // this value must be calculated
    private double getFloatingY2() {
        return 30;
    }

    @Override
    protected ShapeCategory getCategory() {
        return ShapeType.LINE.getCategory();
    }

}