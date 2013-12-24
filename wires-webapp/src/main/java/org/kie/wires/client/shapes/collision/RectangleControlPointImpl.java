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
import com.emitrom.lienzo.client.core.shape.Rectangle;
import com.emitrom.lienzo.shared.core.types.ColorName;
import org.kie.wires.client.shapes.EditableRectangle;
import org.kie.wires.client.shapes.ShapesUtils;
import static org.kie.wires.client.shapes.collision.ControlPoint.*;
import org.kie.wires.client.util.UUID;

/**
 *
 * @author salaboy
 */
public class RectangleControlPointImpl extends Rectangle implements ControlPoint {

    private double dragEventStartX;
    private double dragEventStartY;

    private String id;

    private Rectangle shape;

    private int controlType;

    public RectangleControlPointImpl(Rectangle shape, int controlType) {
        this(10, 10);
        this.shape = shape;
        this.controlType = controlType;
    }

    public RectangleControlPointImpl(double width, double height) {
        super(width, height);
        setFillColor(ShapesUtils.LIGHT_BLUE);
        id = UUID.uuid();
    }

    public String getId() {
        return id;
    }

    public Rectangle getShape() {
        return shape;
    }

    public void initControlPoint(Layer layer) {

        moveControlPoint(layer);

        setDraggable(true)
                .setStrokeWidth(1)
                .setStrokeColor(ColorName.BLACK);

        addNodeDragStartHandler(new NodeDragStartHandler() {
            public void onNodeDragStart(NodeDragStartEvent nodeDragStartEvent) {

                recordStartData(shape, nodeDragStartEvent);
            }
        });

        addNodeDragMoveHandler(new NodeDragMoveHandler() {
            public void onNodeDragMove(NodeDragMoveEvent nodeDragMoveEvent) {
                Layer layer = getLayer();
                ((EditableRectangle) shape).setBeingResized(true);
                nodeDragMove((EditableRectangle) shape, nodeDragMoveEvent, layer);
                if (((EditableRectangle) shape).getTopMagnet() != null) {
                    ((EditableRectangle) shape).getTopMagnet().placeMagnetPoints(layer, Magnet.MAGNET_TOP);
                    ((EditableRectangle) shape).getLeftMagnet().placeMagnetPoints(layer, Magnet.MAGNET_LEFT);
                    ((EditableRectangle) shape).getRightMagnet().placeMagnetPoints(layer, Magnet.MAGNET_RIGHT);
                    ((EditableRectangle) shape).getBottomMagnet().placeMagnetPoints(layer, Magnet.MAGNET_BOTTOM);
                }
                layer.draw();

            }

        });

        addNodeDragEndHandler(new NodeDragEndHandler() {
            public void onNodeDragEnd(NodeDragEndEvent nodeDragEndEvent) {
                ((EditableRectangle) shape).setBeingResized(false);
            }

        });

        layer.add(this);
    }

    public void moveControlPoint(Layer layer) {
        switch (controlType) {
            case CONTROL_TOP_LEFT:
                setX(shape.getX() - 5);
                setY(shape.getY() - 5);
                break;
            case CONTROL_BOTTOM_LEFT:
                setX(shape.getX() - 5);
                setY(shape.getY() + shape.getHeight() - 5);
                break;
            case CONTROL_TOP_RIGHT:
                setX(shape.getX() + shape.getWidth() - 5);
                setY(shape.getY() - 5);
                break;
            case CONTROL_BOTTOM_RIGHT:
                setX(shape.getX() + shape.getWidth() - 5);
                setY(shape.getY() + shape.getHeight() - 5);
                break;
        }
    }

    public void recordStartData(Rectangle destination, NodeDragStartEvent nodeDragStartEvent) {
        dragEventStartX = nodeDragStartEvent.getX();
        dragEventStartY = nodeDragStartEvent.getY();
        ((EditableRectangle) destination).setStartX(destination.getX());
        ((EditableRectangle) destination).setStartY(destination.getY());
        ((EditableRectangle) destination).setStartHeight(destination.getHeight());
        ((EditableRectangle) destination).setStartWidth(destination.getWidth());

    }

