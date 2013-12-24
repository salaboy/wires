package org.kie.wires.client.shapes;

import com.emitrom.lienzo.client.core.event.NodeDragEndEvent;
import com.emitrom.lienzo.client.core.event.NodeDragEndHandler;
import com.emitrom.lienzo.client.core.event.NodeDragMoveEvent;
import com.emitrom.lienzo.client.core.event.NodeDragMoveHandler;
import com.emitrom.lienzo.client.core.event.NodeDragStartEvent;
import com.emitrom.lienzo.client.core.event.NodeDragStartHandler;
import com.emitrom.lienzo.client.core.event.NodeMouseClickEvent;
import com.emitrom.lienzo.client.core.event.NodeMouseClickHandler;
import com.emitrom.lienzo.client.core.event.NodeMouseEnterEvent;
import com.emitrom.lienzo.client.core.event.NodeMouseEnterHandler;
import com.emitrom.lienzo.client.core.event.NodeMouseExitEvent;
import com.emitrom.lienzo.client.core.event.NodeMouseExitHandler;
import com.emitrom.lienzo.client.core.event.NodeMouseOverEvent;
import com.emitrom.lienzo.client.core.event.NodeMouseOverHandler;
import com.emitrom.lienzo.client.core.shape.Layer;
import com.emitrom.lienzo.client.core.shape.Rectangle;
import com.google.gwt.core.client.GWT;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.kie.wires.client.shapes.collision.CollidableShape;
import org.kie.wires.client.shapes.collision.ControlPoint;
import org.kie.wires.client.shapes.collision.Magnet;
import org.kie.wires.client.shapes.collision.RectangleControlPointImpl;
import org.kie.wires.client.shapes.collision.RectangleMagnetImpl;
import org.kie.wires.client.shapes.collision.StickableShape;
import org.kie.wires.client.util.UUID;
import org.kie.wires.client.util.collision.Projection;
import org.kie.wires.client.util.collision.Vector;

public class EditableRectangle extends Rectangle implements EditableShape, CollidableShape, StickableShape {

    private final String id;

    private ControlPoint topLeftControlPoint;
    private ControlPoint topRightControlPoint;
    private ControlPoint bottomLeftControlPoint;
    private ControlPoint bottomRightControlPoint;

    private Magnet topMagnet;
    private Magnet rightMagnet;
    private Magnet bottomMagnet;
    private Magnet leftMagnet;

    private boolean beingResized = false;

    private double currentDragX;
    private double currentDragY;

    private double startX;
    private double startY;
    private double startWidth;
    private double startHeight;

    private boolean beingDragged = false;

    public EditableRectangle(double width, double height) {
        super(width, height);
        setDraggable(true);
        this.id = UUID.uuid();
    }

    public EditableRectangle(double width, double height, double cornerRadius) {
        super(width, height, cornerRadius);
        setDraggable(true);
        this.id = UUID.uuid();
    }

    public String getId() {
        return id;
    }

