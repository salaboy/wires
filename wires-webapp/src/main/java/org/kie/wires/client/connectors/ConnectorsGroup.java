package org.kie.wires.client.connectors;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.kie.wires.client.events.ShapeAddEvent;
import org.kie.wires.client.factoryShapes.ShapeBuilder;
import org.kie.wires.client.factoryShapes.ShapeFactoryUtil;
import org.kie.wires.client.factoryShapes.ShapeType;

import com.emitrom.lienzo.client.core.shape.Group;
import com.emitrom.lienzo.client.core.shape.Layer;
import com.emitrom.lienzo.client.widget.LienzoPanel;
import com.google.gwt.user.client.ui.Composite;

@Dependent
public class ConnectorsGroup extends Composite {

    @Inject
    private Event<ShapeAddEvent> shapeAddEvent;

    @Inject
    private ShapeBuilder shapeBuilder;

    @PostConstruct
    public void init() {
        LienzoPanel panel = new LienzoPanel(ShapeFactoryUtil.WIDTH_PANEL, ShapeFactoryUtil.HEIGHT_PANEL);
        initWidget(panel);
        Layer layer = new Layer();
        panel.add(layer);
        Group group1 = new Group();
        group1.setX(0).setY(5);
        layer.add(group1);
        shapeBuilder.newShape(group1, ShapeType.LINE, panel, shapeAddEvent);
        layer.draw();
    }

}