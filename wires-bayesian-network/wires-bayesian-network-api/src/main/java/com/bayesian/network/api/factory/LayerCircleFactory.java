package com.bayesian.network.api.factory;

import com.emitrom.lienzo.client.core.shape.Circle;
import com.emitrom.lienzo.client.widget.LienzoPanel;

public class LayerCircleFactory extends LayerFactory<Circle> {

    private static final int RADIUS = 7;

    private static final String DESCRIPTION = "Circle";

    private static int layers;

    public LayerCircleFactory() {

    }

    public LayerCircleFactory(LienzoPanel panel, Integer lay) {
        this.drawBoundingBox(null);
        layers = lay;
    }

    @Override
    public void drawBoundingBox(String template) {
        super.createBoundingBox(layers);
        this.drawLayer();
        super.createDescription(DESCRIPTION, layers);
    }

    @Override
    public void drawLayer() {
        final Circle circle = new Circle(RADIUS);
        super.setAttributes(circle, this.getX(), this.getY());
        templateShape.setShape(circle);
    }

    private double getX() {
        return 19;
    }

    private double getY() {
        return 15 + super.calculateY(layers);
    }

}