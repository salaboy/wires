package com.bayesian.network.api.builder;

import javax.enterprise.event.Event;

import com.bayesian.network.api.events.BayesianEvent;
import com.bayesian.network.api.events.ProbabilityEvent;
import com.bayesian.network.api.factory.LayerLineFactory;
import com.bayesian.network.api.factory.LayerRectangleFactory;
import com.bayesian.network.api.factory.LayerTextFactory;
import com.bayesian.parser.client.model.BayesVariable;
import com.emitrom.lienzo.client.core.shape.Group;
import com.emitrom.lienzo.client.core.shape.Line;
import com.emitrom.lienzo.client.core.shape.Rectangle;
import com.emitrom.lienzo.client.core.shape.Shape;
import com.emitrom.lienzo.client.widget.LienzoPanel;
import com.google.gwt.user.client.ui.Composite;

public class LayerBuilder extends Composite {

    public LayerBuilder() {
    }

    public LayerBuilder(Group group, final Shape shape, LienzoPanel panel, int accountLayers, String template,
            Event<BayesianEvent> bayesianEvent, BayesVariable node, Event<ProbabilityEvent> probabilityEvent) {
        this.newLayer(group, shape, panel, accountLayers, template, bayesianEvent, node, probabilityEvent);
    }

    public void newLayer(Group group, final Shape shape, LienzoPanel panel, int accountLayers, String template,
            Event<BayesianEvent> bayesianEvent, BayesVariable node, Event<ProbabilityEvent> probabilityEvent) {
        if (shape == null) {
            new LayerTextFactory(group, panel, accountLayers, node, probabilityEvent);
        } else if (shape instanceof Line) {
            new LayerLineFactory(group, panel, accountLayers);
        } else if (shape instanceof Rectangle) {
            new LayerRectangleFactory(group, accountLayers, template, bayesianEvent);
        }
    }

}
