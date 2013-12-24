package org.kie.wires.client.canvas;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Observes;

import com.emitrom.lienzo.client.core.shape.GridLayer;
import com.emitrom.lienzo.client.core.shape.IPrimitive;
import com.emitrom.lienzo.client.core.shape.Layer;
import com.emitrom.lienzo.client.core.shape.Line;
import com.emitrom.lienzo.client.core.shape.Shape;
import com.emitrom.lienzo.client.widget.LienzoPanel;
import com.emitrom.lienzo.shared.core.types.ColorName;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RequiresResize;
import java.util.List;
import org.kie.wires.client.events.ShapeAddEvent;
import org.kie.wires.client.shapes.EditableLine;
import org.kie.wires.client.shapes.EditableRectangle;
import org.kie.wires.client.shapes.EditableShape;
import org.kie.wires.client.shapes.collision.CollidableShape;
import org.kie.wires.client.shapes.collision.Magnet;
import org.kie.wires.client.shapes.collision.StickableShape;
import org.uberfire.client.annotations.WorkbenchPartTitle;
import org.uberfire.client.annotations.WorkbenchPartView;
import org.uberfire.client.annotations.WorkbenchScreen;
import org.uberfire.lifecycle.OnOpen;

@Dependent
@WorkbenchScreen(identifier = "WiresCanvasScreen")
public class CanvasScreen extends Composite implements RequiresResize {

    private LienzoPanel panel;
    private Layer layer;
    private boolean shapeBeingDragged;

    public CanvasScreen() {
    }

    @PostConstruct
    public void init() {
        panel = new LienzoPanel(800, 600);

        initWidget(panel);

        Line line1 = new Line(0, 0, 0, 0).setStrokeColor(ColorName.BLUE).setAlpha(0.5);  // primary line
        Line line2 = new Line(0, 0, 0, 0).setStrokeColor(ColorName.GREEN).setAlpha(0.5); // secondary line
        line2.setDashArray(2, 2); // the secondary lines are dashed lines

        GridLayer gridLayer = new GridLayer(100, line1, 25, line2);

        panel.add(gridLayer);

        layer = new Layer();

        panel.add(layer);

//        panel.addMouseDownHandler(new MouseDownHandler() {
//
//            public void onMouseDown(MouseDownEvent event) {
//                shapeBeingDragged = true;
//            }
//        });
//
//        panel.addMouseUpHandler(new MouseUpHandler() {
//
//            public void onMouseUp(MouseUpEvent event) {
//                shapeBeingDragged = false;
//            }
//        });
        panel.addMouseMoveHandler(new MouseMoveHandler() {
            public void onMouseMove(MouseMoveEvent event) {

                detectCollisions(event);

            }
        });

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
        } else if (shapeAddEvent.getX() > panel.getAbsoluteLeft() + panel.getWidth() || shapeAddEvent.getY() > panel.getAbsoluteTop() + panel.getHeight()) {
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

        ((EditableShape) shape).init(x, y);

        layer.add(shape);

        layer.draw();
    }

    public void detectCollisions(MouseMoveEvent event) {
        EditableShape shapeActive = null;
        //if (shapeBeingDragged) {

        for (IPrimitive<?> iPrimitive : layer) {
            if (iPrimitive instanceof EditableShape) {
                if (((EditableShape) iPrimitive).isBeingDragged() || ((EditableShape) iPrimitive).isBeingResized()) {
                    shapeActive = ((EditableShape) iPrimitive);
                }

            }
        }

        if (shapeActive != null) {
            for (IPrimitive<?> iPrimitive : layer) {
                if (iPrimitive instanceof EditableShape) {

                    if (!((EditableShape) iPrimitive).getId().equals(shapeActive.getId())
                            && ((CollidableShape) shapeActive).collidesWith(((CollidableShape) iPrimitive))) {
                        ((StickableShape) iPrimitive).showMagnetsPoints();

                        List<Magnet> magnets = ((StickableShape) iPrimitive).getMagnets();
                        double finalDistance = 1000;
                        Magnet selectedMagnet = null;
                        for (Magnet magnet : magnets) {
                            double deltaX = event.getX() - magnet.getX();
                            double deltaY = event.getY() - magnet.getY();
                            double distance = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));

                            if (finalDistance > distance) {
                                finalDistance = distance;
                                selectedMagnet = magnet;
                            }
                            magnet.setMagnetActive(false);
                        }

                        if (selectedMagnet != null) {
                            selectedMagnet.setMagnetActive(true);
//                            if(shapeActive instanceof EditableLine){
//                                selectedMagnet.attachControlPoint(((EditableLine) shapeActive).getStartControlPoint());
//                            }

                            //shapeActive.showMagnetsPoints();
//                            List<Shape> magnets2 = shapeActive.getMagnets();
//
//                            finalDistance = 1000;
//                            Shape selectedMagnet2 = null;
//                            for (Shape magnet : magnets2) {
//                                double deltaX = selectedMagnet.getX() - magnet.getX();
//                                double deltaY = selectedMagnet.getY() - magnet.getY();
//                                double distance = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
//
//                                if (finalDistance > distance) {
//                                    finalDistance = distance;
//                                    selectedMagnet2 = magnet;
//                                }
//                                magnet.setScale(1);
//
//                            }
//                            if (selectedMagnet2 != null) {
//                                selectedMagnet2.setFillColor(ColorName.GREEN);
//                                selectedMagnet2.setScale(2);
//                                selectedMagnet2.setAlpha(0.5);
//                            }

                        }

                    } else {
                         ((StickableShape) iPrimitive).hideMagnetPoints();
                       
                    }
                }
            }
        }
        // }

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

}
