package org.kie.wires.client.factoryLayers;

import javax.enterprise.event.Event;

import org.kie.wires.client.events.DrawnShapesEvent;

import com.emitrom.lienzo.client.core.shape.Circle;
import com.emitrom.lienzo.client.core.shape.Group;
import com.emitrom.lienzo.client.core.shape.Layer;
import com.emitrom.lienzo.client.core.shape.Line;
import com.emitrom.lienzo.client.core.shape.Rectangle;
import com.emitrom.lienzo.client.core.shape.Shape;
import com.emitrom.lienzo.client.widget.LienzoPanel;
import com.google.gwt.user.client.ui.Composite;

@SuppressWarnings("rawtypes")
public class LayerBuilder extends Composite {

    public LayerBuilder() {
    }

    public LayerBuilder(Group group, final Shape shape, LienzoPanel panel, Layer layer, int accountLayers,
            Event<DrawnShapesEvent> drawnShapesEvent) {
        this.newLayer(group, shape, panel, accountLayers, layer, drawnShapesEvent);
    }

    public void newLayer(Group group, final Shape shape, LienzoPanel panel, int accountLayers, Layer layer,
            Event<DrawnShapesEvent> drawnShapesEvent) {
        if (shape instanceof Line) {
            new LayerLineFactory(group, panel, accountLayers, layer, drawnShapesEvent);
        } else if (shape instanceof Rectangle) {
            new LayerRectangleFactory(group, panel, accountLayers, layer, drawnShapesEvent);
        } else if (shape instanceof Circle) {
            new LayerCircleFactory(group, panel, accountLayers, layer, drawnShapesEvent);
        }
    }

}
