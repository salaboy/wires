/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kie.wires.core.client.shapes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.kie.wires.core.api.collision.CollidableShape;
import org.kie.wires.core.api.collision.ControlPoint;
import org.kie.wires.core.api.collision.Magnet;
import org.kie.wires.core.api.collision.Projection;
import org.kie.wires.core.api.collision.Vector;
import org.kie.wires.core.api.events.ShapeSelectedEvent;
import org.kie.wires.core.api.shapes.WiresBaseGroupShape;
import org.kie.wires.core.client.collision.LineControlPointImpl;
import org.kie.wires.core.client.collision.LineMagnetImpl;
import org.kie.wires.core.client.util.ShapesUtils;
import org.kie.wires.core.client.util.UUID;

import com.emitrom.lienzo.client.core.event.NodeDragEndEvent;
import com.emitrom.lienzo.client.core.event.NodeDragEndHandler;
import com.emitrom.lienzo.client.core.event.NodeDragMoveEvent;
import com.emitrom.lienzo.client.core.event.NodeDragMoveHandler;
import com.emitrom.lienzo.client.core.event.NodeDragStartEvent;
import com.emitrom.lienzo.client.core.event.NodeDragStartHandler;
import com.emitrom.lienzo.client.core.event.NodeMouseClickEvent;
import com.emitrom.lienzo.client.core.event.NodeMouseClickHandler;
import com.emitrom.lienzo.client.core.shape.Layer;
import com.emitrom.lienzo.client.core.shape.Line;
import com.emitrom.lienzo.client.core.shape.Shape;
import com.emitrom.lienzo.client.core.types.Point2D;
import com.emitrom.lienzo.client.core.types.Point2DArray;

/**
 *
 * @author salaboy
 */
public class WiresLine extends WiresBaseGroupShape {

    private String id;
    private ControlPoint startControlPoint;
    private ControlPoint endControlPoint;

    private double currentDragX;
    private double currentDragY;

    private boolean beingDragged;

    private boolean beingResized;

    private Line line;

    private Magnet startMagnet;
    private Magnet endMagnet;

    private boolean showingMagnets = false;
    private boolean showingControlPoints = false;

    public WiresLine(double x1, double y1, double x2, double y2) {
        line = new Line(x1, y1, x2, y2);

        this.id = UUID.uuid();
        line.setStrokeWidth(ShapesUtils.RGB_STROKE_WIDTH_LINE);

        startMagnet = new LineMagnetImpl(this, Magnet.MAGNET_START);
        endMagnet = new LineMagnetImpl(this, Magnet.MAGNET_END);

        startControlPoint = new LineControlPointImpl(this, ControlPoint.CONTROL_START);
        endControlPoint = new LineControlPointImpl(this, ControlPoint.CONTROL_END);

    }

    public Line getLine() {
        return line;
    }

    public String getId() {
        return id;
    }

    @Override
    public void init(double x, double y) {
        super.init();
        setX(x);
        setY(y);
        currentDragX = x;
        currentDragY = y;

        addNodeMouseClickHandler(new NodeMouseClickHandler() {
            public void onNodeMouseClick(NodeMouseClickEvent nodeMouseClickEvent) {
                Layer layer = getLayer();
                ShapesUtils.nodeMouseClickHandler(WiresLine.this);
                ShapesUtils.deselectAllOtherShapes(ShapesUtils.shapesInCanvas);
                getSelected().fire(new ShapeSelectedEvent(WiresLine.this));
                layer.draw();
            }
        });

        addNodeDragStartHandler(new NodeDragStartHandler() {
            public void onNodeDragStart(NodeDragStartEvent nodeDragStartEvent) {
                // recordStartData(nodeDragStartEvent);

                hideControlPoints();
                hideMagnetPoints();

            }
        });

        addNodeDragMoveHandler(new NodeDragMoveHandler() {
            public void onNodeDragMove(NodeDragMoveEvent nodeDragMoveEvent) {
                beingDragged = true;

                currentDragX = nodeDragMoveEvent.getDragContext().getNode().getX() + nodeDragMoveEvent.getDragContext().getLocalAdjusted().getX();
                currentDragY = nodeDragMoveEvent.getDragContext().getNode().getY() + nodeDragMoveEvent.getDragContext().getLocalAdjusted().getY();

            }
        });

        addNodeDragEndHandler(new NodeDragEndHandler() {
            public void onNodeDragEnd(NodeDragEndEvent event) {
                beingDragged = false;

            }
        });
        add(line);
    }

    public void recordStartData(NodeDragStartEvent nodeDragStartEvent) {

        Point2DArray points = line.getPoints();
        Point2D startPoint = points.getPoint(0);
        Point2D endPoint = points.getPoint(1);

    }

    public ControlPoint getStartControlPoint() {
        return startControlPoint;
    }

    public ControlPoint getEndControlPoint() {
        return endControlPoint;
    }

    @Override
    public void showControlPoints() {
        final Layer layer = getLayer();

        if (startControlPoint != null && !showingControlPoints) {
            startControlPoint.placeControlPoint(getLayer());
            endControlPoint.placeControlPoint(getLayer());

            layer.add((Shape) startControlPoint);
            layer.add((Shape) endControlPoint);
            showingControlPoints = true;
        }

    }

    @Override
    public void hideControlPoints() {
        Layer layer = getLayer();

        // can be null, afer the main Shape is dragged, and control points are forcibly removed
        if (startControlPoint != null && showingControlPoints) {
            layer.remove((Shape) startControlPoint);
            layer.remove((Shape) endControlPoint);
            showingControlPoints = false;

        }

    }

