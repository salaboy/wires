/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import com.emitrom.lienzo.client.core.shape.Circle;
import com.emitrom.lienzo.client.core.shape.Layer;
import com.emitrom.lienzo.client.core.shape.Line;
import com.emitrom.lienzo.client.core.shape.Rectangle;
import com.emitrom.lienzo.client.core.shape.Shape;
import com.emitrom.lienzo.client.core.types.Point2D;
import com.emitrom.lienzo.client.core.types.Point2DArray;
import com.emitrom.lienzo.shared.core.types.ColorName;
import com.google.gwt.core.client.GWT;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.kie.wires.client.shapes.collision.CollidableShape;
import org.kie.wires.client.util.UUID;
import org.kie.wires.client.util.collision.Projection;
import org.kie.wires.client.util.collision.Vector;

/**
 *
 * @author salaboy
 */
public class EditableLine extends Line implements EditableShape, CollidableShape {

    private String id;
    private Rectangle start;
    private Rectangle end;

    private double dragEventStartX;
    private double dragEventStartY;

    private double dragEventEndX;
    private double dragEventEndY;

    private double initialStartPointX;
    private double initialStartPointY;
    private double initialEndPointX;
    private double initialEndPointY;

    private double currentDragX;
    private double currentDragY;

    private boolean beingDragged;

    private boolean beingResized;

    private static final int MAGNET_START = 0;
    private static final int MAGNET_END = 1;

    private Circle startMagnet;
    private Circle endMagnet;

    public EditableLine(double x1, double y1, double x2, double y2) {
        super(x1, y1, x2, y2);
        this.id = UUID.uuid();
    }

    public String getId() {
        return id;
    }

