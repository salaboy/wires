package org.kie.wires.client.factoryShapes;

import javax.enterprise.event.Event;

import org.kie.wires.client.events.ShapeAddEvent;
import org.kie.wires.client.shapes.EditableLine;

import com.emitrom.lienzo.client.core.event.NodeMouseDownEvent;
import com.emitrom.lienzo.client.core.event.NodeMouseDownHandler;
import com.emitrom.lienzo.client.core.shape.Group;
import com.emitrom.lienzo.client.core.shape.Layer;
import com.emitrom.lienzo.client.core.shape.Line;
import com.emitrom.lienzo.client.core.shape.Shape;
import com.emitrom.lienzo.client.core.shape.Text;
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
import com.google.gwt.user.client.ui.RootPanel;

public class LineFactory extends ShapeFactory<Line>{
    
    private static String description = "Line";
    
    public LineFactory(){
    }
    
    public LineFactory(Group group, LienzoPanel panel, Event<ShapeAddEvent> shapeAddEvent){
        super.panel = panel;
        super.shapeAddEvent = shapeAddEvent;
        this.drawBoundingBox(group);
    }

    @Override
    public void drawBoundingBox(Group group) {
        createBoundingBox(group);
        addHandlers(drawShape(), group);
        createDescription(group);
    }

    @Override
    public void createDescription(Group group) {
        Text text = new Text(description, fontFamily, fontSize);
        text.setX(12).setY(45).setFillColor(textColorDescription);
        group.add(text);
        
    }

    @Override
    public Shape<Line> drawShape() {
        Line line = new EditableLine(12, 8, 42, 30);
        line.setDragBounds(new DragBounds(150, 260, 150, 150));
        line.setStrokeColor(Color.rgbToBrowserHexColor(255, 0, 0))
                .setStrokeWidth(2)
                .setDraggable(false);
        return line;
    }

    @Override
    public void addHandlers(Shape<Line> shape, Group group) {
        shape.addNodeMouseDownHandler(new NodeMouseDownHandler() {
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
        
        group.add(shape);
        
        
        
    }



}
