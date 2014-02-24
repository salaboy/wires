package org.kie.wires.client.canvas;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.errai.common.client.api.Caller;
import org.kie.wires.client.bayesian.factory.BayesianFactory;
import org.kie.wires.client.events.BayesianEvent;
import org.kie.wires.client.events.LayerEvent;
import org.kie.wires.client.events.ProbabilityEvent;
import org.kie.wires.client.events.ProgressEvent;
import org.kie.wires.client.events.ShapeAddEvent;
import org.kie.wires.client.shapes.EditableShape;
import org.kie.wires.client.util.LienzoUtils;
import org.uberfire.client.annotations.WorkbenchPartTitle;
import org.uberfire.client.annotations.WorkbenchPartView;
import org.uberfire.client.annotations.WorkbenchScreen;
import org.uberfire.lifecycle.OnOpen;

import com.emitrom.lienzo.client.core.shape.GridLayer;
import com.emitrom.lienzo.client.core.shape.Group;
import com.emitrom.lienzo.client.core.shape.Layer;
import com.emitrom.lienzo.client.core.shape.Line;
import com.emitrom.lienzo.client.core.shape.Shape;
import com.emitrom.lienzo.client.widget.LienzoPanel;
import com.emitrom.lienzo.shared.core.types.ColorName;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RequiresResize;
import com.xstream.bayesian.client.entry.BayesianService;


@Dependent
@WorkbenchScreen(identifier = "WiresCanvasScreen")
public class CanvasScreen extends Composite implements RequiresResize {

    private LienzoPanel panel;
    private Layer layer;
    private Group group;
    private static final int X = 0;
    private static final int Y = 5;
    @Inject
    private Caller<BayesianService> bayesianService;
    @Inject
    private Event<LayerEvent> layerEvent;
    @Inject
    private Event<ProbabilityEvent> probabilityEvent;
    @Inject
    private Event<ProgressEvent> progressEvent;

    public static final List<EditableShape> shapesInCanvas = new ArrayList<EditableShape>();

    public CanvasScreen() {
    }

    @PostConstruct
    public void init() {
        panel = new LienzoPanel(800, 600);

        initWidget(panel);

        Line line1 = new Line(0, 0, 0, 0).setStrokeColor(ColorName.BLUE).setAlpha(0.5); // primary
        // line
        Line line2 = new Line(0, 0, 0, 0).setStrokeColor(ColorName.GREEN).setAlpha(0.5); // secondary
        // line
        line2.setDashArray(2, 2); // the secondary lines are dashed lines

        GridLayer gridLayer = new GridLayer(100, line1, 25, line2);
        layer = new Layer();
      
        panel.setBackgroundLayer(gridLayer);
        
        gridLayer.moveToBottom();
        gridLayer.setListening(false);
        
        
        panel.getScene().add(layer);

        panel.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
               // ShapesUtils.deselectAllShapes();
            }
        });

        
        gridLayer.draw();
        layer.draw();

    }

    @OnOpen
    public void onOpen() {

    }

    @WorkbenchPartTitle
    public String getTitle() {
        return "Canvas";
    }

    @WorkbenchPartView
    public IsWidget getView() {
        return this;
    }

    public void myResponseObserver(@Observes ShapeAddEvent shapeAddEvent) {
        Shape shape = shapeAddEvent.getShape();
        shape.getLayer().remove(shape);
        if (shapeAddEvent.getX() < panel.getAbsoluteLeft() || shapeAddEvent.getY() < panel.getAbsoluteTop()) {
            return;
        } else if (shapeAddEvent.getX() > panel.getAbsoluteLeft() + panel.getWidth()
                || shapeAddEvent.getY() > panel.getAbsoluteTop() + panel.getHeight()) {
            return;
        }
        int x = shapeAddEvent.getX() - panel.getAbsoluteLeft();
        int y = shapeAddEvent.getY() - panel.getAbsoluteTop();

        if (x < 1) {
            x = 1;
        }
        if (y < 1) {
            y = 1;
        }

        x = 25 * Math.abs(x / 25);
        y = 25 * Math.abs(y / 25);
        shape.setDraggable(true);

        layer.add(shape);

        ((EditableShape) shape).init(x, y, layer);

        shapesInCanvas.add((EditableShape) shape);

        layer.draw();
    }

    @Override
    public void onResize() {
        int height = getParent().getOffsetHeight();
        int width = getParent().getOffsetWidth();
        if (width > 0 && height > 0) {
            panel.setPixelSize(width, height);

        } else {
            panel.setPixelSize(800, 600);
        }
        layer.draw();
    }

    public void addNewPanel(@Observes BayesianEvent event) {
        group.removeAll();
        new BayesianFactory(group, panel, bayesianService, event.getTemplate(), layer, layerEvent, probabilityEvent, progressEvent);
    }
    
    public void progressBar(@Observes ProgressEvent event){
        if(event.getShapes() != null && !event.getShapes().isEmpty()){
            for(Shape shape : event.getShapes()){
                group.remove(shape);
            }
            layer.draw();
        }else if(LienzoUtils.progressShapes == null){
            LienzoUtils.drawProgressBar(group, layer, panel, progressEvent);
        }
    }

}
