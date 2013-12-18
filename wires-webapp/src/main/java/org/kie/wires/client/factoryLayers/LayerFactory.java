package org.kie.wires.client.factoryLayers;

import org.kie.wires.client.factoryShapes.ShapeFactoryUtil;

import com.emitrom.lienzo.client.core.event.NodeMouseDownHandler;
import com.emitrom.lienzo.client.core.shape.Group;
import com.emitrom.lienzo.client.core.shape.Rectangle;
import com.emitrom.lienzo.client.core.shape.Shape;
import com.emitrom.lienzo.client.core.shape.Text;

public abstract class LayerFactory<T extends Shape<T>> {

    protected abstract void drawBoundingBox(Group group);

    protected abstract Shape<T> drawLayer();

    protected abstract void addShapeHandlers(Shape<T> shape, Group group);

    protected abstract NodeMouseDownHandler getNodeMouseDownEvent(final Group group);

    protected abstract void addBoundingHandlers(Rectangle boundingBox, Group group);

    protected Rectangle createBoundingBox(Group group, int layers) {
        final Rectangle boundingBox = new Rectangle(ShapeFactoryUtil.WIDTH_BOUNDING_LAYER,
                ShapeFactoryUtil.HEIGHT_BOUNDING_LAYER);
        boundingBox.setX(getXBoundingBox()).setY(this.getYBoundingBox(layers))
                .setStrokeColor(ShapeFactoryUtil.RGB_STROKE_BOUNDING).setStrokeWidth(1)
                .setFillColor(ShapeFactoryUtil.RGB_FILL_BOUNDING).setDraggable(false);
        group.add(boundingBox);
        return boundingBox;
    }

    private double getXBoundingBox() {
        return 0;
    }

    private double getYBoundingBox(int layers) {
        return calculateY(layers);
    }

    protected double getYText(int layers) {
        return 20 + calculateY(layers);
    }

    protected int calculateX(int layers) {
        int x = layers > 1 ? (this.getPositionInRow(layers) - 1) : 0;
        return x > 0 ? (ShapeFactoryUtil.HEIGHT_BOUNDING_LAYER * x) : 0;
    }

    protected int calculateY(int layers) {
        int y = layers > 1 ? layers - 1 : 0;
        return y * ShapeFactoryUtil.HEIGHT_BOUNDING_LAYER;
    }

    private int getPositionInRow(int shapes) {
        return (shapes - this.shapesByRow()) >= 1 ? (shapes - (this.shapesByRow() * getRow(shapes))) : shapes;
    }

    private int getRow(int layers) {
        return Math.round((layers * ShapeFactoryUtil.WIDTH_BOUNDING_LAYER) / ShapeFactoryUtil.WIDTH_STENCIL);
    }

    private int shapesByRow() {
        return 1;
    }

    protected Text createDescription(String description, int shapes) {
        Text text = new Text(description, ShapeFactoryUtil.FONT_FAMILY_DESCRIPTION, ShapeFactoryUtil.FONT_SIZE_DESCRIPTION);
        text.setX(45).setY(this.getYText(shapes)).setFillColor(ShapeFactoryUtil.RGB_TEXT_DESCRIPTION);
        return text;
    }

}
