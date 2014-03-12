package org.kie.wires.client.shapes;

import com.emitrom.lienzo.client.core.event.NodeDragEndEvent;
import com.emitrom.lienzo.client.core.event.NodeDragEndHandler;
import com.emitrom.lienzo.client.core.event.NodeDragMoveEvent;
import com.emitrom.lienzo.client.core.event.NodeDragMoveHandler;
import com.emitrom.lienzo.client.core.event.NodeDragStartEvent;
import com.emitrom.lienzo.client.core.event.NodeDragStartHandler;
import com.emitrom.lienzo.client.core.event.NodeMouseClickEvent;
import com.emitrom.lienzo.client.core.event.NodeMouseClickHandler;
import com.emitrom.lienzo.client.core.shape.Layer;
import com.emitrom.lienzo.client.core.shape.Rectangle;
import com.google.gwt.core.client.GWT;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.kie.wires.client.events.ShapeSelectedEvent;
import org.kie.wires.client.shapes.api.WiresBaseGroupShape;
import org.kie.wires.client.shapes.collision.RectangleControlPointImpl;
import org.kie.wires.client.shapes.collision.RectangleMagnetImpl;
import org.kie.wires.client.shapes.collision.api.CollidableShape;
import org.kie.wires.client.shapes.collision.api.ControlPoint;
import org.kie.wires.client.shapes.collision.api.Magnet;
import org.kie.wires.client.shapes.collision.api.Projection;
import org.kie.wires.client.shapes.collision.api.Vector;
import org.kie.wires.client.shapes.util.ShapesUtils;
import org.kie.wires.client.util.UUID;

public class WiresRectangle extends WiresBaseGroupShape {

    private final String id;

    private Rectangle rectangle;

    private double currentDragX;
    private double currentDragY;

    private double startX;
    private double startY;
    private double startWidth;
    private double startHeight;

    private boolean beingDragged = false;
    private boolean beingResized = false;

    public WiresRectangle(double width, double height) {
        this(width, height, 3);

        setDraggable(true);
    }

    public WiresRectangle(double width, double height, double cornerRadius) {

        rectangle = new Rectangle(width, height, cornerRadius);
        add(rectangle);

        this.id = UUID.uuid();
        magnets.clear();
        addMagnet(new RectangleMagnetImpl(this, Magnet.MAGNET_TOP));
        addMagnet(new RectangleMagnetImpl(this, Magnet.MAGNET_RIGHT));
        addMagnet(new RectangleMagnetImpl(this, Magnet.MAGNET_BOTTOM));
        addMagnet(new RectangleMagnetImpl(this, Magnet.MAGNET_LEFT));
        controlPoints.clear();
        addControlPoint(new RectangleControlPointImpl(this, ControlPoint.CONTROL_TOP_LEFT));
        addControlPoint(new RectangleControlPointImpl(this, ControlPoint.CONTROL_TOP_RIGHT));
        addControlPoint(new RectangleControlPointImpl(this, ControlPoint.CONTROL_BOTTOM_LEFT));
        addControlPoint(new RectangleControlPointImpl(this, ControlPoint.CONTROL_BOTTOM_RIGHT));

    }

    public String getId() {
        return id;
    }

