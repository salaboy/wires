package org.kie.wires.client.connectors;

import com.emitrom.lienzo.client.core.event.NodeMouseDownEvent;
import com.emitrom.lienzo.client.core.event.NodeMouseDownHandler;
import com.emitrom.lienzo.client.core.shape.Group;
import com.emitrom.lienzo.client.core.shape.Layer;
import com.emitrom.lienzo.client.core.shape.Line;
import com.emitrom.lienzo.client.core.shape.Rectangle;
import com.emitrom.lienzo.client.core.types.DragBounds;
import com.emitrom.lienzo.client.widget.LienzoPanel;
import com.emitrom.lienzo.shared.core.types.Color;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.kie.wires.client.events.ShapeAddEvent;
import org.kie.wires.client.shapes.EditableBezierCurve;
import org.kie.wires.client.shapes.EditableLine;

@Dependent
public class ConnectorsGroup extends Composite {

    @Inject
    private Event<ShapeAddEvent> shapeAddEvent;

    private Layer layer;

    private LienzoPanel panel;

    @PostConstruct
    public void init() {
        panel = new LienzoPanel(200, 300);
        initWidget(panel);

        layer = new Layer();
        panel.add(layer);

        Group group1 = new Group();
        group1.setX(0).setY(5);
        layer.add(group1);
        
//        createBezierConnector(group1);
        
        createBoundingBox(group1);
        
        createLineConnector(group1);

        layer.draw();
    }

//    private void createBezierConnector(Group group) {
//        BezierCurve curve = new EditableBezierCurve(0, 0, 25, -5, 5, 35, 30, 30);
//        curve.setStrokeColor(Color.rgbToBrowserHexColor(255, 0, 0))
//                .setStrokeWidth(2)
//                .setDraggable(false);
//
//        group.add(curve);
//
//        curve.addNodeMouseDownHandler(new NodeMouseDownHandler() {
//            public void onNodeMouseDown(NodeMouseDownEvent event) {
//                final Layer floatingLayer = new Layer();
//                final LienzoPanel floatingPanel = new LienzoPanel(30, 30);
//                final Style style = floatingPanel.getElement().getStyle();
//                style.setPosition(Position.ABSOLUTE);
//                style.setLeft(panel.getAbsoluteLeft() + event.getX(), Unit.PX);
//                style.setTop(panel.getAbsoluteTop() + event.getY(), Unit.PX);
//                style.setZIndex(100);
//
//                final BezierCurve floatingShape = new EditableBezierCurve(0, 0, 25, -5, 5, 35, 30, 30);
//                floatingShape.setStrokeColor(Color.rgbToBrowserHexColor(255, 0, 0))
//                        .setStrokeWidth(2)
//                        .setDraggable(false);
//
//                floatingLayer.add(floatingShape);
//                floatingPanel.add(floatingLayer);
//                floatingLayer.draw();
//
//                RootPanel.get().add(floatingPanel);
//
//                final HandlerRegistration[] handlerRegs = new HandlerRegistration[2];
//
//                handlerRegs[0] = RootPanel.get().addDomHandler(new MouseMoveHandler() {
//                    public void onMouseMove(MouseMoveEvent mouseMoveEvent) {
//                        style.setLeft(mouseMoveEvent.getX(), Unit.PX);
//                        style.setTop(mouseMoveEvent.getY(), Unit.PX);
//                    }
//                }, MouseMoveEvent.getType());
//
//                handlerRegs[1] = RootPanel.get().addDomHandler(new MouseUpHandler() {
//                    public void onMouseUp(MouseUpEvent mouseUpEvent) {
//                        handlerRegs[0].removeHandler();
//                        handlerRegs[1].removeHandler();
//                        RootPanel.get().remove(floatingPanel);
//                        shapeAddEvent.fire(new ShapeAddEvent(floatingShape, mouseUpEvent.getX(), mouseUpEvent.getY()));
//                    }
//                }, MouseUpEvent.getType());
//            }
//        });
//    }

