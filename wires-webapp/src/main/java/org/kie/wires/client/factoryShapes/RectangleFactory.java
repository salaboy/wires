package org.kie.wires.client.factoryShapes;

import javax.enterprise.event.Event;

import org.kie.wires.client.events.ShapeAddEvent;
import org.kie.wires.client.shapes.EditableRectangle;

import com.emitrom.lienzo.client.core.event.NodeMouseDownEvent;
import com.emitrom.lienzo.client.core.event.NodeMouseDownHandler;
import com.emitrom.lienzo.client.core.shape.Group;
import com.emitrom.lienzo.client.core.shape.Rectangle;
import com.emitrom.lienzo.client.core.shape.Shape;
import com.emitrom.lienzo.client.widget.LienzoPanel;

public class RectangleFactory extends ShapeFactory<Rectangle> {

    private static String DESCRIPTION = "Box";

    public RectangleFactory() {

    }

    public RectangleFactory(Group group, LienzoPanel panel, Event<ShapeAddEvent> shapeAddEvent) {
        super(panel, shapeAddEvent);
        this.drawBoundingBox(group);
    }

    @Override
    protected void drawBoundingBox(Group group) {
        this.addBoundingHandlers(createBoundingBox(group), group);
        this.addShapeHandlers(drawShape(group), group);
        group.add(super.createDescription(group, DESCRIPTION));
    }

    @Override
    protected Shape<Rectangle> drawShape(Group group) {
        final Rectangle rectangle = new Rectangle(30, 30);
        setAttributes(rectangle, getX(group), getY(group), group);
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
                final EditableRectangle floatingShape = new EditableRectangle(70, 40);
                setAttributes(floatingShape, getFloatingX(group), getFloatingY(group), group);

                setFloatingPanel(floatingShape, 40, 70, event);
            }
        };

        return nodeMouseDownHandler;
    }

    private void setAttributes(Rectangle floatingShape, double x, double y, Group group) {
        floatingShape.setX(x)
                    .setY(y)
                    .setStrokeColor(RGB_STROKE_SHAPE)
                    .setStrokeWidth(RGB_STROKE_WIDTH_SHAPE)
                    .setFillColor(RGB_FILL_SHAPE)
                    .setDraggable(false);
    }

    // this value must be calculated
    private double getX(Group group) {
        return 10;
    }

    // this value must be calculated
    private double getY(Group group) {
        return 5;
    }

    // this value must be calculated
    private double getFloatingX(Group group) {
        return 0;
    }

    // this value must be calculated
    private double getFloatingY(Group group) {
        return 0;
    }

}
