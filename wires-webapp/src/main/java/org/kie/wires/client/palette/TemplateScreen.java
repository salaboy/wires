package org.kie.wires.client.palette;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.jboss.errai.common.client.api.Caller;
import org.kie.wires.client.factoryLayers.LayerBuilder;
import org.kie.wires.client.factoryShapes.ShapeCategory;
import org.kie.wires.client.factoryShapes.ShapeFactoryUtil;
import org.uberfire.client.annotations.WorkbenchPartTitle;
import org.uberfire.client.annotations.WorkbenchPartView;
import org.uberfire.client.annotations.WorkbenchScreen;

import com.emitrom.lienzo.client.core.shape.Group;
import com.emitrom.lienzo.client.core.shape.Layer;
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
import com.hernsys.bayesian.client.entry.BayesianService;

@Dependent
@WorkbenchScreen(identifier = "WiresTemplateScreen")
public class TemplateScreen extends Composite implements RequiresResize {
	
	interface ViewBinder extends UiBinder<Widget, TemplateScreen> {

    }

    private static ViewBinder uiBinder = GWT.create(ViewBinder.class);

    @UiField
    public SimplePanel templates;

    private LienzoPanel panel;

    private Group group;

    private Layer layer;

    private static final int X = 0;

    private static final int Y = 5;
    
    public static int accountLayers;
    
    @Inject
    private Caller<BayesianService> bayesianServices;
    
    

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
        templates.add(panel);
        this.drawStencil();
    }

    @WorkbenchPartTitle
    @Override
    public String getTitle() {
        return "Templates";
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
    
    private void drawStencil() {
        newAccordion(templates, ShapeCategory.BAYESIAN);
    }
    
    private void newAccordion(SimplePanel simplePanel, ShapeCategory category) {
    	accountLayers += 1;
    	final Shape<Rectangle> rectangle = new Rectangle(10, 10);
    	rectangle.setX(0).setY(5).setStrokeColor(ShapeFactoryUtil.RGB_STROKE_SHAPE)
        .setStrokeWidth(ShapeFactoryUtil.RGB_STROKE_WIDTH_SHAPE).setFillColor(ShapeFactoryUtil.RGB_FILL_SHAPE)
        .setDraggable(false);
    	new LayerBuilder(group, rectangle, panel, layer, accountLayers, "dog-problem.xml03", bayesianServices);
    	
    	accountLayers += 1;
    	new LayerBuilder(group, rectangle, panel, layer, accountLayers, "cancer.xml03", bayesianServices);
    	
        layer.draw();
    }

//    public void myResponseObserver(@Observes ShapeAddEvent shapeAddEvent) {
//        new LayerBuilder(group, shapeAddEvent.getShape(), panel, layer, accountLayers);
//        layer.draw();
//    }


}