    @Override
    public void init(double x, double y) {

        setX(x);
        setY(y);
        currentDragX = x;
        currentDragY = y;
        
        addNodeMouseClickHandler(new NodeMouseClickHandler() {
            public void onNodeMouseClick(NodeMouseClickEvent nodeMouseClickEvent) {
                ShapesUtils.nodeMouseClickHandler(EditableLine.this);
                ShapesUtils.deselectAllOtherShapes(getLayer());
            }
        });

        addNodeMouseEnterHandler(new NodeMouseEnterHandler() {
            public void onNodeMouseEnter(NodeMouseEnterEvent nodeMouseEnterEvent) {
                ShapesUtils.nodeMouseEnterHandler(EditableLine.this);

            }
        });

        addNodeMouseOverHandler(new NodeMouseOverHandler() {
            public void onNodeMouseOver(NodeMouseOverEvent nodeMouseOverEvent) {
                ShapesUtils.nodeMouseOverHandler(EditableLine.this);

            }
        });

        addNodeMouseExitHandler(new NodeMouseExitHandler() {
            public void onNodeMouseExit(NodeMouseExitEvent nodeMouseExitEvent) {
                ShapesUtils.nodeMouseExitHandler(EditableLine.this);

            }
        });

        addNodeDragStartHandler(new NodeDragStartHandler() {
            public void onNodeDragStart(NodeDragStartEvent nodeDragStartEvent) {
                recordStartData(nodeDragStartEvent);
                if (start != null) {
                    hideDragPoints();
                }
                if (startMagnet != null) {
                    hideMagnetPoints();
                }
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

    }

    public void recordStartData(NodeDragStartEvent nodeDragStartEvent) {
        dragEventStartX = nodeDragStartEvent.getX();
        dragEventStartY = nodeDragStartEvent.getY();

        Point2DArray points = getPoints();
        Point2D startPoint = points.getPoint(0);
        Point2D endPoint = points.getPoint(1);

        initialStartPointX = startPoint.getX();
        initialStartPointY = startPoint.getY();

        initialEndPointX = endPoint.getX();
        initialEndPointY = endPoint.getY();
    }

    @Override
    public void showDragPoints() {
        if (start == null) {
            final Layer layer = getLayer();
            start = new Rectangle(10, 10);
            end = new Rectangle(10, 10);
            start.setFillColor(ShapesUtils.LIGHT_BLUE);
            end.setFillColor(ShapesUtils.LIGHT_BLUE);
            createControlPoints(start, end, getLayer());

            layer.draw();
        }
    }

    @Override
    public void hideDragPoints() {

        if (start != null) {
            // can be null, afer the main Shape is dragged, and control points are forcibly removed
            Layer layer = getLayer();

            layer.remove(start);
            start = null;

            layer.remove(end);
            end = null;

            layer.draw();
        }
    }

    private void createControlPoints(final Rectangle start, final Rectangle end, final Layer layer) {
        Point2DArray array = getPoints();

        start.setX(getX() + array.getPoint(0).getX() - 5);
        start.setY(getY() + array.getPoint(0).getY() - 5);

        start.addNodeDragStartHandler(new NodeDragStartHandler() {
            public void onNodeDragStart(NodeDragStartEvent nodeDragStartEvent) {
                dragEventStartX = nodeDragStartEvent.getX();
                dragEventStartY = nodeDragStartEvent.getY();
                initialStartPointX = getPoints().getPoint(0).getX();
                initialStartPointY = getPoints().getPoint(0).getY();
            }
        });

        start.addNodeDragMoveHandler(new NodeDragMoveHandler() {
            public void onNodeDragMove(NodeDragMoveEvent nodeDragMoveEvent) {
                beingResized = true;
                double deltaX = nodeDragMoveEvent.getX() - dragEventStartX;
                double deltaY = nodeDragMoveEvent.getY() - dragEventStartY;

                Point2DArray array = getPoints();
                array.getPoint(0).setX(initialStartPointX + deltaX);
                array.getPoint(0).setY(initialStartPointY + deltaY);
                                
                layer.draw();
            }
        });

        start.addNodeDragEndHandler(new NodeDragEndHandler() {
            public void onNodeDragEnd(NodeDragEndEvent nodeDragEndEvent) {
                beingResized = false;

            }
        });

        end.setX(getX() + array.getPoint(1).getX() - 5);
        end.setY(getY() + array.getPoint(1).getY() - 5);

        end.addNodeDragStartHandler(new NodeDragStartHandler() {
            public void onNodeDragStart(NodeDragStartEvent nodeDragStartEvent) {
                dragEventEndX = nodeDragStartEvent.getX();
                dragEventEndY = nodeDragStartEvent.getY();
                initialEndPointX = getPoints().getPoint(1).getX();
                initialEndPointY = getPoints().getPoint(1).getY();
            }
        });

        end.addNodeDragMoveHandler(new NodeDragMoveHandler() {
            public void onNodeDragMove(NodeDragMoveEvent nodeDragMoveEvent) {
                beingResized = true;
                double deltaX = nodeDragMoveEvent.getX() - dragEventEndX;
                double deltaY = nodeDragMoveEvent.getY() - dragEventEndY;
                
                
                
                Point2DArray array = getPoints();
                array.getPoint(1).setX(initialEndPointX + deltaX);
                array.getPoint(1).setY(initialEndPointY + deltaY);

                layer.draw();
            }
        });

        end.addNodeDragEndHandler(new NodeDragEndHandler() {
            public void onNodeDragEnd(NodeDragEndEvent nodeDragEndEvent) {
                beingResized = false;

            }
        });

        start.setDraggable(true).setStrokeWidth(1)
                .setStrokeColor(ColorName.BLACK);
        end.setDraggable(true)
                .setStrokeWidth(1)
                .setStrokeColor(ColorName.BLACK);
        layer.add(start);
        layer.add(end);

    }

    public void showMagnetsPoints() {
        if (startMagnet == null) {
            final Layer layer = getLayer();

            startMagnet = new Circle(5);
            startMagnet.addNodeMouseEnterHandler(new NodeMouseEnterHandler() {
                public void onNodeMouseEnter(NodeMouseEnterEvent nodeMouseEnterEvent) {

                }
            });
            startMagnet.setFillColor(ColorName.YELLOW);
            placeMagnetPoints(startMagnet, layer, MAGNET_START);

            endMagnet = new Circle(5);
            endMagnet.addNodeMouseEnterHandler(new NodeMouseEnterHandler() {
                public void onNodeMouseEnter(NodeMouseEnterEvent nodeMouseEnterEvent) {

                }
            });
            endMagnet.setFillColor(ColorName.YELLOW);
            placeMagnetPoints(endMagnet, layer, MAGNET_END);

            layer.draw();
        }
    }

    public void hideMagnetPoints() {
        if (startMagnet != null) {
            // can be null, afer the main Shape is dragged, and control points are forcibly removed
            Layer layer = getLayer();
            layer.remove(startMagnet);
            startMagnet = null;

            layer.remove(endMagnet);
            endMagnet = null;

            layer.draw();
        }
    }

    private void placeMagnetPoints(Circle magnet, Layer layer, int control) {
        Point2DArray points = getPoints();

        switch (control) {
            case MAGNET_START:
                magnet.setX(getX() + points.getPoint(0).getX());
                magnet.setY(getY() + points.getPoint(0).getY());
                break;
            case MAGNET_END:
                magnet.setX(getX() + points.getPoint(1).getX());
                magnet.setY(getY() + points.getPoint(1).getY());
                break;

        }
        layer.add(magnet);
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
        Point2DArray points = getPoints();
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

        Point2DArray points = getPoints();
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

    public boolean isBeingResized() {
        return beingResized;
    }

    public double getInitialStartPointX() {
        return initialStartPointX;
    }

    public double getInitialStartPointY() {
        return initialStartPointY;
    }

    public double getInitialEndPointX() {
        return initialEndPointX;
    }

    public double getInitialEndPointY() {
        return initialEndPointY;
    }

    public double getDragEventStartX() {
        return dragEventStartX;
    }

    public double getDragEventStartY() {
        return dragEventStartY;
    }

    public double getDragEventEndX() {
        return dragEventEndX;
    }

    public double getDragEventEndY() {
        return dragEventEndY;
    }

    public double getCurrentDragX() {
        return currentDragX;
    }

    public double getCurrentDragY() {
        return currentDragY;
    }

    public List<Shape> getMagnets() {
        ArrayList<Shape> magnets = new ArrayList<Shape>(2);
        magnets.add(startMagnet);
        magnets.add(endMagnet);
        return magnets;
    }
    
     @Override
    public String toString() {
        return "EditableLine{" + "id=" + getId() + ",x = " + getX() + ", y = " + getY() + ", beingDragged= " + beingDragged + "}";
    }

}
