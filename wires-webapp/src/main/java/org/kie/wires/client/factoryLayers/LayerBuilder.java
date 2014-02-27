package org.kie.wires.client.factoryLayers;

import javax.enterprise.event.Event;

import org.kie.wires.client.events.BayesianEvent;
import org.kie.wires.client.events.ProbabilityEvent;

import com.emitrom.lienzo.client.core.shape.Group;
import com.emitrom.lienzo.client.core.shape.Layer;
import com.emitrom.lienzo.client.core.shape.Line;
import com.emitrom.lienzo.client.core.shape.Rectangle;
import com.emitrom.lienzo.client.core.shape.Shape;
import com.emitrom.lienzo.client.widget.LienzoPanel;
import com.google.gwt.user.client.ui.Composite;
import com.xstream.bayesian.client.model.BayesVariable;

@SuppressWarnings("rawtypes")
public class LayerBuilder extends Composite {

    public LayerBuilder() {
    }

    public LayerBuilder(Group group, final Shape shape, LienzoPanel panel, Layer layer, int accountLayers, String template,
            Event<BayesianEvent> bayesianEvent, BayesVariable node, Event<ProbabilityEvent> probabilityEvent) {
        this.newLayer(group, shape, panel, accountLayers, layer, template, bayesianEvent, node, probabilityEvent);
    }

    public void newLayer(Group group, final Shape shape, LienzoPanel panel, int accountLayers, Layer layer, String template,
            Event<BayesianEvent> bayesianEvent, BayesVariable node, Event<ProbabilityEvent> probabilityEvent) {
        if(shape == null){
            new LayerTextFactory(group, panel, accountLayers, layer, node, probabilityEvent);
        }else if (shape instanceof Line) {
            new LayerLineFactory(group, panel, accountLayers, layer);
        } else if (shape instanceof Rectangle) {
            new LayerRectangleFactory(group, accountLayers, layer, template, bayesianEvent);
        } 
    }

}