    public void init(double x, double y) {
        setX(x);
        setY(y);
        currentDragX = x;
        currentDragY = y;

        addNodeMouseClickHandler(new NodeMouseClickHandler() {
            public void onNodeMouseClick(NodeMouseClickEvent nodeMouseClickEvent) {
                ShapesUtils.nodeMouseClickHandler(EditableRectangle.this);
                ShapesUtils.deselectAllOtherShapes(getLayer());
            }
        });

        addNodeMouseEnterHandler(new NodeMouseEnterHandler() {
            public void onNodeMouseEnter(NodeMouseEnterEvent nodeMouseEnterEvent) {
                ShapesUtils.nodeMouseEnterHandler(EditableRectangle.this);

            }
        });

        addNodeMouseOverHandler(new NodeMouseOverHandler() {
            public void onNodeMouseOver(NodeMouseOverEvent nodeMouseOverEvent) {
                ShapesUtils.nodeMouseOverHandler(EditableRectangle.this);

            }
        });

        addNodeMouseExitHandler(new NodeMouseExitHandler() {
            public void onNodeMouseExit(NodeMouseExitEvent nodeMouseExitEvent) {
                ShapesUtils.nodeMouseExitHandler(EditableRectangle.this);

            }
        });

        addNodeDragStartHandler(new NodeDragStartHandler() {
            public void onNodeDragStart(NodeDragStartEvent nodeDragStartEvent) {

                if (topLeftControlPoint != null) {
                    //hideControlPoints();
                }
                if (topMagnet != null) {
                    //hideMagnetPoints();
                }
            }
        });

        addNodeDragMoveHandler(new NodeDragMoveHandler() {
            public void onNodeDragMove(NodeDragMoveEvent nodeDragMoveEvent) {
                beingDragged = true;
                currentDragX = nodeDragMoveEvent.getDragContext().getNode().getX() + nodeDragMoveEvent.getDragContext().getLocalAdjusted().getX();
                currentDragY = nodeDragMoveEvent.getDragContext().getNode().getY() + nodeDragMoveEvent.getDragContext().getLocalAdjusted().getY();
//                Layer layer = getLayer();
//
//                for (Object cp : topMagnet.getAttachedControlPoints()) {
//                    ((ControlPoint) cp).setControlPointVisible(true);
//                    ((ControlPoint) cp).setControlPointX(currentDragX + (EditableRectangle.this.getWidth() / 2));
//                    ((ControlPoint) cp).setControlPointY(currentDragY);
//                    ((ControlPoint) cp).udpateShape(layer, currentDragX , currentDragY);
//                }
//                for (Object cp : leftMagnet.getAttachedControlPoints()) {
//                    ((ControlPoint) cp).setControlPointVisible(true);
//                    ((ControlPoint) cp).setControlPointX(currentDragX);
//                    ((ControlPoint) cp).setControlPointY(currentDragY + (EditableRectangle.this.getHeight() / 2));
//                    ((ControlPoint) cp).udpateShape(layer, currentDragX , currentDragY);
//                }
//                for (Object cp : rightMagnet.getAttachedControlPoints()) {
//                    ((ControlPoint) cp).setControlPointVisible(true);
//                    ((ControlPoint) cp).setControlPointX(currentDragX + EditableRectangle.this.getWidth());
//                    ((ControlPoint) cp).setControlPointY(currentDragY + (EditableRectangle.this.getHeight() / 2));
//                    ((ControlPoint) cp).udpateShape(layer, currentDragX , currentDragY);
//                }
//                for (Object cp : bottomMagnet.getAttachedControlPoints()) {
//                    ((ControlPoint) cp).setControlPointVisible(true);
//                    ((ControlPoint) cp).setControlPointX(currentDragX + (EditableRectangle.this.getWidth() / 2));
//                    ((ControlPoint) cp).setControlPointY(currentDragY + EditableRectangle.this.getHeight());
//                    
//                    ((ControlPoint) cp).udpateShape(layer, nodeDragMoveEvent.getDragContext().getLocalAdjusted().getX() , nodeDragMoveEvent.getDragContext().getLocalAdjusted().getY());
//                }
//                layer.draw();
            }
        });

        addNodeDragEndHandler(new NodeDragEndHandler() {
            public void onNodeDragEnd(NodeDragEndEvent event) {
                beingDragged = false;

            }
        });

    }

    public void showControlPoints() {
        final Layer layer = getLayer();
        if (topLeftControlPoint == null) {
            // Can be null, if we enter, after an exit but the timer has not removed the points yet

            topLeftControlPoint = new RectangleControlPointImpl(this, ControlPoint.CONTROL_TOP_LEFT);

            topLeftControlPoint.initControlPoint(layer);

            bottomLeftControlPoint = new RectangleControlPointImpl(this, ControlPoint.CONTROL_BOTTOM_LEFT);

            bottomLeftControlPoint.initControlPoint(layer);

            topRightControlPoint = new RectangleControlPointImpl(this, ControlPoint.CONTROL_TOP_RIGHT);

            topRightControlPoint.initControlPoint(layer);

            bottomRightControlPoint = new RectangleControlPointImpl(this, ControlPoint.CONTROL_BOTTOM_RIGHT);

            bottomRightControlPoint.initControlPoint(layer);

            layer.draw();
        } else {

            topLeftControlPoint.setControlPointVisible(true);
            topLeftControlPoint.moveControlPoint(layer);
            bottomLeftControlPoint.setControlPointVisible(true);
            bottomLeftControlPoint.moveControlPoint(layer);
            topRightControlPoint.setControlPointVisible(true);
            topRightControlPoint.moveControlPoint(layer);
            bottomRightControlPoint.setControlPointVisible(true);
            bottomRightControlPoint.moveControlPoint(layer);
            layer.draw();
        }
    }

