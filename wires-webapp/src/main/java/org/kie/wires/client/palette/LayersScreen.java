package org.kie.wires.client.palette;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Observes;

import org.kie.wires.client.events.ShapeAddEvent;
import org.kie.wires.client.factoryLayers.LayerBuilder;
import org.kie.wires.client.factoryShapes.ShapeFactoryUtil;
import org.uberfire.client.annotations.WorkbenchPartTitle;
import org.uberfire.client.annotations.WorkbenchPartView;
import org.uberfire.client.annotations.WorkbenchScreen;

import com.emitrom.lienzo.client.core.shape.Group;
import com.emitrom.lienzo.client.core.shape.Layer;
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

    public void myResponseObserver(@Observes ShapeAddEvent shapeAddEvent) {
        accountLayers += 1;
        new LayerBuilder(group, shapeAddEvent.getShape(), panel, layer, accountLayers);
        layer.draw();
    }

}