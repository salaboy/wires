package org.kie.wires.client.palette;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.kie.wires.client.factoryShapes.StencilBuilder;
import org.kie.wires.core.api.events.ShapeAddEvent;
import org.kie.wires.core.client.shapes.PaletteShape;
import org.kie.wires.core.client.util.ShapeCategory;
import org.kie.wires.core.client.util.ShapeFactoryUtil;
import org.kie.wires.core.client.util.ShapesUtils;

import com.emitrom.lienzo.client.core.shape.Layer;
import com.emitrom.lienzo.client.widget.LienzoPanel;
import com.google.common.collect.Lists;
import com.google.gwt.user.client.ui.Composite;

@Dependent
public class ConnectorsGroup extends Composite {

    private Layer layer;

    private LienzoPanel panel;

    private static List<PaletteShape> listShapes = Lists.newArrayList();

    @Inject
    private Event<ShapeAddEvent> shapeAddEvent;

    public ConnectorsGroup() {
    }

    @PostConstruct
    public void init() {
        panel = new LienzoPanel(ShapeFactoryUtil.WIDTH_PANEL, ShapesUtils.calculateHeight(ShapesUtils
                .getAccountShapesByCategory(ShapeCategory.CONNECTORS)));
        super.initWidget(panel);
        layer = new Layer();
        panel.getScene().add(layer);
        new StencilBuilder(shapeAddEvent, ShapeCategory.CONNECTORS, panel, listShapes);
        for (PaletteShape sha : listShapes) {
            layer.add(sha);
        }
        layer.draw();
    }

}