    public void hideControlPoints() {

        if (topLeftControlPoint != null) {
            // can be null, afer the main Shape is dragged, and control points are forcibly removed
            Layer layer = getLayer();

            topLeftControlPoint.setControlPointVisible(false);
            topLeftControlPoint.moveControlPoint(layer);
            bottomLeftControlPoint.setControlPointVisible(false);
            bottomLeftControlPoint.moveControlPoint(layer);
            topRightControlPoint.setControlPointVisible(false);
            topRightControlPoint.moveControlPoint(layer);
            bottomRightControlPoint.setControlPointVisible(false);
            bottomRightControlPoint.moveControlPoint(layer);
            layer.draw();
        }

    }

    @Override
    public void hideMagnetPoints() {

        if (topMagnet != null) {

            Layer layer = getLayer();

            topMagnet.setMagnetVisible(false);

            leftMagnet.setMagnetVisible(false);

            rightMagnet.setMagnetVisible(false);

            bottomMagnet.setMagnetVisible(false);

            layer.draw();
        }

    }

    public void showMagnetsPoints() {
        final Layer layer = getLayer();
        if (topMagnet == null) {

            topMagnet = new RectangleMagnetImpl(this);
            topMagnet.placeMagnetPoints(layer, Magnet.MAGNET_TOP);
            bottomMagnet = new RectangleMagnetImpl(this);
            bottomMagnet.placeMagnetPoints(layer, Magnet.MAGNET_BOTTOM);
            leftMagnet = new RectangleMagnetImpl(this);
            leftMagnet.placeMagnetPoints(layer, Magnet.MAGNET_LEFT);
            rightMagnet = new RectangleMagnetImpl(this);
            rightMagnet.placeMagnetPoints(layer, Magnet.MAGNET_RIGHT);

            layer.draw();
        } else {
            topMagnet.setMagnetVisible(true);
            topMagnet.placeMagnetPoints(layer, Magnet.MAGNET_TOP);
            bottomMagnet.setMagnetVisible(true);
            bottomMagnet.placeMagnetPoints(layer, Magnet.MAGNET_BOTTOM);
            leftMagnet.setMagnetVisible(true);
            leftMagnet.placeMagnetPoints(layer, Magnet.MAGNET_LEFT);
            rightMagnet.setMagnetVisible(true);
            rightMagnet.placeMagnetPoints(layer, Magnet.MAGNET_RIGHT);
        }
    }

    public Magnet getTopMagnet() {
        return topMagnet;
    }

    public Magnet getRightMagnet() {
        return rightMagnet;
    }

    public Magnet getBottomMagnet() {
        return bottomMagnet;
    }

    public Magnet getLeftMagnet() {
        return leftMagnet;
    }

    public double getStartX() {
        return startX;
    }

    public void setStartX(double startX) {
        this.startX = startX;
    }

    public double getStartY() {
        return startY;
    }

    public void setStartY(double startY) {
        this.startY = startY;
    }

    public double getStartWidth() {
        return startWidth;
    }

    public void setStartWidth(double startWidth) {
        this.startWidth = startWidth;
    }

    public double getStartHeight() {
        return startHeight;
    }

    public void setStartHeight(double startHeight) {
        this.startHeight = startHeight;
    }

