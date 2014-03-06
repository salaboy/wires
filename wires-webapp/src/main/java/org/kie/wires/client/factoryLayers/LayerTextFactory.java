package org.kie.wires.client.factoryLayers;

import javax.enterprise.event.Event;

import org.kie.wires.client.events.ProbabilityEvent;

import com.emitrom.lienzo.client.core.event.NodeMouseClickEvent;
import com.emitrom.lienzo.client.core.event.NodeMouseClickHandler;
import com.emitrom.lienzo.client.core.event.NodeMouseDownHandler;
import com.emitrom.lienzo.client.core.shape.Group;
import com.emitrom.lienzo.client.core.shape.Layer;
import com.emitrom.lienzo.client.core.shape.Rectangle;
import com.emitrom.lienzo.client.core.shape.Shape;
import com.emitrom.lienzo.client.widget.LienzoPanel;
import com.xstream.bayesian.client.model.BayesVariable;

public class LayerTextFactory extends LayerFactory<Rectangle> {

    private static int layers;
    private Event<ProbabilityEvent> probabilityEvent;

    public LayerTextFactory() {

    }

    public LayerTextFactory(Group group, LienzoPanel panel, Integer lay, Layer layer, BayesVariable node,
            Event<ProbabilityEvent> probabilityEvent) {
        layers = lay;
        this.probabilityEvent = probabilityEvent;
        this.drawLayer(group, layer, null, node);
    }

    public void drawLayer(Group group, Layer layer, String template, final BayesVariable nodeVariable) {
        final Rectangle boundingBox = super.createBoundingBox(group, layers);
        this.addShapeHandlers(boundingBox, group);

        boundingBox.addNodeMouseClickHandler(new NodeMouseClickHandler() {

            @Override
            public void onNodeMouseClick(NodeMouseClickEvent event) {
                probabilityEvent.fire(new ProbabilityEvent(nodeVariable, null));

            }
        });
        group.add(boundingBox);

        group.add(super.createDescription(nodeVariable.getName(), layers));
    }

    @Override
    public void drawBoundingBox(Group group, Layer layer, String template) {

    }

    @Override
    public Shape<Rectangle> drawLayer() {
        return null;
    }

    @Override
    public void addShapeHandlers(Shape<Rectangle> shape, Group group) {

    }

    @Override
    protected void addBoundingHandlers(Rectangle boundingBox, Group group) {

    }

    @Override
    protected NodeMouseDownHandler getNodeMouseDownEvent(final Group group) {
        return null;

    }

}
