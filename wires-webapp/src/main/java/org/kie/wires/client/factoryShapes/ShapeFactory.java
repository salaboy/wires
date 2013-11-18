package org.kie.wires.client.factoryShapes;

import javax.enterprise.event.Event;

import org.kie.wires.client.events.ShapeAddEvent;
import org.kie.wires.client.shapes.EditableLine;

import com.emitrom.lienzo.client.core.event.NodeMouseDownEvent;
import com.emitrom.lienzo.client.core.event.NodeMouseDownHandler;
import com.emitrom.lienzo.client.core.shape.Group;
import com.emitrom.lienzo.client.core.shape.Layer;
import com.emitrom.lienzo.client.core.shape.Line;
import com.emitrom.lienzo.client.core.shape.Rectangle;
import com.emitrom.lienzo.client.core.shape.Shape;
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
import com.google.gwt.user.client.ui.RootPanel;

public abstract class ShapeFactory<T extends Shape<T>> {

    public static String fontFamily = "oblique normal";

    public static double fontSize = 11;

    public static String textColorDescription = Color.rgbToBrowserHexColor(188, 187, 189);

    public LienzoPanel panel;

    public Event<ShapeAddEvent> shapeAddEvent;

    public abstract void drawBoundingBox(Group group);

    public abstract Shape<T> drawShape();

    public abstract void createDescription(Group group);

    public abstract void addHandlers(Shape<T> shape, Group group);

    public void createBoundingBox(Group group) {
        final Rectangle boundingBox = new Rectangle(50, 50);
        boundingBox.setX(0).setY(0).setStrokeColor(Color.rgbToBrowserHexColor(219, 217, 217)).setStrokeWidth(1)
                .setFillColor(Color.rgbToBrowserHexColor(255, 255, 255)).setDraggable(false);

        group.add(boundingBox);

        boundingBox.addNodeMouseDownHandler(new NodeMouseDownHandler() {
            public void onNodeMouseDown(NodeMouseDownEvent event) {
                //TODO this logic should be changed 
                final Layer floatingLayer = new Layer();
                final LienzoPanel floatingPanel = new LienzoPanel(30, 30);
                final Style style = floatingPanel.getElement().getStyle();
                style.setPosition(Position.ABSOLUTE);
                style.setLeft(panel.getAbsoluteLeft() + event.getX(), Unit.PX);
                style.setTop(panel.getAbsoluteTop() + event.getY(), Unit.PX);
                style.setZIndex(100);

                final Line floatingShape = new EditableLine(0, 0, 30, 30);
                floatingShape.setStrokeColor(Color.rgbToBrowserHexColor(255, 0, 0)).setStrokeWidth(2).setDraggable(false);

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
