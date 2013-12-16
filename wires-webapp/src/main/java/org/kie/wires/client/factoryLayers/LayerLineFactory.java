package org.kie.wires.client.factoryLayers;

import org.kie.wires.client.factoryShapes.ShapeFactoryUtil;
import org.kie.wires.client.shapes.EditableLine;

import com.emitrom.lienzo.client.core.event.NodeMouseDownEvent;
import com.emitrom.lienzo.client.core.event.NodeMouseDownHandler;
import com.emitrom.lienzo.client.core.shape.Group;
import com.emitrom.lienzo.client.core.shape.Line;
import com.emitrom.lienzo.client.core.shape.Rectangle;
import com.emitrom.lienzo.client.core.shape.Shape;
import com.emitrom.lienzo.client.core.types.DragBounds;
import com.emitrom.lienzo.client.widget.LienzoPanel;

public class LayerLineFactory extends LayerFactory<Line> {

    private static final String DESCRIPTION = "Line";

    private static int layers;

    public LayerLineFactory() {

    }

    public LayerLineFactory(Group group, LienzoPanel panel, Integer lay) {
        layers = lay;
        this.drawBoundingBox(group);
    }

    @Override
    public void drawBoundingBox(Group group) {
        this.addBoundingHandlers(super.createBoundingBox(group, layers), group);
        this.addShapeHandlers(this.drawLayer(), group);
        group.add(super.createDescription(DESCRIPTION, layers));
    }

    @Override
    public Shape<Line> drawLayer() {
        Line line = new EditableLine(this.getX1(), this.getY1(), this.getX2(), this.getY2());
        line.setDragBounds(new DragBounds(150, 260, 150, 150));
        line.setStrokeColor(ShapeFactoryUtil.RGB_STROKE_SHAPE).setStrokeWidth(ShapeFactoryUtil.RGB_STROKE_WIDTH_SHAPE)
                .setDraggable(false);
        return line;
    }

    @Override
    public void addShapeHandlers(Shape<Line> shape, Group group) {
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
                // final EditableLine floatingShape = new
                // EditableLine(getFloatingX1(), getFloatingY1(),
                // getFloatingX2(), getFloatingY2());
                // floatingShape.setStrokeColor(ShapeFactoryUtil.RGB_STROKE_SHAPE)
                // .setStrokeWidth(ShapeFactoryUtil.RGB_STROKE_WIDTH_SHAPE).setDraggable(false);
                // setFloatingPanel(floatingShape, 30, 30, event, null);
            }
        };

        return nodeMouseDownHandler;

    }

    private double getX1() {
        return 12;
    }

    private double getY1() {
        return 8 + super.calculateY(layers);
    }

    private double getX2() {
        return 32;
    }

    private double getY2() {
        return 20 + super.calculateY(layers);
    }

}
