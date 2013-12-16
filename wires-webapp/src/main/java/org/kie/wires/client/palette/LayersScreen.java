package org.kie.wires.client.palette;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;

import org.kie.wires.client.factoryLayers.LayerBuilder;
import org.kie.wires.client.factoryShapes.ShapeFactoryUtil;
import org.kie.wires.client.factoryShapes.ShapeType;
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


    @PostConstruct
    public void init() {
        super.initWidget(uiBinder.createAndBindUi(this));
        panel = new LienzoPanel(ShapeFactoryUtil.WIDTH_PANEL,
				ShapeFactoryUtil.HEIGHT_PANEL);
		layer = new Layer();
		panel.add(layer);
		group = new Group();
		group.setX(0).setY(5);
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
    
    public void initDrawLayer(final ShapeType shapeType){
    	LayerBuilder builder = new LayerBuilder();
    	builder.newLayer(group, shapeType, panel);
    	layer.draw();
    }
    

}