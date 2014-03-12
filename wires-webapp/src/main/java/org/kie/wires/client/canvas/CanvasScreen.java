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
import org.kie.wires.client.events.ClearEvent;
import org.kie.wires.client.events.LayerEvent;
import org.kie.wires.client.events.ProbabilityEvent;
import org.kie.wires.client.events.ProgressEvent;
import org.kie.wires.client.events.ReadyEvent;
import org.kie.wires.client.events.ShapeAddEvent;
import org.kie.wires.client.shapes.api.WiresBaseGroupShape;
import org.kie.wires.client.shapes.WiresRectangle;
import org.kie.wires.client.shapes.api.EditableShape;
import org.uberfire.client.annotations.WorkbenchPartTitle;
import org.uberfire.client.annotations.WorkbenchPartView;
import org.uberfire.client.annotations.WorkbenchScreen;
import org.uberfire.lifecycle.OnOpen;

import com.bayesian.parser.client.service.BayesianService;
import com.emitrom.lienzo.client.core.shape.GridLayer;
import com.emitrom.lienzo.client.core.shape.IPrimitive;
import com.emitrom.lienzo.client.core.shape.Layer;
import com.emitrom.lienzo.client.core.shape.Line;
import com.emitrom.lienzo.client.widget.LienzoPanel;
import com.emitrom.lienzo.shared.core.types.ColorName;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RequiresResize;
import org.kie.wires.client.events.ShapeSelectedEvent;
import org.kie.wires.client.shapes.WiresLine;

@Dependent
@WorkbenchScreen(identifier = "WiresCanvasScreen")
public class CanvasScreen extends Composite implements RequiresResize {

    private LienzoPanel panel;
    private Layer layer;

    /*
     * Please remove this from here!!!
     */
    @Inject
    private Caller<BayesianService> bayesianService;
    @Inject
    private Event<ProbabilityEvent> probabilityEvent;

    /*
     * If these are two are generic enough we can keep them here
     */
    @Inject
    private Event<ProgressEvent> progressEvent;

    @Inject
    private Event<ReadyEvent> readyEvent;

    @Inject
    Event<ShapeSelectedEvent> selected;

    @Inject
    private Event<LayerEvent> layerEvent;

    public static final List<WiresBaseGroupShape> shapesInCanvas = new ArrayList<WiresBaseGroupShape>();

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

        panel.setBackgroundLayer(gridLayer);

        gridLayer.moveToBottom();
        gridLayer.setListening(false);
        layer = new Layer();
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
    @Override
    public String getTitle() {
        return "Canvas";
    }

    @WorkbenchPartView
    public IsWidget getView() {
        return this;
    }

    public void myResponseObserver(@Observes ShapeAddEvent shapeAddEvent) {
        String shape = shapeAddEvent.getShape();

        /*This is the ugly bit that needs to be refactored to be generic */
        WiresBaseGroupShape wiresShape = null;
        if (shape.equals("WiresRectangle")) {
            wiresShape = new WiresRectangle(70, 40);
        } else if (shape.equals("WiresLine")) {
            wiresShape = new WiresLine(0, 0, 30, 30);
        }

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

        wiresShape.setDraggable(true);
        layer.add(wiresShape);

        wiresShape.init(x, y);

        wiresShape.setSelected(selected);

        shapesInCanvas.add(wiresShape);

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
    /*
     * We need to move this from here as soon as possible!!
     */

    public void addNewPanel(@Observes BayesianEvent event) {

        new BayesianFactory(bayesianService, event.getTemplate(), layer, layerEvent, probabilityEvent, readyEvent,
                progressEvent);

    }

    public void addNodes(@Observes ReadyEvent event) {
        for (WiresRectangle shape : event.getBayesianNodes()) {
            layer.add(shape);
            shape.setSelected(selected);
            shapesInCanvas.add(shape);
        }
        layer.draw();

    }

    public void clearPanel(@Observes ClearEvent event) {
        for (WiresBaseGroupShape shape : shapesInCanvas) {
            layer.remove((IPrimitive<?>) shape);

        }
        shapesInCanvas.clear();
        layer.draw();
    }

}
