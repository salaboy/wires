package com.bayesian.network.api.factory;

import org.kie.wires.core.client.util.ShapesUtils;

import com.emitrom.lienzo.client.core.shape.Circle;
import com.emitrom.lienzo.client.core.shape.Group;
import com.emitrom.lienzo.client.core.shape.Shape;
import com.emitrom.lienzo.client.widget.LienzoPanel;

public class LayerCircleFactory extends LayerFactory<Circle> {

    private static final int RADIUS = 7;

    private static final String DESCRIPTION = "Circle";

    private static int layers;

    public LayerCircleFactory() {

    }

    public LayerCircleFactory(Group group, LienzoPanel panel, Integer lay) {
    	this.drawBoundingBox(group, null);
        layers = lay;
    }

    @Override
    public void drawBoundingBox(Group group, String template) {
        final Double x = this.getX() + 212;
        final Double y = this.getY() - 2;
        super.createOptions(x.intValue(), y.intValue());
        super.createBoundingBox(group, layers);
        group.add(this.drawLayer());
        group.add(super.createDescription(DESCRIPTION, layers));
    }

    @Override
    public Shape<Circle> drawLayer() {
        final Circle circle = new Circle(RADIUS);
        setAttributes(circle, this.getX(), this.getY());
        return circle;
    }

    private void setAttributes(Circle floatingShape, double x, double y) {
        floatingShape.setX(x).setY(y).setStrokeColor(ShapesUtils.RGB_STROKE_SHAPE)
                .setStrokeWidth(ShapesUtils.RGB_STROKE_WIDTH_SHAPE).setFillColor(ShapesUtils.RGB_FILL_SHAPE)
                .setDraggable(false);
    }

    private double getX() {
        return 19;
    }

    private double getY() {
        return 15 + super.calculateY(layers);
    }

}