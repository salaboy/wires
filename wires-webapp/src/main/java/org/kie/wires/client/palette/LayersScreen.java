package org.kie.wires.client.palette;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.kie.wires.client.events.DrawnShapesEvent;
import org.kie.wires.client.events.ShapeAddEvent;
import org.kie.wires.client.factoryLayers.LayerBuilder;
import org.kie.wires.client.factoryLayers.LayerGroup;
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

    @Inject
    public Event<DrawnShapesEvent> drawnShapesEvent;

    private static List<Integer> keysDeleted;

    @PostConstruct
    public void init() {
        keysDeleted = new ArrayList<Integer>();
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
        int totalLayers = accountLayers - keysDeleted.size();
        new LayerBuilder(group, shapeAddEvent.getShape(), panel, layer, totalLayers, drawnShapesEvent);
        layer.draw();
    }

    public void removeLayer(@Observes DrawnShapesEvent drawnShapesEvent) {
        keysDeleted.add(drawnShapesEvent.getKeyDrawnShapes());
        Map<Integer, LayerGroup> listLayerGroup = drawnShapesEvent.getListLayerGroup();
        LayerGroup layerGroup = listLayerGroup.get(drawnShapesEvent.getKeyDrawnShapes());
        group.remove(layerGroup.getBounding());
        group.remove(layerGroup.getShape());
        group.remove(layerGroup.getDescription());
        layer.remove(layerGroup.getDeleteButton());
        layer.remove(layerGroup.getViewButton());
        refreshPositionLayers(listLayerGroup, drawnShapesEvent.getKeyDrawnShapes());
        layer.draw();
    }

    private void refreshPositionLayers(Map<Integer, LayerGroup> listLayerGroup, int currentPosition) {
        boolean wasDeleted = false;
        int cantDeleted = 0;
        for (int i = 1; i <= listLayerGroup.size(); i++) {
            wasDeleted = false;
            for (Integer key : keysDeleted) {
                if (key.equals(i)) {
                    wasDeleted = true;
                    cantDeleted += 1;
                    break;
                }
            }
            LayerGroup layerGroup = listLayerGroup.get(i);
            double currentY = i - 1;
            double descriptionY = i == 1 ? 20 : (20 + currentY * 30);
            double buttonY = i == 1 ? 13 : (13 + currentY * 30);
            layerGroup.getBounding().setY(currentY * 30 - (cantDeleted * 30));
            if (!wasDeleted) {
                int lastDeleted = getLastDeleted(listLayerGroup);
                if (lastDeleted < i) {
                    layerGroup.getShape().setY(layerGroup.getShape().getY() - 30);
                }
            }
            layerGroup.getDescription().setY(descriptionY - (cantDeleted * 30));
            layerGroup.getDeleteButton().setY(buttonY - (cantDeleted * 30));
            layerGroup.getViewButton().setY(buttonY - (cantDeleted * 30));
        }
    }

    private int getLastDeleted(Map<Integer, LayerGroup> listLayerGroup) {
        int lastDeleted = 0;
        for (Integer key : keysDeleted) {
            for (int t = 1; t <= listLayerGroup.size(); t++) {
                if (key.equals(t)) {
                    lastDeleted = t;
                }
            }
        }
        return lastDeleted;
    }

}