    public List<Vector> getAxes() {
        Vector v1 = new Vector();
        Vector v2 = new Vector();
        List<Vector> axes = new ArrayList<Vector>();

        // THIS IS HARDCODED HERE BUT IT CAN BE A LOOP FOR POLYGONS
        // top - left
        v1.setX(getCurrentDragX());
        v1.setY(getCurrentDragY());

        // top - right
        v2.setX(getCurrentDragX() + getWidth());
        v2.setY(getCurrentDragY());

        axes.add(v1.edge(v2).normal());

        v1 = new Vector();
        v2 = new Vector();
        // top - right 
        v1.setX(getCurrentDragX() + getWidth());
        v1.setY(getCurrentDragY());

        // bottom - right
        v2.setX(getCurrentDragX() + getWidth());
        v2.setY(getCurrentDragY() + getHeight());

        axes.add(v1.edge(v2).normal());

        v1 = new Vector();
        v2 = new Vector();
        // bottom - right
        v1.setX(getCurrentDragX() + getWidth());
        v1.setY(getCurrentDragY() + getHeight());

        // bottom - left
        v2.setX(getCurrentDragX());
        v2.setY(getCurrentDragY() + getHeight());

        axes.add(v1.edge(v2).normal());

        v1 = new Vector();
        v2 = new Vector();
        // bottom - left
        v1.setX(getCurrentDragX());
        v1.setY(getCurrentDragY() + getHeight());

        // top - left
        v2.setX(getCurrentDragX());
        v2.setY(getCurrentDragY());

        axes.add(v1.edge(v2).normal());

        return axes;
    }

    public Projection project(Vector axis) {
        List<Double> scalars = new ArrayList<Double>();
        Vector v1 = new Vector();

        // top - left
        v1.setX(getCurrentDragX());
        v1.setY(getCurrentDragY());

        scalars.add(v1.dotProduct(axis));

        v1 = new Vector();
        // top - right
        v1.setX(getCurrentDragX() + getWidth());
        v1.setY(getCurrentDragY());

        scalars.add(v1.dotProduct(axis));

        v1 = new Vector();
        // bottom - right
        v1.setX(getCurrentDragX() + getWidth());
        v1.setY(getCurrentDragY() + getHeight());

        scalars.add(v1.dotProduct(axis));

        v1 = new Vector();
        // bottom - left
        v1.setX(getCurrentDragX());
        v1.setY(getCurrentDragY() + getHeight());

        scalars.add(v1.dotProduct(axis));

        Double min = Collections.min(scalars);
        Double max = Collections.max(scalars);

        return new Projection(min, max);

    }

    public boolean collidesWith(CollidableShape shape) {
        List<Vector> axes = getAxes();
        axes.addAll(shape.getAxes());
        return !separationOnAxes(axes, shape);
    }

    public boolean separationOnAxes(List<Vector> axes, CollidableShape shape) {
        for (int i = 0; i < axes.size(); ++i) {
            Vector axis = axes.get(i);
            Projection projection1 = shape.project(axis);
            Projection projection2 = this.project(axis);

            if (!projection1.overlaps(projection2)) {
                return true; // there is no need to continue testing
            }
        }
        return false;
    }

    public ControlPoint getTopLeftControlPoint() {
        return topLeftControlPoint;
    }

    public ControlPoint getTopRightControlPoint() {
        return topRightControlPoint;
    }

    public ControlPoint getBottomLeftControlPoint() {
        return bottomLeftControlPoint;
    }

    public ControlPoint getBottomRightControlPoint() {
        return bottomRightControlPoint;
    }

    public boolean isBeingDragged() {
        return beingDragged;
    }

    public double getCurrentDragX() {
        return currentDragX;
    }

    public double getCurrentDragY() {
        return currentDragY;
    }

    public void setCurrentDragX(double currentDragX) {
        this.currentDragX = currentDragX;
    }

    public void setCurrentDragY(double currentDragY) {
        this.currentDragY = currentDragY;
    }

    public List<Magnet> getMagnets() {
        ArrayList<Magnet> magnets = new ArrayList<Magnet>();
        magnets.add(topMagnet);
        magnets.add(rightMagnet);
        magnets.add(leftMagnet);
        magnets.add(bottomMagnet);
        return magnets;
    }

    public boolean isBeingResized() {
        return beingResized;
    }

    public void setBeingResized(boolean beingResized) {
        this.beingResized = beingResized;
    }

    @Override
    public String toString() {
        return "EditableRectangle{" + "id=" + getId() + ",x = " + getX() + ", y = " + getY() + ", beingDragged= " + beingDragged + "}";
    }

}
