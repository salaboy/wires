package org.kie.wires.client.factoryLayers;

import org.jboss.errai.common.client.api.Caller;

import com.emitrom.lienzo.client.core.shape.Circle;
import com.emitrom.lienzo.client.core.shape.Group;
import com.emitrom.lienzo.client.core.shape.Layer;
import com.emitrom.lienzo.client.core.shape.Line;
import com.emitrom.lienzo.client.core.shape.Rectangle;
import com.emitrom.lienzo.client.core.shape.Shape;
import com.emitrom.lienzo.client.widget.LienzoPanel;
import com.google.gwt.user.client.ui.Composite;
import com.hernsys.bayesian.client.entry.BayesianService;

@SuppressWarnings("rawtypes")
public class LayerBuilder extends Composite {

    public LayerBuilder() {
    }

    public LayerBuilder(Group group, final Shape shape, LienzoPanel panel, Layer layer, int accountLayers, String template, Caller<BayesianService> bayesianService) {
        this.newLayer(group, shape, panel, accountLayers, layer, template, bayesianService);
    }

    public void newLayer(Group group, final Shape shape, LienzoPanel panel, int accountLayers, Layer layer, String template, Caller<BayesianService> bayesianService) {
        if (shape instanceof Line) {
            new LayerLineFactory(group, panel, accountLayers, layer);
        } else if (shape instanceof Rectangle) {
            new LayerRectangleFactory(group, panel, accountLayers, layer, template, bayesianService);
        } else if (shape instanceof Circle) {
            new LayerCircleFactory(group, panel, accountLayers, layer);
        }
    }

}
