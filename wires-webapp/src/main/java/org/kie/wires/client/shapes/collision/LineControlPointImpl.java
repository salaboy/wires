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
import static org.kie.wires.client.factoryShapes.ShapeFactoryUtil.CP_RGB_FILL_COLOR;
import org.kie.wires.client.shapes.EditableLine;
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
        setFillColor(CP_RGB_FILL_COLOR);

    }

    public LineControlPointImpl(double width, double height) {
        super(width, height);
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

    public void placeControlPoint(final Layer layer) {

        moveControlPoint();

        switch (controlType) {
            case ControlPoint.CONTROL_START:

                addNodeDragStartHandler(new NodeDragStartHandler() {
                    public void onNodeDragStart(NodeDragStartEvent nodeDragStartEvent) {
                        dragEventStartX = nodeDragStartEvent.getX();
                        dragEventStartY = nodeDragStartEvent.getY();
                        initialStartPointX = shape.getPoints().getPoint(0).getX();
                        initialStartPointY = shape.getPoints().getPoint(0).getY();
                        ((StickableShape) shape).hideMagnetPoints();
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

                
                break;
            case ControlPoint.CONTROL_END:

                addNodeDragStartHandler(new NodeDragStartHandler() {
                    public void onNodeDragStart(NodeDragStartEvent nodeDragStartEvent) {
                        ((StickableShape) shape).hideMagnetPoints();
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
                
                break;
        }

    }

    public void udpateShape(Layer layer, double x, double y) {
        Point2DArray array = shape.getPoints();

        switch (controlType) {
            case ControlPoint.CONTROL_START:
                array.getPoint(0).setX(  x - shape.getX() );
                array.getPoint(0).setY( y - shape.getY() );

                break;
            case ControlPoint.CONTROL_END:
                array.getPoint(1).setX( x - (shape.getX() ) );
                array.getPoint(1).setY( y - (shape.getY() ) );

                break;
        }
        layer.draw();

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

    public void moveControlPoint() {
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

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 89 * hash + (int) (Double.doubleToLongBits(this.initialStartPointX) ^ (Double.doubleToLongBits(this.initialStartPointX) >>> 32));
        hash = 89 * hash + (int) (Double.doubleToLongBits(this.initialStartPointY) ^ (Double.doubleToLongBits(this.initialStartPointY) >>> 32));
        hash = 89 * hash + (int) (Double.doubleToLongBits(this.initialEndPointX) ^ (Double.doubleToLongBits(this.initialEndPointX) >>> 32));
        hash = 89 * hash + (int) (Double.doubleToLongBits(this.initialEndPointY) ^ (Double.doubleToLongBits(this.initialEndPointY) >>> 32));
        hash = 89 * hash + (this.shape != null ? this.shape.hashCode() : 0);
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
        final LineControlPointImpl other = (LineControlPointImpl) obj;
        if ((this.id == null) ? (other.id != null) : !this.id.equals(other.id)) {
            return false;
        }
        if (Double.doubleToLongBits(this.initialStartPointX) != Double.doubleToLongBits(other.initialStartPointX)) {
            return false;
        }
        if (Double.doubleToLongBits(this.initialStartPointY) != Double.doubleToLongBits(other.initialStartPointY)) {
            return false;
        }
        if (Double.doubleToLongBits(this.initialEndPointX) != Double.doubleToLongBits(other.initialEndPointX)) {
            return false;
        }
        if (Double.doubleToLongBits(this.initialEndPointY) != Double.doubleToLongBits(other.initialEndPointY)) {
            return false;
        }
        if (this.shape != other.shape && (this.shape == null || !this.shape.equals(other.shape))) {
            return false;
        }
        return true;
    }

}
