package com.bayesian.network.api.builder;

import javax.enterprise.event.Event;

import org.kie.wires.core.client.shapes.TemplateShape;

import com.bayesian.network.api.events.BayesianEvent;
import com.bayesian.network.api.events.ProbabilityEvent;
import com.bayesian.network.api.factory.LayerLineFactory;
import com.bayesian.network.api.factory.LayerRectangleFactory;
import com.bayesian.network.api.factory.LayerTextFactory;
import com.bayesian.parser.client.model.BayesVariable;
import com.emitrom.lienzo.client.core.shape.Line;
import com.emitrom.lienzo.client.core.shape.Rectangle;
import com.emitrom.lienzo.client.core.shape.Shape;
import com.emitrom.lienzo.client.widget.LienzoPanel;

public class LayerBuilder {

    private TemplateShape templateShape;

    public LayerBuilder() {
    }

    public LayerBuilder(final Shape<?> shape, LienzoPanel panel, int accountLayers, String template,
            Event<BayesianEvent> bayesianEvent, BayesVariable node, Event<ProbabilityEvent> probabilityEvent) {
        this.newLayer(shape, panel, accountLayers, template, bayesianEvent, node, probabilityEvent);
    }

    public void newLayer(final Shape<?> shape, LienzoPanel panel, int accountLayers, String template,
            Event<BayesianEvent> bayesianEvent, BayesVariable node, Event<ProbabilityEvent> probabilityEvent) {
        if (shape == null) {
            templateShape = new LayerTextFactory(panel, accountLayers, node, probabilityEvent).getLayer();
        } else if (shape instanceof Line) {
            templateShape = new LayerLineFactory(panel, accountLayers).getLayer();
        } else if (shape instanceof Rectangle) {
            templateShape = new LayerRectangleFactory(accountLayers, template, bayesianEvent).getLayer();
        }
    }

    public TemplateShape getLayer() {
        return templateShape;
    }

}
