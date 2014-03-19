package com.bayesian.network.api.factory;

import javax.enterprise.event.Event;

import com.bayesian.network.api.events.ProbabilityEvent;
import com.bayesian.parser.client.model.BayesVariable;
import com.emitrom.lienzo.client.core.event.NodeMouseClickEvent;
import com.emitrom.lienzo.client.core.event.NodeMouseClickHandler;
import com.emitrom.lienzo.client.core.shape.Group;
import com.emitrom.lienzo.client.core.shape.Rectangle;
import com.emitrom.lienzo.client.core.shape.Shape;
import com.emitrom.lienzo.client.widget.LienzoPanel;

public class LayerTextFactory extends LayerFactory<Rectangle> {

    private static int layers;
    private Event<ProbabilityEvent> probabilityEvent;

    public LayerTextFactory() {

    }

    public LayerTextFactory(Group group, LienzoPanel panel, Integer lay, BayesVariable node,
            Event<ProbabilityEvent> probabilityEvent) {
        layers = lay;
        this.probabilityEvent = probabilityEvent;
        this.drawLayer(group, null, node);
    }

    public void drawLayer(Group group, String template, final BayesVariable node) {
        final Rectangle boundingBox = super.createBoundingBox(group, layers);
        boundingBox.addNodeMouseClickHandler(new NodeMouseClickHandler() {

            @Override
            public void onNodeMouseClick(NodeMouseClickEvent event) {
                probabilityEvent.fire(new ProbabilityEvent(node, null));

            }
        });
        group.add(boundingBox);

        group.add(super.createDescription(node.getName(), layers));
    }

    @Override
    public void drawBoundingBox(Group group, String template) {

    }

    @Override
    public Shape<Rectangle> drawLayer() {
        return null;
    }

}
