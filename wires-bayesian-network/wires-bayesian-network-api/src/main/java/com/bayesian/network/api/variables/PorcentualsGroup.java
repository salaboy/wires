package com.bayesian.network.api.variables;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.kie.wires.core.api.events.ClearEvent;

import com.bayesian.network.api.events.ProbabilityEvent;
import com.bayesian.network.api.factory.ProbabilityFactory;
import com.emitrom.lienzo.client.core.shape.Layer;
import com.emitrom.lienzo.client.widget.LienzoPanel;
import com.google.gwt.user.client.ui.Composite;

@Dependent
public class PorcentualsGroup extends Composite {

    private static Layer layer;

    private static LienzoPanel panel;

    public static int accountLayers;
    public static Integer accountNodes;

    @Inject
    private Event<ProbabilityEvent> probabilityEvent;

    public PorcentualsGroup() {
    }

    @PostConstruct
    public void init() {
        panel = new LienzoPanel(1200, 600);
        super.initWidget(panel);
        layer = new Layer();
        panel.getScene().add(layer);
    }

    public void myResponseObserver(@Observes ProbabilityEvent event) {
        layer.removeAll();
        if (event.getBayesianProbabilityGrid() != null) {
            layer.add(event.getBayesianProbabilityGrid());
        } else if (event.getVariable() != null) {
            new ProbabilityFactory(event.getVariable(), probabilityEvent);
        }
        layer.draw();
    }

    public void clearPanel(@Observes ClearEvent event) {
        layer.removeAll();
        layer.draw();
    }

}