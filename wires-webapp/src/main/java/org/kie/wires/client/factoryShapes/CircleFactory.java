package org.kie.wires.client.factoryShapes;

import java.util.Map;

import javax.enterprise.event.Event;

import org.kie.wires.client.events.ShapeAddEvent;
import org.kie.wires.client.shapes.WiresCircle;

import com.emitrom.lienzo.client.core.event.NodeMouseDownEvent;
import com.emitrom.lienzo.client.core.event.NodeMouseDownHandler;
import com.emitrom.lienzo.client.core.shape.Circle;
import com.emitrom.lienzo.client.core.shape.Group;
import com.emitrom.lienzo.client.core.shape.Layer;
import com.emitrom.lienzo.client.core.shape.Rectangle;
import com.emitrom.lienzo.client.core.shape.Shape;
import com.emitrom.lienzo.client.widget.LienzoPanel;
import com.google.gwt.user.client.ui.RootPanel;

public class CircleFactory extends ShapeFactory<Circle> {

    private static String DESCRIPTION = "Circle";

    private static int shapes;

    public CircleFactory() {
    }

    public CircleFactory(Group group, LienzoPanel panel, Event<ShapeAddEvent> shapeAddEvent,
            Map<ShapeCategory, Integer> shapesByCategory) {
        super(panel, shapeAddEvent);
        shapes = shapesByCategory.get(this.getCategory());
        this.drawBoundingBox(group);
    }

    @Override
    protected void drawBoundingBox(Group group) {
        this.addBoundingHandlers(super.createBoundingBox(group, shapes), group);
        this.addShapeHandlers(drawShape(), group);
        group.add(super.createDescription(DESCRIPTION, shapes));

    }

    @Override
    protected Shape<Circle> drawShape() {
        final Circle circle = new Circle(15);
        setAttributes(circle, this.getX(), this.getY());
        return circle;
    }

    @Override
    protected void addShapeHandlers(Shape<Circle> shape, Group group) {
        shape.addNodeMouseDownHandler(getNodeMouseDownEvent(group));
        group.add(shape);

    }

    @Override
    protected void addBoundingHandlers(Rectangle boundingBox, Group group) {
        boundingBox.addNodeMouseDownHandler(getNodeMouseDownEvent(group));

    }

    @Override
    protected NodeMouseDownHandler getNodeMouseDownEvent(Group group) {
        NodeMouseDownHandler nodeMouseDownHandler = new NodeMouseDownHandler() {
            public void onNodeMouseDown(NodeMouseDownEvent event) {
                final Layer floatingLayer = new Layer();
                final LienzoPanel floatingPanel = new LienzoPanel(50, 50);
                final WiresCircle floatingShape = new WiresCircle(15);
                setAttributes(floatingShape, 17, 17);
                floatingLayer.add(floatingShape);
                floatingPanel.add(floatingLayer);
                floatingLayer.draw();
                RootPanel.get().add(floatingPanel);
//                setFloatingHandlers(getFloatingStyle(floatingPanel, event), floatingPanel, floatingShape);
            }

        };

        return nodeMouseDownHandler;
    }

    @Override
    protected ShapeCategory getCategory() {
//        return ShapeType.CIRCLE.getCategory();
        return null;
    }

    private void setAttributes(Circle circle, double x, double y) {
        circle.setX(x).setY(y).setStrokeColor(ShapeFactoryUtil.RGB_STROKE_SHAPE)
                .setStrokeWidth(ShapeFactoryUtil.RGB_STROKE_WIDTH_SHAPE).setFillColor(ShapeFactoryUtil.RGB_FILL_SHAPE)
                .setDraggable(false);
    }

    private double getX() {
        return 24 + super.calculateX(shapes);
    }

    private double getY() {
        return 19 + super.calculateY(shapes);
    }

}
