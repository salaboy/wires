package com.bayesian.network.api.screen;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.errai.common.client.api.Caller;
import org.kie.wires.core.api.events.ProgressEvent;
import org.kie.wires.core.client.canvas.Canvas;

import com.bayesian.network.api.events.BayesianEvent;
import com.bayesian.network.api.events.LayerEvent;
import com.bayesian.network.api.events.ProbabilityEvent;
import com.bayesian.network.api.events.ReadyEvent;
import com.bayesian.network.api.factory.BayesianFactory;
import com.bayesian.parser.client.service.BayesianService;

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
    
    @Inject
    private Event<ProgressEvent> progressEvent;

    public void addNewPanel(@Observes BayesianEvent event) {
        new BayesianFactory(bayesianService, event.getTemplate(), layerEvent, probabilityEvent, readyEvent, progressEvent);
    }

}
