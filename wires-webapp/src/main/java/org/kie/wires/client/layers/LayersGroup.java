package org.kie.wires.client.layers;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import com.bayesian.network.client.builder.LayerBuilder;
import com.bayesian.network.client.events.ProbabilityEvent;
import com.bayesian.parser.client.model.BayesVariable;
import com.emitrom.lienzo.client.core.shape.Layer;
import com.emitrom.lienzo.client.core.shape.Shape;
import com.emitrom.lienzo.client.widget.LienzoPanel;
import com.google.gwt.user.client.ui.Composite;
import org.kie.wires.core.client.util.ShapeFactoryUtil;

@Dependent
public class LayersGroup extends Composite {

    private static Layer layer;

    private static LienzoPanel panel;

    public static int accountLayers;
    public static Integer accountNodes;

    @Inject
    private Event<ProbabilityEvent> probabilityEvent;

    public LayersGroup() {
    }

    @PostConstruct
    public void init() {
        panel = new LienzoPanel( ShapeFactoryUtil.WIDTH_PANEL, ShapeFactoryUtil.HEIGHT_PANEL );
        super.initWidget( panel );
        layer = new Layer();
        panel.getScene().add( layer );
    }

    public void buildNewLayer( final Shape<?> shape,
                               final BayesVariable node,
                               final int account ) {
        layer.add( new LayerBuilder( shape,
                                     account,
                                     null,
                                     null,
                                     node,
                                     probabilityEvent ).getLayer() );
    }

    public void drawLayer() {
        layer.draw();
    }

    public void clearPanel() {
        accountNodes = null;
        accountLayers = 0;
        layer.removeAll();
        layer.draw();
    }

}