    public void showMagnetsPoints() {
        final Layer layer = getLayer();

        if (startMagnet != null && !showingMagnets) {
            layer.add((Shape) startMagnet);
            layer.add((Shape) endMagnet);
            startMagnet.placeMagnetPoints();
            endMagnet.placeMagnetPoints();
            showingMagnets = true;
        }

    }

    public void hideMagnetPoints() {
        Layer layer = getLayer();
        if (startMagnet != null && showingMagnets) {
            layer.remove((Shape) startMagnet);
            layer.remove((Shape) endMagnet);
            showingMagnets = false;
        }

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

    public List<Vector> getAxes() {
        Vector v1 = new Vector();
        Vector v2 = new Vector();
        List<Vector> axes = new ArrayList<Vector>();

        // THIS IS HARDCODED HERE BUT IT CAN BE A LOOP FOR POLYGONS
        // start
        Point2DArray points = line.getPoints();
        Point2D startPoint = points.getPoint(0);
        v1.setX(getCurrentDragX() + startPoint.getX());
        v1.setY(getCurrentDragY() + startPoint.getY());

        Point2D endPoint = points.getPoint(1);

        // end
        v2.setX(getCurrentDragX() + endPoint.getX());
        v2.setY(getCurrentDragY() + endPoint.getY());

        axes.add(v1.edge(v2).normal());

        return axes;
    }

    public Projection project(Vector axis) {
        List<Double> scalars = new ArrayList<Double>();
        Vector v1 = new Vector();

        Point2DArray points = line.getPoints();
        Point2D startPoint = points.getPoint(0);
        // start
        v1.setX(getCurrentDragX() + startPoint.getX());
        v1.setY(getCurrentDragY() + startPoint.getY());

        scalars.add(v1.dotProduct(axis));

        Point2D endPoint = points.getPoint(1);
        v1 = new Vector();
        // end
        v1.setX(getCurrentDragX() + +endPoint.getX());
        v1.setY(getCurrentDragY() + endPoint.getY());

        scalars.add(v1.dotProduct(axis));

        Double min = Collections.min(scalars);
        Double max = Collections.max(scalars);

        return new Projection(min, max);

    }

    public boolean isBeingDragged() {
        return beingDragged;
    }

    public void setBeingResized(boolean beingResized) {
        this.beingResized = beingResized;
    }

    public boolean isBeingResized() {
        return beingResized;
    }

    public double getCurrentDragX() {
        return currentDragX;
    }

    public double getCurrentDragY() {
        return currentDragY;
    }

    public List<Magnet> getMagnets() {
        ArrayList<Magnet> magnets = new ArrayList<Magnet>(2);
        magnets.add(startMagnet);
        magnets.add(endMagnet);
        return magnets;
    }

    @Override
    public String toString() {
        return "EditableLine{" + "id=" + getId() + ",x = " + getX() + ", y = " + getY() + ", beingDragged= " + beingDragged + "}";
    }

    public void attachControlPointToMagent(Magnet selectedMagnet) {
        double startX = ((Shape) getStartControlPoint()).getX();
        double startY = ((Shape) getStartControlPoint()).getY();
        double endX = ((Shape) getEndControlPoint()).getX();
        double endY = ((Shape) getEndControlPoint()).getY();

        double deltaStartX = selectedMagnet.getX() - startX;
        double deltaStartY = selectedMagnet.getY() - startY;

        double startDistance = Math.sqrt(Math.pow(deltaStartX, 2)
                + Math.pow(deltaStartY, 2));

        double deltaEndX = selectedMagnet.getX() - endX;
        double deltaEndY = selectedMagnet.getY() - endY;

        double endDistance = Math.sqrt(Math.pow(deltaEndX, 2)
                + Math.pow(deltaEndY, 2));

        if (endDistance < startDistance) {
            if (!selectedMagnet.getAttachedControlPoints().contains(getEndControlPoint())) {
                selectedMagnet.attachControlPoint(getEndControlPoint());
                // This needs to be encapsulated outside here
                getEndControlPoint().setControlPointX(selectedMagnet.getX());
                getEndControlPoint().setControlPointY(selectedMagnet.getY());
//                Point2DArray array = getLine().getPoints();
//                array.getPoint(1).setX(selectedMagnet.getX());
//                array.getPoint(1).setY(selectedMagnet.getY());
                
                
                for (Magnet m : selectedMagnet.getShape().getMagnets()) {
                    if (!m.getId().equals(selectedMagnet.getId())) {
                        if (m.getAttachedControlPoints().contains(getEndControlPoint())) {
                            m.getAttachedControlPoints().remove(getEndControlPoint());
                        }
                    }
                }
            }
        } else {
            if (!selectedMagnet.getAttachedControlPoints().contains(getStartControlPoint())) {
                selectedMagnet.attachControlPoint(getStartControlPoint());
                // This needs to be encapsulated outside here
                getStartControlPoint().setControlPointX(selectedMagnet.getX());
                getStartControlPoint().setControlPointY(selectedMagnet.getY());
//                Point2DArray array = getLine().getPoints();
//                array.getPoint(1).setX(selectedMagnet.getX());
//                array.getPoint(1).setY(selectedMagnet.getY());
                
                
                for (Magnet m : selectedMagnet.getShape().getMagnets()) {
                    if (!m.getId().equals(selectedMagnet.getId())) {
                        if (m.getAttachedControlPoints().contains(getEndControlPoint())) {
                            m.getAttachedControlPoints().remove(getEndControlPoint());
                        }
                    }
                }
            }
        }
    }

}
