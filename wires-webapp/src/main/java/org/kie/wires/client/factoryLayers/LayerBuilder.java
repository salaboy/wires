package org.kie.wires.client.factoryLayers;

import com.emitrom.lienzo.client.core.shape.Circle;
import com.emitrom.lienzo.client.core.shape.Group;
import com.emitrom.lienzo.client.core.shape.Line;
import com.emitrom.lienzo.client.core.shape.Rectangle;
import com.emitrom.lienzo.client.core.shape.Shape;
import com.emitrom.lienzo.client.widget.LienzoPanel;
import com.google.gwt.user.client.ui.Composite;

@SuppressWarnings("rawtypes")
public class LayerBuilder extends Composite {

    public LayerBuilder() {
    }

    public LayerBuilder(Group group, final Shape shape, LienzoPanel panel, int accountLayers) {
        this.newLayer(group, shape, panel, accountLayers);
    }

    public void newLayer(Group group, final Shape shape, LienzoPanel panel, int accountLayers) {
        if (shape instanceof Line) {
            new LayerLineFactory(group, panel, accountLayers);
        } else if (shape instanceof Rectangle) {
            new LayerRectangleFactory(group, panel, accountLayers);
        } else if (shape instanceof Circle) {
            new LayerCircleFactory(group, panel, accountLayers);
        }
    }

}
