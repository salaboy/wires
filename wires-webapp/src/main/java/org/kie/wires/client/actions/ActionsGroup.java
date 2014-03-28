package org.kie.wires.client.actions;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.kie.wires.client.events.ImageReadyEvent;
import org.kie.wires.client.factoryShapes.ActionFactory;
import org.kie.wires.core.api.events.ClearEvent;
import org.kie.wires.core.client.shapes.ActionShape;
import org.kie.wires.core.client.util.ShapeCategory;
import org.kie.wires.core.client.util.ShapeFactoryUtil;
import org.kie.wires.core.client.util.ShapesUtils;

import com.emitrom.lienzo.client.core.shape.Layer;
import com.emitrom.lienzo.client.widget.LienzoPanel;
import com.google.gwt.user.client.ui.Composite;

@Dependent
public class ActionsGroup extends Composite {

    private Layer layer;

    private LienzoPanel panel;

    @Inject
    private Event<ClearEvent> clearEvent;

    @Inject
    private Event<ImageReadyEvent> imageReadyEvent;

    public ActionsGroup() {
    }

    @PostConstruct
    public void init() {
        panel = new LienzoPanel(ShapeFactoryUtil.WIDTH_PANEL, ShapesUtils.calculateHeight(ShapesUtils
                .getAccountShapesByCategory(ShapeCategory.SHAPES)));
        super.initWidget(panel);
        layer = new Layer();
        panel.getScene().add(layer);
        this.drawActions();
    }

    private void drawActions() {
        new ActionFactory(clearEvent, imageReadyEvent);
    }

    public void readyEvent(@Observes ImageReadyEvent event) {
        for (ActionShape action : event.getActionsShape()) {
            layer.add(action);
        }
        layer.draw();
    }

}
