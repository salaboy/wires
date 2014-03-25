package org.kie.wires.client.layers;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.kie.wires.core.api.events.ClearEvent;
import org.kie.wires.core.api.events.ReadyShape;
import org.kie.wires.core.client.util.ShapeFactoryUtil;
import org.uberfire.client.annotations.WorkbenchPartTitle;
import org.uberfire.client.annotations.WorkbenchPartView;
import org.uberfire.client.annotations.WorkbenchScreen;

import com.bayesian.network.api.builder.LayerBuilder;
import com.bayesian.network.api.events.LayerEvent;
import com.bayesian.network.api.events.ProbabilityEvent;
import com.bayesian.parser.client.model.BayesVariable;
import com.emitrom.lienzo.client.core.shape.Group;
import com.emitrom.lienzo.client.core.shape.Layer;
import com.emitrom.lienzo.client.core.shape.Line;
import com.emitrom.lienzo.client.core.shape.Rectangle;
import com.emitrom.lienzo.client.core.shape.Shape;
import com.emitrom.lienzo.client.widget.LienzoPanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

@Dependent
@WorkbenchScreen(identifier = "WiresLayersScreen")
public class LayersScreen extends Composite implements RequiresResize {

    interface ViewBinder extends UiBinder<Widget, LayersScreen> {

    }

    private static ViewBinder uiBinder = GWT.create(ViewBinder.class);

    @UiField
    public SimplePanel layers;

    private LienzoPanel panel;

    private Group group;

    private Layer layer;

    public static int accountLayers;

    private static final int X = 0;

    private static final int Y = 5;

    @Inject
    private Event<ProbabilityEvent> probabilityEvent;

    @PostConstruct
    public void init() {
        accountLayers = 0;
        super.initWidget(uiBinder.createAndBindUi(this));
        panel = new LienzoPanel(ShapeFactoryUtil.WIDTH_PANEL, ShapeFactoryUtil.HEIGHT_PANEL);
        layer = new Layer();
        panel.add(layer);
        group = new Group();
        group.setX(X).setY(Y);
        layer.add(group);
        layers.add(panel);
    }

    @WorkbenchPartTitle
    @Override
    public String getTitle() {
        return "Layers";
    }

    @WorkbenchPartView
    public IsWidget getView() {
        return this;
    }

    @Override
    public void onResize() {
        int height = getParent().getOffsetHeight();
        int width = getParent().getOffsetWidth();
        super.setPixelSize(width, height);
    }

    public void myResponseObserver(@Observes ReadyShape readyShape) {
        accountLayers += 1;
        /* refactor this to be generic as well */
        if (readyShape.getShape().equals("WiresRectangle")) {
            buildNewLayer(new Rectangle(40, 30), null);
        } else if (readyShape.getShape().equals("WiresLine")) {
            buildNewLayer(new Line(0, 0, 30, 30), null);
        }
        layer.draw();
    }

    public void drawNamesNode(@Observes LayerEvent layerEvent) {
        for (BayesVariable node : layerEvent.getNodes()) {
            accountLayers += 1;
            buildNewLayer(null, node);
        }
        layer.draw();
    }

    private void buildNewLayer(Shape shape, BayesVariable node) {
        new LayerBuilder(group, shape, panel, accountLayers, null, null, node, probabilityEvent);
    }

    public void clearPanel(@Observes ClearEvent event) {
        accountLayers = 0;
        group.removeAll();
        layer.draw();
    }

}