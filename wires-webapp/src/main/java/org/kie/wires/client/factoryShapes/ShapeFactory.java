package org.kie.wires.client.factoryShapes;

import javax.enterprise.event.Event;

import org.kie.wires.client.events.ShapeAddEvent;

import com.emitrom.lienzo.client.core.event.NodeMouseDownEvent;
import com.emitrom.lienzo.client.core.event.NodeMouseDownHandler;
import com.emitrom.lienzo.client.core.shape.Group;
import com.emitrom.lienzo.client.core.shape.Layer;
import com.emitrom.lienzo.client.core.shape.Rectangle;
import com.emitrom.lienzo.client.core.shape.Shape;
import com.emitrom.lienzo.client.core.shape.Text;
import com.emitrom.lienzo.client.widget.LienzoPanel;
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

    protected ShapeFactory() {

    }

    protected ShapeFactory(LienzoPanel lienzoPanel, Event<ShapeAddEvent> shapeEvent) {
        panel = lienzoPanel;
        shapeAddEvent = shapeEvent;
    }

    protected LienzoPanel panel;

    protected Event<ShapeAddEvent> shapeAddEvent;

    protected abstract void drawBoundingBox(Group group);

    protected abstract Shape<T> drawShape();

    protected abstract void addShapeHandlers(Shape<T> shape, Group group);

    protected abstract void addBoundingHandlers(Rectangle boundingBox, Group group);

    protected abstract NodeMouseDownHandler getNodeMouseDownEvent(Group group);

    protected abstract int getCategory();

    protected Rectangle createBoundingBox(Group group, int shapes) {
        final Rectangle boundingBox = new Rectangle(ShapeFactoryUtil.WIDTH_BOUNDING, ShapeFactoryUtil.HEIGHT_BOUNDING);
        boundingBox.setX(this.getXBoundingBox(shapes)).setY(this.getYBoundingBox(shapes))
                .setStrokeColor(ShapeFactoryUtil.RGB_STROKE_BOUNDING).setStrokeWidth(1)
                .setFillColor(ShapeFactoryUtil.RGB_FILL_BOUNDING).setDraggable(false);
        group.add(boundingBox);
        return boundingBox;
    }

    protected void setFloatingPanel(final Shape<T> floatingShape, int height, int width, NodeMouseDownEvent event) {
        final Layer floatingLayer = new Layer();
        final LienzoPanel floatingPanel = new LienzoPanel(width, height);
        floatingLayer.add(floatingShape);
        floatingPanel.add(floatingLayer);
        floatingLayer.draw();

        final Style style = getFloatingStyle(floatingPanel, event);

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

    private double getXBoundingBox(int shapes) {
        return this.calculateX(shapes);
    }

    // this value must be calculated
    private double getYBoundingBox(int shapes) {
        return calculateY(shapes);
    }

    protected double getXText(int shapes) {
        return 12 + this.calculateX(shapes);
    }

    // this value must be calculated
    protected double getYText(int shapes) {
        return 46 + calculateY(shapes);
    }

    private Style getFloatingStyle(LienzoPanel floatingPanel, NodeMouseDownEvent event) {
        Style style = floatingPanel.getElement().getStyle();
        style.setPosition(Position.ABSOLUTE);
        style.setLeft(panel.getAbsoluteLeft() + event.getX(), Unit.PX);
        style.setTop(panel.getAbsoluteTop() + event.getY(), Unit.PX);
        style.setZIndex(100);
        return style;
    }

    protected Text createDescription(String description, int shapes) {
        Text text = new Text(description, ShapeFactoryUtil.FONT_FAMILY_DESCRIPTION, ShapeFactoryUtil.FONT_SIZE_DESCRIPTION);
        text.setX(this.getXText(shapes)).setY(this.getYText(shapes)).setFillColor(ShapeFactoryUtil.RGB_TEXT_DESCRIPTION);
        return text;
    }

    protected int calculateX(int shapes) {
        int x = shapes > 1 ? (getPositionInRow(shapes) - 1) : 0;
        return x > 0 ? (ShapeFactoryUtil.WIDTH_BOUNDING * x) + ShapeFactoryUtil.SPACE_BETWEEN_BOUNDING * x
                : ShapeFactoryUtil.WIDTH_BOUNDING * x;
    }

    protected int calculateY(int shapes) {
        int y = shapes > 1 ? this.getRow(shapes) : 0;
        return y > 0 ? (y * ShapeFactoryUtil.HEIGHT_BOUNDING) + ShapeFactoryUtil.SPACE_BETWEEN_BOUNDING * y
                        : y * ShapeFactoryUtil.HEIGHT_BOUNDING ;
    }

    private int getRow(int shapes) {
        return Math.round((shapes * ShapeFactoryUtil.WIDTH_BOUNDING) / ShapeFactoryUtil.WIDTH_STENCIL);
    }

    private int shapesByRow() {
        return Math.round(ShapeFactoryUtil.WIDTH_STENCIL / ShapeFactoryUtil.WIDTH_BOUNDING);
    }

    private int getPositionInRow(int shapes) {
        return (shapes - shapesByRow()) >= 1 ? (shapes - (shapesByRow() * getRow(shapes))) : shapes;
    }

}