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
import com.emitrom.lienzo.client.core.shape.Shape;
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

public class EditableRectangle extends BaseGroupShape {

    private final String id;

    private ControlPoint topLeftControlPoint;
    private ControlPoint topRightControlPoint;
    private ControlPoint bottomLeftControlPoint;
    private ControlPoint bottomRightControlPoint;

    private Magnet topMagnet;
    private Magnet rightMagnet;
    private Magnet bottomMagnet;
    private Magnet leftMagnet;

    private Rectangle rectangle;
    
    private double currentDragX;
    private double currentDragY;

    private double startX;
    private double startY;
    private double startWidth;
    private double startHeight;

    private boolean beingDragged = false;
    private boolean beingResized = false;

    private boolean showingMagnets = false;
    private boolean showingControlPoints = false;

    public EditableRectangle(double width, double height) {
        this(width, height, 3);

        setDraggable(true);
    }

    public EditableRectangle(double width, double height, double cornerRadius) {

        rectangle = new Rectangle(width, height, cornerRadius);
        add(rectangle);
        
        this.id = UUID.uuid();

        topMagnet = new RectangleMagnetImpl(this, Magnet.MAGNET_TOP);
        rightMagnet = new RectangleMagnetImpl(this, Magnet.MAGNET_RIGHT);
        bottomMagnet = new RectangleMagnetImpl(this, Magnet.MAGNET_BOTTOM);
        leftMagnet = new RectangleMagnetImpl(this, Magnet.MAGNET_LEFT);

        topLeftControlPoint = new RectangleControlPointImpl(this, ControlPoint.CONTROL_TOP_LEFT);
        topRightControlPoint = new RectangleControlPointImpl(this, ControlPoint.CONTROL_TOP_RIGHT);
        bottomLeftControlPoint = new RectangleControlPointImpl(this, ControlPoint.CONTROL_BOTTOM_LEFT);
        bottomRightControlPoint = new RectangleControlPointImpl(this, ControlPoint.CONTROL_BOTTOM_RIGHT);

    }

    public String getId() {
        return id;
    }

