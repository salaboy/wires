package com.bayesian.network.client.builder;

import javax.enterprise.event.Event;

import com.bayesian.network.client.events.BayesianTemplateSelectedEvent;
import com.bayesian.network.client.events.ProbabilityEvent;
import com.bayesian.network.client.factory.LayerCircleFactory;
import com.bayesian.network.client.factory.LayerLineFactory;
import com.bayesian.network.client.factory.LayerRectangleFactory;
import com.bayesian.network.client.factory.LayerTextFactory;
import com.bayesian.parser.client.model.BayesVariable;
import com.emitrom.lienzo.client.core.shape.Circle;
import com.emitrom.lienzo.client.core.shape.Line;
import com.emitrom.lienzo.client.core.shape.Rectangle;
import com.emitrom.lienzo.client.core.shape.Shape;
import com.bayesian.network.client.templates.TemplateShape;

public class LayerBuilder {

    private TemplateShape templateShape;

    public LayerBuilder() {
    }

    public LayerBuilder( final Shape<?> shape,
                         final int accountLayers,
                         final String template,
                         final Event<BayesianTemplateSelectedEvent> bayesianEvent,
                         final BayesVariable node,
                         final Event<ProbabilityEvent> probabilityEvent ) {
        this.newLayer( shape,
                       accountLayers,
                       template,
                       bayesianEvent,
                       node,
                       probabilityEvent );
    }

    public void newLayer( final Shape<?> shape,
                          final int accountLayers,
                          final String template,
                          final Event<BayesianTemplateSelectedEvent> bayesianEvent,
                          final BayesVariable node,
                          final Event<ProbabilityEvent> probabilityEvent ) {
        if ( shape == null ) {
            templateShape = new LayerTextFactory( accountLayers,
                                                  node,
                                                  probabilityEvent ).getLayer();

        } else if ( shape instanceof Line ) {
            templateShape = new LayerLineFactory( accountLayers ).getLayer();

        } else if ( shape instanceof Circle ) {
            templateShape = new LayerCircleFactory( accountLayers ).getLayer();

        } else if ( shape instanceof Rectangle ) {
            templateShape = new LayerRectangleFactory( accountLayers,
                                                       template,
                                                       bayesianEvent ).getLayer();
        }
    }

    public TemplateShape getLayer() {
        return templateShape;
    }

}
