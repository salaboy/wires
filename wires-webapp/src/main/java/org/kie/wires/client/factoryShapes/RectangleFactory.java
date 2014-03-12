package org.kie.wires.client.factoryShapes;

import java.util.Map;

import javax.enterprise.event.Event;

import org.kie.wires.core.api.events.ShapeAddEvent;
import org.kie.wires.core.client.util.ShapeCategory;
import org.kie.wires.core.client.util.ShapesUtils;

import com.emitrom.lienzo.client.core.event.NodeMouseDownEvent;
import com.emitrom.lienzo.client.core.event.NodeMouseDownHandler;
import com.emitrom.lienzo.client.core.shape.Group;
import com.emitrom.lienzo.client.core.shape.Rectangle;
import com.emitrom.lienzo.client.core.shape.Shape;
import com.emitrom.lienzo.client.widget.LienzoPanel;

public class RectangleFactory extends ShapeFactory<Rectangle> {

    private static String DESCRIPTION = "Box";

    private int shapes;

    public RectangleFactory() {

    }

    public RectangleFactory(Group group, LienzoPanel panel, Event<ShapeAddEvent> shapeAddEvent, 
            Map<ShapeCategory, Integer> shapesByCategory) {
        super(panel, shapeAddEvent);
        shapes = shapesByCategory.get(this.getCategory());
        this.drawBoundingBox(group);
    }

    @Override
    protected void drawBoundingBox(Group group) {
        this.addBoundingHandlers(createBoundingBox(group, shapes), group);
        this.addShapeHandlers(drawShape(), group);
        group.add(super.createDescription(DESCRIPTION, shapes));
    }

    @Override
    protected Shape<Rectangle> drawShape() {
        final Rectangle rectangle = new Rectangle(30, 30);
        setAttributes(rectangle, getX(), getY());
        return rectangle;
    }

    @Override
    protected void addShapeHandlers(Shape<Rectangle> shape, Group group) {
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
                final Rectangle floatingShape = new Rectangle(70, 40);
                setAttributes(floatingShape, getFloatingX(), getFloatingY());
                setFloatingPanel(floatingShape,"WiresRectangle", 40, 70, event, null);
            }
        };

        return nodeMouseDownHandler;
    }

    private void setAttributes(Rectangle floatingShape, double x, double y) {
        floatingShape.setX(x).setY(y).setStrokeColor(ShapesUtils.RGB_STROKE_SHAPE)
                .setStrokeWidth(ShapesUtils.RGB_STROKE_WIDTH_SHAPE).setFillColor(ShapesUtils.RGB_FILL_SHAPE)
                .setDraggable(false);
    }

    private double getX() {
        return 11 + super.calculateX(shapes);
    }

    private double getY() {
        return 5 + super.calculateY(shapes);
    }

    private double getFloatingX() {
        return 0;
    }

    private double getFloatingY() {
        return 0;
    }

    @Override
    protected ShapeCategory getCategory() {
        return ShapeType.RECTANGLE.getCategory();
    }

}