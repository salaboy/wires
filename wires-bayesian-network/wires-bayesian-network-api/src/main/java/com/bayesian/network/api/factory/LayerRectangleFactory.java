package com.bayesian.network.api.factory;

import javax.enterprise.event.Event;

import com.bayesian.network.api.events.BayesianEvent;
import com.emitrom.lienzo.client.core.event.NodeMouseClickEvent;
import com.emitrom.lienzo.client.core.event.NodeMouseClickHandler;
import com.emitrom.lienzo.client.core.shape.Rectangle;
import com.emitrom.lienzo.client.core.shape.Shape;

public class LayerRectangleFactory extends LayerFactory<Rectangle> {

    private static final String DESCRIPTION = "Rectangle";

    private static int layers;

    private Event<BayesianEvent> bayesianEvent;

    public LayerRectangleFactory() {

    }

    public LayerRectangleFactory(Integer lay, String template, Event<BayesianEvent> bayesianEvent) {
        layers = lay;
        this.bayesianEvent = bayesianEvent;
        this.drawBoundingBox(template);
    }

    @Override
    public void drawBoundingBox(String template) {
        super.createBoundingBox(layers);
        String text = DESCRIPTION;
        this.drawLayer();
        if (template != null) {
            text = template;
            this.addShapeClick(templateShape.getShape(), template);
        }
        super.createDescription(text, layers);
    }

    private void addShapeClick(Shape<?> shape, final String xml03File) {
        shape.addNodeMouseClickHandler(new NodeMouseClickHandler() {

            @Override
            public void onNodeMouseClick(NodeMouseClickEvent event) {
                bayesianEvent.fire(new BayesianEvent(xml03File));
            }
        });
    }

    @Override
    public void drawLayer() {
        final Rectangle rectangle = new Rectangle(15, 15);
        super.setAttributes(rectangle, getX(), getY());
        templateShape.setShape(rectangle);
    }

    private double getX() {
        return 13;
    }

    private double getY() {
        return 8 + super.calculateY(layers);
    }

}