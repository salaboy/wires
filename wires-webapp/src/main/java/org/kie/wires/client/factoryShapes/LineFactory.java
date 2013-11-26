package org.kie.wires.client.factoryShapes;

import javax.enterprise.event.Event;

import org.kie.wires.client.events.ShapeAddEvent;
import org.kie.wires.client.shapes.EditableLine;

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

    public LineFactory() {
    }

    public LineFactory(Group group, LienzoPanel panel, Event<ShapeAddEvent> shapeAddEvent) {
        super(panel, shapeAddEvent);
        this.drawBoundingBox(group);
    }

    @Override
    public void drawBoundingBox(Group group) {
        this.addBoundingHandlers(createBoundingBox(group), group);
        this.addShapeHandlers(drawShape(group), group);
        group.add(super.createDescription(group, DESCRIPTION));
    }

    @Override
    public Shape<Line> drawShape(Group group) {
        Line line = new EditableLine(getX1(group), getY1(group), getX2(group), getY2(group));
        line.setDragBounds(new DragBounds(150, 260, 150, 150));
        line.setStrokeColor(RGB_STROKE_SHAPE).setStrokeWidth(RGB_STROKE_WIDTH_SHAPE).setDraggable(false);
        return line;
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
                final Line floatingShape = new EditableLine(getFloatingX1(group), getFloatingY1(group),
                        getFloatingX2(group), getFloatingY2(group));
                floatingShape.setStrokeColor(RGB_STROKE_SHAPE).setStrokeWidth(RGB_STROKE_WIDTH_SHAPE).setDraggable(false);

                setFloatingPanel(floatingShape,30,30, event);
            }
        };

        return nodeMouseDownHandler;

    }

    // this value must be calculated
    private double getX1(Group group) {
        return 12;
    }

    // this value must be calculated
    private double getY1(Group group) {
        return 8;
    }

    // this value must be calculated
    private double getX2(Group group) {
        return 42;
    }

    // this value must be calculated
    private double getY2(Group group) {
        return 30;
    }

    // this value must be calculated
    private double getFloatingX1(Group group) {
        return 0;
    }

    // this value must be calculated
    private double getFloatingY1(Group group) {
        return 0;
    }

    // this value must be calculated
    private double getFloatingX2(Group group) {
        return 30;
    }

    // this value must be calculated
    private double getFloatingY2(Group group) {
        return 30;
    }

}