    private void createBoundingBox(Group group) {
        
        final Rectangle boundingBox = new Rectangle(32, 32);
        boundingBox.setX(0).setY(0)
                .setStrokeColor(Color.rgbToBrowserHexColor(219, 217, 217))
                .setStrokeWidth(1)
                .setFillColor(Color.rgbToBrowserHexColor(255, 255, 255))
                .setDraggable(false);

        group.add(boundingBox);

        boundingBox.addNodeMouseDownHandler(new NodeMouseDownHandler() {
            public void onNodeMouseDown(NodeMouseDownEvent event) {
                final Layer floatingLayer = new Layer();
                final LienzoPanel floatingPanel = new LienzoPanel(30, 30);
                final Style style = floatingPanel.getElement().getStyle();
                style.setPosition(Position.ABSOLUTE);
                style.setLeft(panel.getAbsoluteLeft() + event.getX(), Unit.PX);
                style.setTop(panel.getAbsoluteTop() + event.getY(), Unit.PX);
                style.setZIndex(100);

                final Line floatingShape = new EditableLine(0, 0, 30, 30);
                floatingShape.setStrokeColor(Color.rgbToBrowserHexColor(255, 0, 0))
                        .setStrokeWidth(2)
                        .setDraggable(false);

                floatingLayer.add(floatingShape);
                floatingPanel.add(floatingLayer);
                floatingLayer.draw();

                RootPanel.get().add(floatingPanel);

                final HandlerRegistration[] handlerRegs = new HandlerRegistration[2];

                handlerRegs[0] = RootPanel.get().addDomHandler(new MouseMoveHandler() {
                    public void onMouseMove(MouseMoveEvent mouseMoveEvent) {
                        style.setLeft(mouseMoveEvent.getX(), Unit.PX);
                        style.setTop(mouseMoveEvent.getY(), Unit.PX);
                    }
                }, MouseMoveEvent.getType());

                handlerRegs[1] = RootPanel.get().addDomHandler(new MouseUpHandler() {
                    public void onMouseUp(MouseUpEvent mouseUpEvent) {
                        handlerRegs[0].removeHandler();
                        handlerRegs[1].removeHandler();
                        RootPanel.get().remove(floatingPanel);
                        shapeAddEvent.fire(new ShapeAddEvent(floatingShape, mouseUpEvent.getX(), mouseUpEvent.getY()));
                    }
                }, MouseUpEvent.getType());
            }
        });
    }     
    
    private void createLineConnector(Group group) {
        Line line = new EditableLine(0, 0, 30, 30);
        line.setDragBounds(new DragBounds(150, 260, 150, 150));
        line.setStrokeColor(Color.rgbToBrowserHexColor(255, 0, 0))
                .setStrokeWidth(2)
                .setDraggable(false);

        group.add(line);

        line.addNodeMouseDownHandler(new NodeMouseDownHandler() {
            public void onNodeMouseDown(NodeMouseDownEvent event) {
                final Layer floatingLayer = new Layer();
                final LienzoPanel floatingPanel = new LienzoPanel(30, 30);
                final Style style = floatingPanel.getElement().getStyle();
                style.setPosition(Position.ABSOLUTE);
                style.setLeft(panel.getAbsoluteLeft() + event.getX(), Unit.PX);
                style.setTop(panel.getAbsoluteTop() + event.getY(), Unit.PX);
                style.setZIndex(100);

                final Line floatingShape = new EditableLine(0, 0, 30, 30);
                floatingShape.setStrokeColor(Color.rgbToBrowserHexColor(255, 0, 0))
                        .setStrokeWidth(2)
                        .setDraggable(false);

                floatingLayer.add(floatingShape);
                floatingPanel.add(floatingLayer);
                floatingLayer.draw();

                RootPanel.get().add(floatingPanel);

                final HandlerRegistration[] handlerRegs = new HandlerRegistration[2];

                handlerRegs[0] = RootPanel.get().addDomHandler(new MouseMoveHandler() {
                    public void onMouseMove(MouseMoveEvent mouseMoveEvent) {
                        style.setLeft(mouseMoveEvent.getX(), Unit.PX);
                        style.setTop(mouseMoveEvent.getY(), Unit.PX);
                    }
                }, MouseMoveEvent.getType());

                handlerRegs[1] = RootPanel.get().addDomHandler(new MouseUpHandler() {
                    public void onMouseUp(MouseUpEvent mouseUpEvent) {
                        handlerRegs[0].removeHandler();
                        handlerRegs[1].removeHandler();
                        RootPanel.get().remove(floatingPanel);
                        shapeAddEvent.fire(new ShapeAddEvent(floatingShape, mouseUpEvent.getX(), mouseUpEvent.getY()));
                    }
                }, MouseUpEvent.getType());
            }
        });
    }
}