    public void init(double x, double y) {
        super.init();
        setX(x);
        setY(y);
        currentDragX = x;
        currentDragY = y;

        addNodeMouseClickHandler(new NodeMouseClickHandler() {
            public void onNodeMouseClick(NodeMouseClickEvent nodeMouseClickEvent) {
                Layer layer = getLayer();
                ShapesUtils.nodeMouseClickHandler(WiresRectangle.this);
                ShapesUtils.deselectAllOtherShapes();
                getSelected().fire(new ShapeSelectedEvent(WiresRectangle.this));
                layer.draw();
            }
        });

        addNodeDragStartHandler(new NodeDragStartHandler() {
            public void onNodeDragStart(NodeDragStartEvent nodeDragStartEvent) {

                hideControlPoints();
                hideMagnetPoints();

            }
        });

        addNodeDragMoveHandler(new NodeDragMoveHandler() {
            public void onNodeDragMove(NodeDragMoveEvent nodeDragMoveEvent) {
                beingDragged = true;
                currentDragX = nodeDragMoveEvent.getDragContext().getNode().getX() + nodeDragMoveEvent.getDragContext().getLocalAdjusted().getX();
                currentDragY = nodeDragMoveEvent.getDragContext().getNode().getY() + nodeDragMoveEvent.getDragContext().getLocalAdjusted().getY();
                Layer layer = getLayer();
                for (Magnet m : magnets) {
                    if (!m.getAttachedControlPoints().isEmpty()) {
                        //GWT.log("there are attached control points to topMagnet " + topMagnet.getAttachedControlPoints().size());
                        List<ControlPoint> removeCp = new ArrayList<ControlPoint>();
                        for (ControlPoint cp : m.getAttachedControlPoints()) {
                            GWT.log("Is Attached?? Control Point : "+cp);
                            if (cp.isAttached()) {
                                cp.setControlPointVisible(true);
                                // All the coordinate for the control points should be relative and autocalculated
                                // TODO: refactor this
                                switch (m.getType()) {
                                    case Magnet.MAGNET_TOP:
                                        cp.setControlPointX(currentDragX + (rectangle.getWidth() / 2) - 5);
                                        cp.setControlPointY(currentDragY - 5);
                                        cp.udpateShape(layer, currentDragX + (rectangle.getWidth() / 2), currentDragY);
                                        break;

                                    case Magnet.MAGNET_LEFT:
                                        cp.setControlPointX(currentDragX - 5);
                                        cp.setControlPointY(currentDragY + (rectangle.getHeight() / 2) - 5);
                                        cp.udpateShape(layer, currentDragX, currentDragY + (rectangle.getHeight() / 2));
                                        break;

                                    case Magnet.MAGNET_RIGHT:
                                        cp.setControlPointX(currentDragX + rectangle.getWidth() - 5);
                                        cp.setControlPointY(currentDragY + (rectangle.getHeight() / 2) - 5);
                                        cp.udpateShape(layer, currentDragX + rectangle.getWidth(), currentDragY + (rectangle.getHeight() / 2));

                                    case Magnet.MAGNET_BOTTOM:
                                        cp.setControlPointX(currentDragX + (rectangle.getWidth() / 2) - 5);
                                        cp.setControlPointY(currentDragY + rectangle.getHeight() - 5);
                                        cp.udpateShape(layer, currentDragX + (rectangle.getWidth() / 2), currentDragY + rectangle.getHeight());
                                        break;
                                }

                            } else {
                                GWT.log("Removing non attached control point : " + cp + " from magnet" + m);
                                removeCp.add(cp);

                            }

                        }
                        for (ControlPoint cp : removeCp) {
                            m.getAttachedControlPoints().remove(cp);
                        }
                    }

                }
                layer.draw();
            }
        });

        addNodeDragEndHandler(new NodeDragEndHandler() {
            public void onNodeDragEnd(NodeDragEndEvent event) {
                beingDragged = false;

            }
        });
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
        v2.setX(getCurrentDragX() + rectangle.getWidth());
        v2.setY(getCurrentDragY());

        axes.add(v1.edge(v2).normal());

        v1 = new Vector();
        v2 = new Vector();
        // top - right 
        v1.setX(getCurrentDragX() + rectangle.getWidth());
        v1.setY(getCurrentDragY());

        // bottom - right
        v2.setX(getCurrentDragX() + rectangle.getWidth());
        v2.setY(getCurrentDragY() + rectangle.getHeight());

        axes.add(v1.edge(v2).normal());

        v1 = new Vector();
        v2 = new Vector();
        // bottom - right
        v1.setX(getCurrentDragX() + rectangle.getWidth());
        v1.setY(getCurrentDragY() + rectangle.getHeight());

        // bottom - left
        v2.setX(getCurrentDragX());
        v2.setY(getCurrentDragY() + rectangle.getHeight());

        axes.add(v1.edge(v2).normal());

        v1 = new Vector();
        v2 = new Vector();
        // bottom - left
        v1.setX(getCurrentDragX());
        v1.setY(getCurrentDragY() + rectangle.getHeight());

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
        v1.setX(getCurrentDragX() + rectangle.getWidth());
        v1.setY(getCurrentDragY());

        scalars.add(v1.dotProduct(axis));

        v1 = new Vector();
        // bottom - right
        v1.setX(getCurrentDragX() + rectangle.getWidth());
        v1.setY(getCurrentDragY() + rectangle.getHeight());

        scalars.add(v1.dotProduct(axis));

        v1 = new Vector();
        // bottom - left
        v1.setX(getCurrentDragX());
        v1.setY(getCurrentDragY() + rectangle.getHeight());

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

   

    public boolean isBeingResized() {
        return beingResized;
    }

    public void setBeingResized(boolean beingResized) {
        this.beingResized = beingResized;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    @Override
    public String toString() {
        return "WiresRectangle{" + "id=" + getId() + ",x = " + getX() + ", y = " + getY() + ", beingDragged= " + beingDragged + "}";
    }

}
