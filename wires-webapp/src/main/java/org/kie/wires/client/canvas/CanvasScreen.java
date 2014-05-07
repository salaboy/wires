package org.kie.wires.client.canvas;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.kie.wires.client.layers.LayersGroup;
import org.kie.wires.core.api.events.ClearEvent;
import org.kie.wires.core.api.events.ProgressEvent;
import org.kie.wires.core.api.events.ReadyShape;
import org.kie.wires.core.api.events.ShapeAddEvent;
import org.kie.wires.core.api.events.ShapeSelectedEvent;
import org.kie.wires.core.api.shapes.EditableShape;
import org.kie.wires.core.api.shapes.WiresBaseGroupShape;
import org.kie.wires.core.client.canvas.Canvas;
import org.kie.wires.core.client.shapes.ProgressBar;
import org.kie.wires.core.client.shapes.WiresLine;
import org.kie.wires.core.client.shapes.WiresRectangle;
import org.uberfire.client.annotations.WorkbenchPartTitle;
import org.uberfire.client.annotations.WorkbenchPartView;
import org.uberfire.client.annotations.WorkbenchScreen;
import org.uberfire.lifecycle.OnOpen;

import com.bayesian.network.api.events.ReadyEvent;
import com.bayesian.network.api.screen.BayesianScreen;
import com.emitrom.lienzo.client.core.shape.IPrimitive;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.ui.IsWidget;

@SuppressWarnings("unused")
@Dependent
@WorkbenchScreen(identifier = "WiresCanvasScreen")
public class CanvasScreen extends Canvas {

    @Inject
    private Event<ShapeSelectedEvent> selected;

    @Inject
    private Event<ReadyShape> readyShape;

    @Inject
    private BayesianScreen bayesianScreen;

    @Inject
    private LayersGroup layerGroup;

    public CanvasScreen() {
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
        /* This is the ugly bit that needs to be refactored to be generic */
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
        wiresShape.setDraggable(true);
        ((EditableShape) wiresShape).init(this.getX(shapeAddEvent.getX()), this.getY(shapeAddEvent.getY()));
        this.addShapeToCanvas(wiresShape);
        readyShape.fire(new ReadyShape(shape));
    }

    private int getX(int xShapeEvent) {
        int x = ((xShapeEvent - panel.getAbsoluteLeft()) < 1) ? 1 : xShapeEvent - panel.getAbsoluteLeft();
        return 25 * Math.abs(x / 25);
    }

    private int getY(int yShapeEvent) {
        int y = ((yShapeEvent - panel.getAbsoluteTop() < 1)) ? 1 : yShapeEvent - panel.getAbsoluteTop();
        return 25 * Math.abs(y / 25);
    }

    public void addNodes(@Observes ReadyEvent event) {
        GWT.log("---event.getBayesianNodes() " + event.getBayesianNodes().size());
        addShapeToCanvas(event.getBayesianNodes().get(0));
    }
    
    private void addShapeToCanvas(WiresBaseGroupShape shape){
        canvasLayer.add(shape);
        shape.setSelected(selected);
        shapesInCanvas.add((EditableShape) shape);
        canvasLayer.draw();
    }
    

    public void clearPanel(@Observes ClearEvent event) {
        for (EditableShape shape : shapesInCanvas) {
            canvasLayer.remove((IPrimitive<?>) shape);

        }
        shapesInCanvas.clear();
        canvasLayer.draw();
    }

    public void progress(@Observes ProgressEvent event) {
        if (progressBar == null) {
            progressBar = new ProgressBar(300, 34, canvasLayer);
            progressBar.setX(20).setY(10);
            canvasLayer.add(progressBar);
            canvasLayer.draw();
        }
    }

}
