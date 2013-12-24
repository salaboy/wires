/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kie.wires.client.shapes.collision;

import com.emitrom.lienzo.client.core.event.NodeDragEndEvent;
import com.emitrom.lienzo.client.core.event.NodeDragEndHandler;
import com.emitrom.lienzo.client.core.event.NodeDragMoveEvent;
import com.emitrom.lienzo.client.core.event.NodeDragMoveHandler;
import com.emitrom.lienzo.client.core.event.NodeDragStartEvent;
import com.emitrom.lienzo.client.core.event.NodeDragStartHandler;
import com.emitrom.lienzo.client.core.shape.Layer;
import com.emitrom.lienzo.client.core.shape.Line;
import com.emitrom.lienzo.client.core.shape.Rectangle;
import com.emitrom.lienzo.client.core.types.Point2DArray;
import com.emitrom.lienzo.shared.core.types.ColorName;
import com.google.gwt.core.client.GWT;
import org.kie.wires.client.shapes.EditableLine;
import org.kie.wires.client.shapes.ShapesUtils;
import org.kie.wires.client.util.UUID;

/**
 *
 * @author salaboy
 */
public class LineControlPointImpl extends Rectangle implements ControlPoint {

    private String id;

    private double initialStartPointX;
    private double initialStartPointY;
    private double initialEndPointX;
    private double initialEndPointY;

    private double dragEventStartX;
    private double dragEventStartY;

    private double dragEventEndX;
    private double dragEventEndY;

    private Line shape;
    private int controlType = 0;

    public LineControlPointImpl(Line shape, int controlType) {
        this(10, 10);
        this.shape = shape;
        this.controlType = controlType;
    }

    public LineControlPointImpl(double width, double height) {
        super(width, height);
        setFillColor(ShapesUtils.LIGHT_BLUE);
        id = UUID.uuid();
    }

    public int getControlType() {
        return controlType;
    }

    public Line getShape() {
        return shape;
    }

    public void setShape(Line shape) {
        this.shape = shape;
    }

    public String getId() {
        return id;
    }

    public void initControlPoint(final Layer layer) {

        moveControlPoint(layer);

        switch (controlType) {
            case ControlPoint.CONTROL_START:

                addNodeDragStartHandler(new NodeDragStartHandler() {
                    public void onNodeDragStart(NodeDragStartEvent nodeDragStartEvent) {
                        dragEventStartX = nodeDragStartEvent.getX();
                        dragEventStartY = nodeDragStartEvent.getY();
                        initialStartPointX = shape.getPoints().getPoint(0).getX();
                        initialStartPointY = shape.getPoints().getPoint(0).getY();
                    }
                });

                addNodeDragMoveHandler(new NodeDragMoveHandler() {
                    public void onNodeDragMove(NodeDragMoveEvent nodeDragMoveEvent) {
                        ((EditableLine) shape).setBeingResized(true);
                        double deltaX = nodeDragMoveEvent.getX() - dragEventStartX;
                        double deltaY = nodeDragMoveEvent.getY() - dragEventStartY;

                        Point2DArray array = shape.getPoints();
                        array.getPoint(0).setX(initialStartPointX + deltaX);
                        array.getPoint(0).setY(initialStartPointY + deltaY);

                        layer.draw();
                    }
                });

                addNodeDragEndHandler(new NodeDragEndHandler() {
                    public void onNodeDragEnd(NodeDragEndEvent nodeDragEndEvent) {
                        ((EditableLine) shape).setBeingResized(false);

                    }
                });

                setDraggable(true).setStrokeWidth(1)
                        .setStrokeColor(ColorName.BLACK);

                layer.add(this);
                break;
            case ControlPoint.CONTROL_END:

                addNodeDragStartHandler(new NodeDragStartHandler() {
                    public void onNodeDragStart(NodeDragStartEvent nodeDragStartEvent) {
                        dragEventEndX = nodeDragStartEvent.getX();
                        dragEventEndY = nodeDragStartEvent.getY();
                        initialEndPointX = shape.getPoints().getPoint(1).getX();
                        initialEndPointY = shape.getPoints().getPoint(1).getY();

                    }
                });

                addNodeDragMoveHandler(new NodeDragMoveHandler() {
                    public void onNodeDragMove(NodeDragMoveEvent nodeDragMoveEvent) {
                        ((EditableLine) shape).setBeingResized(true);
                        double deltaX = nodeDragMoveEvent.getX() - dragEventEndX;
                        double deltaY = nodeDragMoveEvent.getY() - dragEventEndY;

                        Point2DArray array = shape.getPoints();
                        array.getPoint(1).setX(initialEndPointX + deltaX);
                        array.getPoint(1).setY(initialEndPointY + deltaY);

                        layer.draw();
                    }
                });

                addNodeDragEndHandler(new NodeDragEndHandler() {
                    public void onNodeDragEnd(NodeDragEndEvent nodeDragEndEvent) {
                        ((EditableLine) shape).setBeingResized(false);

                    }
                });
                setDraggable(true)
                        .setStrokeWidth(1)
                        .setStrokeColor(ColorName.BLACK);
                layer.add(this);
                break;
        }

    }

    public void udpateShape(Layer layer, double x, double y) {
        Point2DArray array = shape.getPoints();
        
        switch (controlType) {
            case ControlPoint.CONTROL_START:
                array.getPoint(0).setX( x);
                array.getPoint(0).setY( y);
        
                layer.draw();
                break;
            case ControlPoint.CONTROL_END:
                array.getPoint(1).setX(x);
                array.getPoint(1).setY(y);
                layer.draw();
                break;
        }

    }

    public void setControlPointX(double x) {
        setX(x);
    }

    public void setControlPointY(double y) {
        setY(y);
    }

    @Override
    public String toString() {
        return "LineControlPointImpl{" + "id=" + id + '}';
    }

    public void setControlPointVisible(boolean visible) {
        setVisible(visible);
    }

    public void moveControlPoint(Layer layer) {
        Point2DArray array = shape.getPoints();
        switch (controlType) {
            case ControlPoint.CONTROL_START:

                setX(shape.getX() + array.getPoint(0).getX() - 5);
                setY(shape.getY() + array.getPoint(0).getY() - 5);
                break;
            case ControlPoint.CONTROL_END:

                setX(shape.getX() + array.getPoint(1).getX() - 5);
                setY(shape.getY() + array.getPoint(1).getY() - 5);
                break;
        }

    }

}