    public void nodeDragMove(EditableRectangle rect, double deltaX, double deltaY, Layer layer) {
        switch (controlType) {
            case CONTROL_TOP_LEFT:
                rect.setX(rect.getStartX() + deltaX);
                rect.setY(rect.getStartY() + deltaY);

                rect.setWidth(rect.getStartWidth() - deltaX);
                rect.setHeight(rect.getStartHeight() - deltaY);

                rect.setCurrentDragX(rect.getStartX() + deltaX);
                rect.setCurrentDragY(rect.getStartY() + deltaY);

                break;
            case CONTROL_BOTTOM_LEFT:
                rect.setX(rect.getStartX() + deltaX);

                rect.setWidth(rect.getStartWidth() - deltaX);
                rect.setHeight(rect.getStartHeight() + deltaY);

                rect.setCurrentDragX(rect.getStartX() + deltaX);

                break;
            case CONTROL_TOP_RIGHT:
                rect.setY(rect.getStartY() + deltaY);

                rect.setWidth(rect.getStartWidth() + deltaX);
                rect.setHeight(rect.getStartHeight() - deltaY);

                rect.setCurrentDragY(rect.getStartY() + deltaY);
                break;
            case CONTROL_BOTTOM_RIGHT:
                rect.setWidth(rect.getStartWidth() + deltaX);
                rect.setHeight(rect.getStartHeight() + deltaY);

                break;
        }

        switch (controlType) {
            case CONTROL_TOP_LEFT:
                rect.getBottomLeftControlPoint().setControlPointX(rect.getX() - 5);
                rect.getTopRightControlPoint().setControlPointY(rect.getY() - 5);
                break;
            case CONTROL_BOTTOM_LEFT:
                rect.getTopLeftControlPoint().setControlPointX(rect.getX() - 5);
                rect.getBottomRightControlPoint().setControlPointY(rect.getY() + rect.getHeight() - 5);
                break;
            case CONTROL_TOP_RIGHT:
                rect.getTopLeftControlPoint().setControlPointY(rect.getY() - 5);
                rect.getBottomRightControlPoint().setControlPointX(rect.getX() + rect.getWidth() - 5);
                break;
            case CONTROL_BOTTOM_RIGHT:
                rect.getBottomLeftControlPoint().setControlPointY(rect.getY() + rect.getHeight() - 5);
                rect.getTopRightControlPoint().setControlPointX(rect.getX() + rect.getWidth() - 5);
                break;
        }
    }

    public void nodeDragMove(EditableRectangle rect, NodeDragMoveEvent nodeDragMoveEvent, Layer layer) {

        double deltaX = nodeDragMoveEvent.getX() - getDragEventStartX();
        double deltaY = nodeDragMoveEvent.getY() - getDragEventStartY();
        nodeDragMove(rect, deltaX, deltaY, layer);

    }

    public double getDragEventStartX() {
        return dragEventStartX;
    }

    public double getDragEventStartY() {
        return dragEventStartY;
    }

    public void setControlPointX(double x) {
        setX(x);
    }

    public void setControlPointY(double y) {
        setY(y);
    }

    @Override
    public String toString() {
        return "RectangleControlPointImpl{" + "id=" + id + '}';
    }

    public void setControlPointVisible(boolean visible) {
        setVisible(visible);
    }

    public int getControlType() {
        return controlType;
    }

    public void udpateShape(Layer layer, double x, double y) {
        nodeDragMove((EditableRectangle) shape, x, y, layer);
        if (((EditableRectangle) shape).getTopMagnet() != null) {
            ((EditableRectangle) shape).getTopMagnet().placeMagnetPoints(layer, Magnet.MAGNET_TOP);
            ((EditableRectangle) shape).getLeftMagnet().placeMagnetPoints(layer, Magnet.MAGNET_LEFT);
            ((EditableRectangle) shape).getRightMagnet().placeMagnetPoints(layer, Magnet.MAGNET_RIGHT);
            ((EditableRectangle) shape).getBottomMagnet().placeMagnetPoints(layer, Magnet.MAGNET_BOTTOM);
        }
        layer.draw();
    }

}
