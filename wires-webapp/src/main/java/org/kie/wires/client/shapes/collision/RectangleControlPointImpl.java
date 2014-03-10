/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kie.wires.client.shapes.collision;

import org.kie.wires.client.shapes.collision.api.ControlPoint;
import org.kie.wires.client.shapes.collision.api.StickableShape;
import com.emitrom.lienzo.client.core.event.NodeDragEndEvent;
import com.emitrom.lienzo.client.core.event.NodeDragEndHandler;
import com.emitrom.lienzo.client.core.event.NodeDragMoveEvent;
import com.emitrom.lienzo.client.core.event.NodeDragMoveHandler;
import com.emitrom.lienzo.client.core.event.NodeDragStartEvent;
import com.emitrom.lienzo.client.core.event.NodeDragStartHandler;
import com.emitrom.lienzo.client.core.shape.Layer;
import com.emitrom.lienzo.client.core.shape.Rectangle;
import com.emitrom.lienzo.shared.core.types.ColorName;
import static org.kie.wires.client.factoryShapes.ShapeFactoryUtil.CP_RGB_FILL_COLOR;
import static org.kie.wires.client.factoryShapes.ShapeFactoryUtil.CP_RGB_STROKE_WIDTH_SHAPE;
import org.kie.wires.client.shapes.WiresRectangle;
import static org.kie.wires.client.shapes.collision.api.ControlPoint.*;
import org.kie.wires.client.util.UUID;

/**
 *
 * @author salaboy
 */
public class RectangleControlPointImpl extends Rectangle implements ControlPoint {

    private double dragEventStartX;
    private double dragEventStartY;

    private String id;

    private WiresRectangle shape;

    private int controlType;
    
    private boolean attached = false;

    public RectangleControlPointImpl(WiresRectangle shape, int controlType) {
        this(12, 12);
        this.shape = shape;
        this.controlType = controlType;
        
    }

    public RectangleControlPointImpl(double width, double height) {
        super(width, height);
        id = UUID.uuid();
        setFillColor(CP_RGB_FILL_COLOR);
        
    }

    public String getId() {
        return id;
    }

    public WiresRectangle getShape() {
        return shape;
    }

    @Override
    public boolean isAttached() {
        return attached;
    }
    
    public void placeControlPoint(final Layer layer) {

        moveControlPoint();

        setDraggable(true)
                .setStrokeWidth(CP_RGB_STROKE_WIDTH_SHAPE)
                .setStrokeColor(ColorName.BLACK);

        addNodeDragStartHandler(new NodeDragStartHandler() {
            public void onNodeDragStart(NodeDragStartEvent nodeDragStartEvent) {
                ((StickableShape) shape).hideMagnetPoints();
                ((WiresRectangle) shape).setBeingResized(true);
                recordStartData(shape, nodeDragStartEvent);
            }
        });

        addNodeDragMoveHandler(new NodeDragMoveHandler() {
            public void onNodeDragMove(NodeDragMoveEvent nodeDragMoveEvent) {

                
                
                nodeDragMove((WiresRectangle) shape, nodeDragMoveEvent, layer);

                layer.draw();

            }

        });

        addNodeDragEndHandler(new NodeDragEndHandler() {
            public void onNodeDragEnd(NodeDragEndEvent nodeDragEndEvent) {
                ((WiresRectangle) shape).setBeingResized(false);
            }

        });


    }

    public void moveControlPoint() {
        switch (controlType) {
            case CONTROL_TOP_LEFT:
                setX(shape.getX() - 5);
                setY(shape.getY() - 5);
                break;
            case CONTROL_BOTTOM_LEFT:
                setX(shape.getX() - 5);
                setY(shape.getY() + shape.getRectangle().getHeight() - 5);
                break;
            case CONTROL_TOP_RIGHT:
                setX(shape.getX() + shape.getRectangle().getWidth() - 5);
                setY(shape.getY() - 5);
                break;
            case CONTROL_BOTTOM_RIGHT:
                setX(shape.getX() + shape.getRectangle().getWidth() - 5);
                setY(shape.getY() + shape.getRectangle().getHeight() - 5);
                break;
        }
    }

