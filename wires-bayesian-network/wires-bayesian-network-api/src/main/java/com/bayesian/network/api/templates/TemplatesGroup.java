package com.bayesian.network.api.templates;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.kie.wires.core.client.util.ShapeFactoryUtil;
import org.kie.wires.core.client.util.ShapesUtils;

import com.bayesian.network.api.builder.LayerBuilder;
import com.bayesian.network.api.events.BayesianEvent;
import com.emitrom.lienzo.client.core.shape.Layer;
import com.emitrom.lienzo.client.core.shape.Rectangle;
import com.emitrom.lienzo.client.widget.LienzoPanel;
import com.google.common.collect.ImmutableSet;
import com.google.gwt.user.client.ui.Composite;

@Dependent
public class TemplatesGroup extends Composite {

    private Layer layer;

    private LienzoPanel panel;

    private final ImmutableSet<String> filesNames = ImmutableSet.of("dog-problem.xml03", "cancer.xml03", "asia.xml03",
            "car-starts.xml03", "elimbel2.xml03", "john-mary-call.xml03");

    public static int accountLayers;

    @Inject
    private Event<BayesianEvent> bayesianEvent;

    public TemplatesGroup() {
    }

    @PostConstruct
    public void init() {
        accountLayers = 0;
        panel = new LienzoPanel(ShapeFactoryUtil.WIDTH_PANEL, ShapeFactoryUtil.HEIGHT_PANEL);
        super.initWidget(panel);
        layer = new Layer();
        panel.getScene().add(layer);
        this.drawLayersExamples();
    }

    private void drawLayersExamples() {
        final Rectangle rectangle = new Rectangle(10, 10);
        rectangle.setX(0).setY(5).setStrokeColor(ShapesUtils.RGB_STROKE_SHAPE)
                .setStrokeWidth(ShapesUtils.RGB_STROKE_WIDTH_SHAPE).setFillColor(ShapesUtils.RGB_FILL_SHAPE);
        for (String name : filesNames) {
            accountLayers += 1;
            layer.add(new LayerBuilder(rectangle, panel, accountLayers, name, bayesianEvent, null, null).getLayer());
        }
        layer.draw();
    }

}
