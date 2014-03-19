package com.bayesian.network.api.factory;

import javax.enterprise.event.Event;

import org.kie.wires.core.client.util.ShapesUtils;

import com.bayesian.network.api.events.BayesianEvent;
import com.emitrom.lienzo.client.core.event.NodeMouseClickEvent;
import com.emitrom.lienzo.client.core.event.NodeMouseClickHandler;
import com.emitrom.lienzo.client.core.shape.Group;
import com.emitrom.lienzo.client.core.shape.Rectangle;
import com.emitrom.lienzo.client.core.shape.Shape;

public class LayerRectangleFactory extends LayerFactory<Rectangle> {

    private static final String DESCRIPTION = "Rectangle";

    private static int layers;

    private Event<BayesianEvent> bayesianEvent;

    public LayerRectangleFactory() {

    }

    public LayerRectangleFactory(Group group, Integer lay, String template, Event<BayesianEvent> bayesianEvent) {
        layers = lay;
        this.bayesianEvent = bayesianEvent;
        this.drawBoundingBox(group, template);
    }

    @Override
    public void drawBoundingBox(Group group, String template) {
        final Double x = this.getX() + 218;
        final Double y = this.getY() + 5;
        super.createBoundingBox(group, layers);
        String text = DESCRIPTION;
        if (template != null) {
            text = template;
            this.addShapeClick(this.drawLayer(), group, template);
        } else {
            super.createOptions(x.intValue(), y.intValue());
            group.add(this.drawLayer());
        }
        group.add(super.createDescription(text, layers));
    }

    private void addShapeClick(Shape<Rectangle> shape, final Group group, final String xml03File) {
        shape.addNodeMouseClickHandler(new NodeMouseClickHandler() {

            @Override
            public void onNodeMouseClick(NodeMouseClickEvent event) {
                bayesianEvent.fire(new BayesianEvent(xml03File));
            }
        });
        group.add(shape);
    }

    @Override
    public Shape<Rectangle> drawLayer() {
        final Rectangle rectangle = new Rectangle(15, 15);
        this.setAttributes(rectangle, getX(), getY());
        return rectangle;
    }

    private void setAttributes(Rectangle floatingShape, double x, double y) {
        floatingShape.setX(x).setY(y).setStrokeColor(ShapesUtils.RGB_STROKE_SHAPE)
                .setStrokeWidth(ShapesUtils.RGB_STROKE_WIDTH_SHAPE).setFillColor(ShapesUtils.RGB_FILL_SHAPE)
                .setDraggable(false);
    }

    private double getX() {
        return 13;
    }

    private double getY() {
        return 8 + super.calculateY(layers);
    }

}