    public void recordStartData(WiresRectangle destination, NodeDragStartEvent nodeDragStartEvent) {
        dragEventStartX = nodeDragStartEvent.getX();
        dragEventStartY = nodeDragStartEvent.getY();
        ((WiresRectangle) destination).setStartX(destination.getX());
        ((WiresRectangle) destination).setStartY(destination.getY());
        ((WiresRectangle) destination).setStartHeight(destination.getRectangle().getHeight());
        ((WiresRectangle) destination).setStartWidth(destination.getRectangle().getWidth());

    }

    public void nodeDragMove(WiresRectangle rect, double deltaX, double deltaY, Layer layer) {
        switch (controlType) {
            case CONTROL_TOP_LEFT:
                rect.setX(rect.getStartX() + deltaX);
                rect.setY(rect.getStartY() + deltaY);

                rect.getRectangle().setWidth(rect.getStartWidth() - deltaX);
                rect.getRectangle().setHeight(rect.getStartHeight() - deltaY);

                rect.setCurrentDragX(rect.getStartX() + deltaX);
                rect.setCurrentDragY(rect.getStartY() + deltaY);

                break;
            case CONTROL_BOTTOM_LEFT:
                rect.setX(rect.getStartX() + deltaX);

                rect.getRectangle().setWidth(rect.getStartWidth() - deltaX);
                rect.getRectangle().setHeight(rect.getStartHeight() + deltaY);

                rect.setCurrentDragX(rect.getStartX() + deltaX);

                break;
            case CONTROL_TOP_RIGHT:
                rect.setY(rect.getStartY() + deltaY);

                rect.getRectangle().setWidth(rect.getStartWidth() + deltaX);
                rect.getRectangle().setHeight(rect.getStartHeight() - deltaY);

                rect.setCurrentDragY(rect.getStartY() + deltaY);
                break;
            case CONTROL_BOTTOM_RIGHT:
                rect.getRectangle().setWidth(rect.getStartWidth() + deltaX);
                rect.getRectangle().setHeight(rect.getStartHeight() + deltaY);

                break;
        }

        switch (controlType) {
            case CONTROL_TOP_LEFT:
                rect.getBottomLeftControlPoint().setControlPointX(rect.getX() - 5);
                rect.getTopRightControlPoint().setControlPointY(rect.getY() - 5);
                break;
            case CONTROL_BOTTOM_LEFT:
                rect.getTopLeftControlPoint().setControlPointX(rect.getX() - 5);
                rect.getBottomRightControlPoint().setControlPointY(rect.getY() + rect.getRectangle().getHeight() - 5);
                break;
            case CONTROL_TOP_RIGHT:
                rect.getTopLeftControlPoint().setControlPointY(rect.getY() - 5);
                rect.getBottomRightControlPoint().setControlPointX(rect.getX() + rect.getRectangle().getWidth() - 5);
                break;
            case CONTROL_BOTTOM_RIGHT:
                rect.getBottomLeftControlPoint().setControlPointY(rect.getY() + rect.getRectangle().getHeight() - 5);
                rect.getTopRightControlPoint().setControlPointX(rect.getX() + rect.getRectangle().getWidth() - 5);
                break;
        }
    }

    public void nodeDragMove(WiresRectangle rect, NodeDragMoveEvent nodeDragMoveEvent, Layer layer) {

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
        return "RectangleControlPointImpl{" + "id=" + id + ", controlType=" + controlType + '}';
    }

    
    

    public void setControlPointVisible(boolean visible) {
        setVisible(visible);
    }

    public int getControlType() {
        return controlType;
    }

    public void udpateShape(Layer layer, double x, double y) {
        nodeDragMove((WiresRectangle) shape, x, y, layer);

        ((WiresRectangle) shape).getTopMagnet().placeMagnetPoints();
        ((WiresRectangle) shape).getLeftMagnet().placeMagnetPoints();
        ((WiresRectangle) shape).getRightMagnet().placeMagnetPoints();
        ((WiresRectangle) shape).getBottomMagnet().placeMagnetPoints();

        layer.draw();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 29 * hash + (this.shape != null ? this.shape.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RectangleControlPointImpl other = (RectangleControlPointImpl) obj;
        if ((this.id == null) ? (other.id != null) : !this.id.equals(other.id)) {
            return false;
        }
        if (this.shape != other.shape && (this.shape == null || !this.shape.equals(other.shape))) {
            return false;
        }
        return true;
    }

}
