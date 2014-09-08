package com.bayesian.network.client.factory;

import javax.enterprise.event.Event;

import com.bayesian.network.client.events.ProbabilityEvent;
import com.bayesian.parser.client.model.BayesVariable;
import com.emitrom.lienzo.client.core.event.NodeMouseClickEvent;
import com.emitrom.lienzo.client.core.event.NodeMouseClickHandler;
import com.emitrom.lienzo.client.core.shape.Rectangle;
import com.emitrom.lienzo.client.widget.LienzoPanel;

public class LayerTextFactory extends LayerFactory<Rectangle> {

    private static int layers;
    private Event<ProbabilityEvent> probabilityEvent;

    public LayerTextFactory() {

    }

    public LayerTextFactory( final LienzoPanel panel,
                             final Integer lay,
                             final BayesVariable node,
                             final Event<ProbabilityEvent> probabilityEvent ) {
        layers = lay;
        this.probabilityEvent = probabilityEvent;
        super.createBoundingBox( layers );
        this.drawLayer( null, node );
    }

    public void drawLayer( final String template,
                           final BayesVariable node ) {
        templateShape.getBounding().addNodeMouseClickHandler( new NodeMouseClickHandler() {
            @Override
            public void onNodeMouseClick( NodeMouseClickEvent event ) {
                probabilityEvent.fire( new ProbabilityEvent( node, null ) );
            }
        } );
        super.createDescription( node.getName(),
                                 layers );
    }

    @Override
    public void drawBoundingBox( final String template ) {

    }

    @Override
    public void drawLayer() {
    }

}
