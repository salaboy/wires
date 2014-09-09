package com.bayesian.network.client.screen;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.bayesian.network.client.events.BayesianEvent;
import com.bayesian.network.client.events.LayerEvent;
import com.bayesian.network.client.events.ProbabilityEvent;
import com.bayesian.network.client.events.ReadyEvent;
import com.bayesian.network.client.factory.BayesianFactory;
import com.bayesian.parser.client.service.BayesianService;
import org.jboss.errai.common.client.api.Caller;
import org.kie.wires.core.client.canvas.Canvas;

@Dependent
public class BayesianScreen extends Canvas {

    @Inject
    private Caller<BayesianService> bayesianService;

    @Inject
    private Event<ProbabilityEvent> probabilityEvent;

    @Inject
    private Event<ReadyEvent> readyEvent;

    @Inject
    private Event<LayerEvent> layerEvent;

    public void addNewPanel( @Observes BayesianEvent event ) {
        new BayesianFactory( bayesianService,
                             event.getTemplate(),
                             layerEvent,
                             probabilityEvent,
                             readyEvent );
    }

}
