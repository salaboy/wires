package org.kie.wires.client.shapes;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import com.emitrom.lienzo.client.core.event.NodeMouseDownEvent;
import com.emitrom.lienzo.client.core.event.NodeMouseDownHandler;
import com.emitrom.lienzo.client.core.shape.Circle;
import com.emitrom.lienzo.client.core.shape.Group;
import com.emitrom.lienzo.client.core.shape.Layer;
import com.emitrom.lienzo.client.core.shape.Rectangle;
import com.emitrom.lienzo.client.core.types.Point2D;
import com.emitrom.lienzo.client.widget.DragConstraintEnforcer;
import com.emitrom.lienzo.client.widget.DragContext;
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
import org.kie.wires.client.events.ShapeAddEvent;

@Dependent
public class ShapesGroup extends Composite {

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
        group1.setX(5).setY(5);
        layer.add(group1);
        createRectangleStencil(group1);

        layer.draw();
    }

    private void createRectangleStencil(Group group) {
        final Rectangle rectangle = new Rectangle(30, 30);
        rectangle.setX(0).setY(0)
                .setStrokeColor(Color.rgbToBrowserHexColor(255, 0, 0))
                .setStrokeWidth(3)
                .setFillColor(Color.rgbToBrowserHexColor(0, 255, 255))
                .setDraggable(false);

        group.add(rectangle);

        rectangle.addNodeMouseDownHandler(new NodeMouseDownHandler() {
            public void onNodeMouseDown(NodeMouseDownEvent event) {
                final Layer floatingLayer = new Layer();
                final LienzoPanel floatingPanel = new LienzoPanel(30, 30);
                final Style style = floatingPanel.getElement().getStyle();
                style.setPosition(Position.ABSOLUTE);
                style.setLeft(panel.getAbsoluteLeft() + event.getX(), Unit.PX);
                style.setTop(panel.getAbsoluteTop() + event.getY(), Unit.PX);
                style.setZIndex(100);

                final EditableRectangle floatingShape = new EditableRectangle(30, 30) {

                    public DragConstraintEnforcer getDragConstraints() {
                        return new DragConstraintEnforcer() {

                            public void adjust(Point2D dxy) {
                                dxy.setX(snap(dxy.getX()));
                                dxy.setY(snap(dxy.getY()));
                            }

                            private double snap(double x) {
                                int w = 25;
                                return w * Math.round(x / w);
                            }

                            public void startDrag(DragContext dragContext) {
                                // not used
                            }
                        };
                    }
                };

                floatingShape.setX(0).setY(0)
                        .setStrokeColor(Color.rgbToBrowserHexColor(255, 0, 0))
                        .setStrokeWidth(3)
                        .setFillColor(Color.rgbToBrowserHexColor(0, 255, 255))
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

//        final Circle circle = new Circle(15);
//        circle.setX(80).setY(15)
//                .setStrokeColor(Color.rgbToBrowserHexColor(255, 0, 0))
//                .setStrokeWidth(3)
//                .setFillColor(Color.rgbToBrowserHexColor(0, 255, 255))
//                .setDraggable(false);
//
//        group.add(circle);
//
//        circle.addNodeMouseDownHandler(new NodeMouseDownHandler() {
//            public void onNodeMouseDown(NodeMouseDownEvent event) {
//                final Layer floatingLayer = new Layer();
//                final LienzoPanel floatingPanel = new LienzoPanel(30, 30);
//                final Style style = floatingPanel.getElement().getStyle();
//                style.setPosition(Position.ABSOLUTE);
//                style.setLeft(panel.getAbsoluteLeft() + event.getX(), Unit.PX);
//                style.setTop(panel.getAbsoluteTop() + event.getY(), Unit.PX);
//                style.setZIndex(100);
//                final EditableCircle floatingShape = new EditableCircle(15) {
//
//                    public DragConstraintEnforcer getDragConstraints() {
//                        return new DragConstraintEnforcer() {
//
//                            public void adjust(Point2D dxy) {
//                                dxy.setX(snap(dxy.getX()));
//                                dxy.setY(snap(dxy.getY()));
//                            }
//
//                            private double snap(double x) {
//                                int w = 25;
//                                return w * Math.round(x / w);
//                            }
//
//                            public void startDrag(DragContext dragContext) {
//                                // not used
//                            }
//                        };
//                    }
//                };
//
//                floatingShape.setX(0).setY(0)
//                        .setStrokeColor(Color.rgbToBrowserHexColor(255, 0, 0))
//                        .setStrokeWidth(3)
//                        .setFillColor(Color.rgbToBrowserHexColor(0, 255, 255))
//                        .setDraggable(false);
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
//            
//        });

    }
}
