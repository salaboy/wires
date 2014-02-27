package org.kie.wires.client.palette;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.kie.wires.client.events.ClearEvent;
import org.kie.wires.client.events.LayerEvent;
import org.kie.wires.client.events.ProbabilityEvent;
import org.kie.wires.client.events.ShapeAddEvent;
import org.kie.wires.client.factoryLayers.LayerBuilder;
import org.kie.wires.client.factoryShapes.ShapeFactoryUtil;
import org.kie.wires.client.shapes.EditableLine;
import org.kie.wires.client.shapes.EditableRectangle;
import org.uberfire.client.annotations.WorkbenchPartTitle;
import org.uberfire.client.annotations.WorkbenchPartView;
import org.uberfire.client.annotations.WorkbenchScreen;

import com.emitrom.lienzo.client.core.shape.Group;
import com.emitrom.lienzo.client.core.shape.Layer;
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
import com.xstream.bayesian.client.model.BayesVariable;

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

    public void myResponseObserver(@Observes ShapeAddEvent shapeAddEvent) {
        accountLayers += 1;
        if(shapeAddEvent.getShape() instanceof EditableRectangle){
            buildNewLayer(((EditableRectangle)shapeAddEvent.getShape()).getRectangle(), null);
        }else  if(shapeAddEvent.getShape() instanceof EditableLine){
            buildNewLayer(((EditableLine)shapeAddEvent.getShape()).getLine(), null);
        }
        layer.draw();
    }

    public void drawNamesNode(@Observes LayerEvent layerEvent) {
//        accountLayers = 0;
//        if(layerEvent.getNodes() == null){
//            group.removeAll();
//        }else{
            for (BayesVariable node : layerEvent.getNodes()) {
                accountLayers += 1;
                buildNewLayer(null, node);
            }
        //}
        layer.draw();
    }

    private void buildNewLayer(Shape shape, BayesVariable node) {
        new LayerBuilder(group, shape, panel, layer, accountLayers, null, null, node, probabilityEvent);
    }
    
    public void clearPanel(@Observes ClearEvent event) {
        accountLayers = 0;
        group.removeAll();
        layer.draw();
    }

}