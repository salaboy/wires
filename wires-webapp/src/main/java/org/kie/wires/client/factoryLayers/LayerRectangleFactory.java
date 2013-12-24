package org.kie.wires.client.factoryLayers;

import org.kie.wires.client.factoryShapes.ShapeFactoryUtil;

import com.emitrom.lienzo.client.core.event.NodeMouseDownEvent;
import com.emitrom.lienzo.client.core.event.NodeMouseDownHandler;
import com.emitrom.lienzo.client.core.shape.Group;
import com.emitrom.lienzo.client.core.shape.Layer;
import com.emitrom.lienzo.client.core.shape.Rectangle;
import com.emitrom.lienzo.client.core.shape.Shape;
import com.emitrom.lienzo.client.widget.LienzoPanel;

public class LayerRectangleFactory extends LayerFactory<Rectangle> {

    private static final String DESCRIPTION = "Rectangle";

    private static int layers;

    public LayerRectangleFactory() {

    }

    public LayerRectangleFactory(Group group, LienzoPanel panel, Integer lay, Layer layer) {
        layers = lay;
        this.drawBoundingBox(group, layer);
    }

    @Override
    public void drawBoundingBox(Group group, Layer layer) {
        final Double x = this.getX() + 218;
        final Double y = this.getY() + 5;
        super.createOptions(layer, x.intValue(), y.intValue());
        this.addBoundingHandlers(super.createBoundingBox(group, layers), group);
        this.addShapeHandlers(this.drawLayer(), group);
        group.add(super.createDescription(DESCRIPTION, layers));
    }

    @Override
    public Shape<Rectangle> drawLayer() {
        final Rectangle rectangle = new Rectangle(15, 15);
        this.setAttributes(rectangle, getX(), getY());
        return rectangle;
    }

    private void setAttributes(Rectangle floatingShape, double x, double y) {
        floatingShape.setX(x).setY(y).setStrokeColor(ShapeFactoryUtil.RGB_STROKE_SHAPE)
                .setStrokeWidth(ShapeFactoryUtil.RGB_STROKE_WIDTH_SHAPE).setFillColor(ShapeFactoryUtil.RGB_FILL_SHAPE)
                .setDraggable(false);
    }

    @Override
    public void addShapeHandlers(Shape<Rectangle> shape, Group group) {
        shape.addNodeMouseDownHandler(this.getNodeMouseDownEvent(group));
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
                // final EditableRectangle floatingShape = new
                // EditableRectangle(getFloatingX1(), getFloatingY1(),
                // getFloatingX2(), getFloatingY2());
                // floatingShape.setStrokeColor(ShapeFactoryUtil.RGB_STROKE_SHAPE)
                // .setStrokeWidth(ShapeFactoryUtil.RGB_STROKE_WIDTH_SHAPE).setDraggable(false);
                // setFloatingPanel(floatingShape, 30, 30, event, null);
            }
        };

        return nodeMouseDownHandler;

    }

    private double getX() {
        return 13;
    }

    private double getY() {
        return 8 + super.calculateY(layers);
    }

}