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
    
    
    protected ShapeFactory(){
        
    }
    
    protected ShapeFactory(LienzoPanel lienzoPanel, Event<ShapeAddEvent> shapeEvent){
        panel = lienzoPanel;
        shapeAddEvent = shapeEvent;
    }
    
    
    //these constant could be extracted
	private static final String RGB_FILL_BOUNDING = Color.rgbToBrowserHexColor(255, 255, 255);

    private static final String RGB_STROKE_BOUNDING = Color.rgbToBrowserHexColor(219, 217, 217);
    
    protected static String RGB_TEXT_DESCRIPTION = Color.rgbToBrowserHexColor(188, 187, 189);
    
    protected static final String RGB_STROKE_SHAPE = Color.rgbToBrowserHexColor(255, 0, 0);
    
    protected static final String RGB_FILL_SHAPE = Color.rgbToBrowserHexColor(240, 240, 240);
    
    protected static final int RGB_STROKE_WIDTH_SHAPE = 3;

    protected static String FONT_FAMILY_DESCRIPTION = "oblique normal";

    protected static double FONT_SIZE_DESCRIPTION = 10;

    protected LienzoPanel panel;

    protected Event<ShapeAddEvent> shapeAddEvent;
    
    

    protected abstract void drawBoundingBox(Group group);

    protected abstract Shape<T> drawShape(Group group);

    protected abstract void addShapeHandlers(Shape<T> shape, Group group);
    
    protected abstract void addBoundingHandlers(Rectangle boundingBox, Group group);
    
    protected abstract NodeMouseDownHandler getNodeMouseDownEvent(Group group);
    
    protected Rectangle createBoundingBox(Group group) {
        final Rectangle boundingBox = new Rectangle(50, 50);
        boundingBox.setX(this.getXBoundingBox(group)).setY(this.getYBoundingBox(group)).setStrokeColor(RGB_STROKE_BOUNDING).setStrokeWidth(1)
                .setFillColor(RGB_FILL_BOUNDING).setDraggable(false);
        group.add(boundingBox);
        return boundingBox;
    }
    
    protected void setFloatingPanel(final Shape<T> floatingShape, int height, int width, NodeMouseDownEvent event){
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
    
    // this value must be calculated 
    private double getXBoundingBox(Group group){
        return 0;
    }
    
    // this value must be calculated
    private double getYBoundingBox(Group group){
        return 0;
    }
    
    // this value must be calculated
    protected double getXText(Group group){
        return 12;
    }
    
    // this value must be calculated
    protected double getYText(Group group){
        return 46;
    }
    
    private Style getFloatingStyle(LienzoPanel floatingPanel, NodeMouseDownEvent event){
        Style style = floatingPanel.getElement().getStyle();
        style.setPosition(Position.ABSOLUTE);
        style.setLeft(panel.getAbsoluteLeft() + event.getX(), Unit.PX);
        style.setTop(panel.getAbsoluteTop() + event.getY(), Unit.PX);
        style.setZIndex(100);
        return style;
    }
    
    protected Text createDescription(Group group, String description){
        Text text = new Text(description, FONT_FAMILY_DESCRIPTION, FONT_SIZE_DESCRIPTION);
        text.setX(this.getXText(group)).setY(this.getYText(group)).setFillColor(RGB_TEXT_DESCRIPTION);
        return text;
    }
    
}