    public void init(double x, double y, Layer layer) {
        super.init();
        setX(x);
        setY(y);
        currentDragX = x;
        currentDragY = y;

        addNodeMouseClickHandler(new NodeMouseClickHandler() {
            public void onNodeMouseClick(NodeMouseClickEvent nodeMouseClickEvent) {
                Layer layer = getLayer();
                ShapesUtils.nodeMouseClickHandler(EditableRectangle.this);
                ShapesUtils.deselectAllOtherShapes();
                layer.draw();
            }
        });

        addNodeDragStartHandler(new NodeDragStartHandler() {
            public void onNodeDragStart(NodeDragStartEvent nodeDragStartEvent) {

                if (topLeftControlPoint != null) {
                    hideControlPoints();
                }
                if (topMagnet != null) {
                    hideMagnetPoints();
                }
            }
        });

        addNodeDragMoveHandler(new NodeDragMoveHandler() {
            public void onNodeDragMove(NodeDragMoveEvent nodeDragMoveEvent) {
                beingDragged = true;
                currentDragX = nodeDragMoveEvent.getDragContext().getNode().getX() + nodeDragMoveEvent.getDragContext().getLocalAdjusted().getX();
                currentDragY = nodeDragMoveEvent.getDragContext().getNode().getY() + nodeDragMoveEvent.getDragContext().getLocalAdjusted().getY();
                Layer layer = getLayer();

                if (topMagnet != null && !topMagnet.getAttachedControlPoints().isEmpty()) {
                    //GWT.log("there are attached control points to topMagnet " + topMagnet.getAttachedControlPoints().size());
                    for (Object cp : topMagnet.getAttachedControlPoints()) {

                        ((ControlPoint) cp).setControlPointVisible(true);
                        ((ControlPoint) cp).setControlPointX(currentDragX + (rectangle.getWidth() / 2) - 5);
                        ((ControlPoint) cp).setControlPointY(currentDragY - 5);
                        ((ControlPoint) cp).udpateShape(layer, currentDragX + (rectangle.getWidth() / 2), currentDragY);

                    }
                }
                if (leftMagnet != null && !leftMagnet.getAttachedControlPoints().isEmpty()) {
                    for (Object cp : leftMagnet.getAttachedControlPoints()) {
                        //  GWT.log("there are attached control points to leftMagnet " + leftMagnet.getAttachedControlPoints().size());
                        ((ControlPoint) cp).setControlPointVisible(true);
                        ((ControlPoint) cp).setControlPointX(currentDragX - 5);
                        ((ControlPoint) cp).setControlPointY(currentDragY + (rectangle.getHeight() / 2) - 5);
                        ((ControlPoint) cp).udpateShape(layer, currentDragX, currentDragY + (rectangle.getHeight() / 2));

                    }
                }
                if (rightMagnet != null && !rightMagnet.getAttachedControlPoints().isEmpty()) {
                    for (Object cp : rightMagnet.getAttachedControlPoints()) {
                        // GWT.log("there are attached control points to rightMagnet " + rightMagnet.getAttachedControlPoints().size());
                        ((ControlPoint) cp).setControlPointVisible(true);
                        ((ControlPoint) cp).setControlPointX(currentDragX + rectangle.getWidth() - 5);
                        ((ControlPoint) cp).setControlPointY(currentDragY + (rectangle.getHeight() / 2) - 5);
                        ((ControlPoint) cp).udpateShape(layer, currentDragX + rectangle.getWidth(), currentDragY + (rectangle.getHeight() / 2));

                    }
                }
                if (bottomMagnet != null && !bottomMagnet.getAttachedControlPoints().isEmpty()) {
                    for (Object cp : bottomMagnet.getAttachedControlPoints()) {
                        //  GWT.log("there are attached control points to bottomMagnet " + bottomMagnet.getAttachedControlPoints().size());
                        ((ControlPoint) cp).setControlPointVisible(true);
                        ((ControlPoint) cp).setControlPointX(currentDragX + (rectangle.getWidth() / 2) - 5);
                        ((ControlPoint) cp).setControlPointY(currentDragY + rectangle.getHeight() - 5);
                        ((ControlPoint) cp).udpateShape(layer, currentDragX + (rectangle.getWidth() / 2), currentDragY + rectangle.getHeight());

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

    public void showControlPoints() {
        final Layer layer = getLayer();
        if (topLeftControlPoint != null && !showingControlPoints) {
            layer.add((Shape) topLeftControlPoint);
            layer.add((Shape) topRightControlPoint);
            layer.add((Shape) bottomLeftControlPoint);
            layer.add((Shape) bottomRightControlPoint);

            topLeftControlPoint.placeControlPoint(layer);
            bottomLeftControlPoint.placeControlPoint(layer);
            topRightControlPoint.placeControlPoint(layer);
            bottomRightControlPoint.placeControlPoint(layer);
            showingControlPoints = true;
        }

    }

    public void hideControlPoints() {

        // can be null, afer the main Shape is dragged, and control points are forcibly removed
        Layer layer = getLayer();
        if (topLeftControlPoint != null && showingControlPoints) {
            layer.remove((Shape) topLeftControlPoint);
            layer.remove((Shape) bottomLeftControlPoint);
            layer.remove((Shape) topRightControlPoint);
            layer.remove((Shape) bottomRightControlPoint);
            showingControlPoints = false;
        }

    }

    @Override
    public void hideMagnetPoints() {
        Layer layer = getLayer();
        if (topMagnet != null && showingMagnets) {
            layer.remove((Shape) topMagnet);
            layer.remove((Shape) leftMagnet);
            layer.remove((Shape) rightMagnet);
            layer.remove((Shape) bottomMagnet);
            showingMagnets = false;
        }

    }

    public void showMagnetsPoints() {
        final Layer layer = getLayer();
        if (topMagnet != null && !showingMagnets) {
            layer.add((Shape) topMagnet);
            layer.add((Shape) leftMagnet);
            layer.add((Shape) rightMagnet);
            layer.add((Shape) bottomMagnet);

            topMagnet.placeMagnetPoints();

            bottomMagnet.placeMagnetPoints();

            leftMagnet.placeMagnetPoints();

            rightMagnet.placeMagnetPoints();
            showingMagnets = true;
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

    

    @Override
    public String toString() {
        return "EditableRectangle{" + "id=" + getId() + ",x = " + getX() + ", y = " + getY() + ", beingDragged= " + beingDragged + "}";
    }

    public void attachControlPointToMagent(Magnet selectedMagnet) {
        double topLeftX = ((Shape) getTopLeftControlPoint()).getX();
        double topLeftY = ((Shape) getTopLeftControlPoint()).getY();
        double bottomLeftX = ((Shape) getBottomLeftControlPoint()).getX();
        double bottomLeftY = ((Shape) getBottomLeftControlPoint()).getY();
        double topRightX = ((Shape) getTopRightControlPoint()).getX();
        double topRightY = ((Shape) getTopRightControlPoint()).getY();

        double bottomRightX = ((Shape) getBottomRightControlPoint()).getX();
        double bottomRightY = ((Shape) getBottomRightControlPoint()).getY();

        double deltaTopLeftX = selectedMagnet.getX() - topLeftX;
        double deltaTopLeftY = selectedMagnet.getY() - topLeftY;

        double topLeftDistance = Math.sqrt(Math.pow(deltaTopLeftX, 2)
                + Math.pow(deltaTopLeftY, 2));

        double deltaBottomLeftX = selectedMagnet.getX() - bottomLeftX;
        double deltaBottomLeftY = selectedMagnet.getY() - bottomLeftY;

        double bottomLeftDistance = Math.sqrt(Math.pow(deltaBottomLeftX, 2)
                + Math.pow(deltaBottomLeftY, 2));

        double deltaTopRightX = selectedMagnet.getX() - topRightX;
        double deltaTopRightY = selectedMagnet.getY() - topRightY;

        double topRightDistance = Math.sqrt(Math.pow(deltaTopRightX, 2)
                + Math.pow(deltaTopRightY, 2));

        double deltaBottomRightX = selectedMagnet.getX() - bottomRightX;
        double deltaBottomRightY = selectedMagnet.getY() - bottomRightY;

        double bottomRightDistance = Math.sqrt(Math.pow(deltaBottomRightX, 2)
                + Math.pow(deltaBottomRightY, 2));

        
        
        if (topLeftDistance < bottomLeftDistance && topLeftDistance < topRightDistance && topLeftDistance < topLeftDistance) {
            if (!selectedMagnet.getAttachedControlPoints().contains(getTopLeftControlPoint())) {
                selectedMagnet.attachControlPoint(getTopLeftControlPoint());
            }
        } else if(bottomLeftDistance < topLeftDistance && bottomLeftDistance < topRightDistance && bottomLeftDistance < bottomRightDistance ){
            if (!selectedMagnet.getAttachedControlPoints().contains(getBottomLeftControlPoint())) {
                selectedMagnet.attachControlPoint(getBottomLeftControlPoint());
            }
        } else if(topRightDistance < topLeftDistance && topRightDistance < bottomLeftDistance && topRightDistance < bottomRightDistance ){
            if (!selectedMagnet.getAttachedControlPoints().contains(getTopRightControlPoint())) {
                selectedMagnet.attachControlPoint(getTopRightControlPoint());
            }
        } else if(bottomRightDistance < topLeftDistance && bottomRightDistance < bottomLeftDistance && bottomRightDistance < topRightDistance ){
            if (!selectedMagnet.getAttachedControlPoints().contains(getBottomRightControlPoint())) {
                selectedMagnet.attachControlPoint(getBottomRightControlPoint());
            }
        }
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
    
    

}
