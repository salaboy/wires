package org.kie.wires.client.shapes;

import java.util.List;

import org.kie.wires.client.util.collision.Projection;
import org.kie.wires.client.util.collision.Vector;

import com.emitrom.lienzo.client.core.event.NodeDragMoveEvent;
import com.emitrom.lienzo.client.core.event.NodeDragMoveHandler;
import com.emitrom.lienzo.client.core.event.NodeDragStartEvent;
import com.emitrom.lienzo.client.core.event.NodeDragStartHandler;
import com.emitrom.lienzo.client.core.event.NodeMouseEnterEvent;
import com.emitrom.lienzo.client.core.event.NodeMouseEnterHandler;
import com.emitrom.lienzo.client.core.event.NodeMouseExitEvent;
import com.emitrom.lienzo.client.core.event.NodeMouseExitHandler;
import com.emitrom.lienzo.client.core.shape.Circle;
import com.emitrom.lienzo.client.core.shape.Layer;
import com.emitrom.lienzo.client.core.shape.Rectangle;
import com.emitrom.lienzo.client.core.shape.Shape;
import com.emitrom.lienzo.shared.core.types.ColorName;
import com.google.gwt.core.client.GWT;


public class EditableCircle extends Circle implements EditableShape {
    private static final int TOP = 0;
    private static final int BOTTOM = 1;
    private static final int RIGHT = 2;
    private static final int LEFT = 3;

    private Rectangle top;
    private Rectangle right;
    private Rectangle bottom;
    private Rectangle left;

    private double dragEventStartX;
    private double dragEventStartY;
    private double startX;
    private double startY;
    private double startRadius;
  

    public EditableCircle(double radius) {
        super(radius);
    }

    

    public void init(double x, double y) {
        setX( x );
        setY( y );

        addNodeMouseEnterHandler(new NodeMouseEnterHandler() {
            public void onNodeMouseEnter(NodeMouseEnterEvent nodeMouseEnterEvent) {
                ShapesUtils.nodeMouseClickHandler(EditableCircle.this);
            }
        });

        addNodeMouseExitHandler(new NodeMouseExitHandler() {
            public void onNodeMouseExit(NodeMouseExitEvent nodeMouseExitEvent) {
                ShapesUtils.nodeMouseExitHandler(EditableCircle.this);
            }
        });

        addNodeDragStartHandler(new NodeDragStartHandler() {
            public void onNodeDragStart(NodeDragStartEvent nodeDragStartEvent) {
               
                if (top != null) {
                    hideControlPoints();
                }
            }
        });
    }

    public Rectangle getTop() {
        return top;
    }

    public void setTop(Rectangle top) {
        this.top = top;
    }

    public Rectangle getRight() {
        return right;
    }

    public void setRight(Rectangle right) {
        this.right = right;
    }

    public Rectangle getBottom() {
        return bottom;
    }

    public void setBottom(Rectangle bottom) {
        this.bottom = bottom;
    }

    public Rectangle getLeft() {
        return left;
    }

    public void setLeft(Rectangle left) {
        this.left = left;
    }

    public double getDragEventStartX() {
        return dragEventStartX;
    }

    public void setDragEventStartX(double dragEventStartX) {
        this.dragEventStartX = dragEventStartX;
    }

    public double getDragEventStartY() {
        return dragEventStartY;
    }

    public void setDragEventStartY(double dragEventStartY) {
        this.dragEventStartY = dragEventStartY;
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
        return startRadius;
    }

    public void setStartWidth(double startWidth) {
        this.startRadius = startWidth;
    }

   
    public void showControlPoints() {
        if ( top == null ) {
            GWT.log("show");

            // Can be null, if we enter, after an exit but the timer has not removed the points yet
            final Layer layer = getLayer();

            top = new Rectangle(6, 6);
            createControlPoints(top, layer, TOP);

            bottom = new Rectangle(6, 6);
            createControlPoints(bottom, layer, BOTTOM);

            right = new Rectangle(6, 6);
            createControlPoints(right, layer, RIGHT);

            left = new Rectangle(6, 6);
            createControlPoints(left, layer, LEFT);

            layer.draw();
        }
    }


    public void hideControlPoints() {
        GWT.log("hide");
        if ( top != null ) {
            // can be null, afer the main Shape is dragged, and control points are forcibly removed
            Layer layer = getLayer();

            layer.remove( top );
            top = null;

            layer.remove( bottom );
            bottom = null;

            layer.remove( right );
            right = null;

            layer.remove( left );
            left = null;

            layer.draw();
        }
    }

    public void recordStartData(NodeDragStartEvent nodeDragStartEvent) {
        dragEventStartX = nodeDragStartEvent.getX();
        dragEventStartY = nodeDragStartEvent.getY();

        startX = getX();
        startY = getY();
        startRadius = getRadius();
    }

    private void createControlPoints(Rectangle rect, Layer layer, final int control) {
        switch( control ) {
            case TOP: 
                rect.setX( getX() )
                     .setY( getY() + getRadius() );
                break;
            case BOTTOM: 
                rect.setX( getX() )
                     .setY( getY() - getRadius());
                break;
            case RIGHT: 
                rect.setX( getX() + getRadius() )
                     .setY( getY() );
                break;
            case LEFT: 
                rect.setX( getX() - getRadius()  )
                     .setY( getY()  );
                break;
        }


        rect.setDraggable( true )
            .setStrokeWidth( 1 )
            .setStrokeColor( ColorName.BLACK );

        rect.addNodeMouseEnterHandler(new NodeMouseEnterHandler() {
            public void onNodeMouseEnter(NodeMouseEnterEvent nodeMouseEnterEvent) {
                ShapesUtils.nodeMouseClickHandler(EditableCircle.this);
            }
        });

        rect.addNodeMouseExitHandler(new NodeMouseExitHandler() {
            public void onNodeMouseExit(NodeMouseExitEvent nodeMouseExitEvent) {
                ShapesUtils.nodeMouseExitHandler(EditableCircle.this);
            }
        });

        rect.addNodeDragStartHandler( new NodeDragStartHandler() {
            public void onNodeDragStart( NodeDragStartEvent nodeDragStartEvent ) {
                GWT.log("move start");
                recordStartData(nodeDragStartEvent);
            }
        } );

        rect.addNodeDragMoveHandler( new NodeDragMoveHandler() {
            public void onNodeDragMove( NodeDragMoveEvent nodeDragMoveEvent ) {
                nodeDragMove(EditableCircle.this, control, nodeDragMoveEvent, getLayer());
            }
        } );
//
        layer.add( rect );
    }

    public static void nodeDragMove( EditableCircle circle, int control, NodeDragMoveEvent nodeDragMoveEvent, Layer layer) {
        GWT.log("move");

        double deltaX = nodeDragMoveEvent.getX() - circle.getDragEventStartX();
        double deltaY = nodeDragMoveEvent.getY() - circle.getDragEventStartY();
        double radius = 5;
        switch( control ) {
            case TOP: 
                circle.setX( circle.getStartX() + deltaX );
                circle.setY( circle.getStartY() + deltaY );
                radius = circle.getRadius() + deltaX;
                if(radius < 5){
                    radius = 5;
                }
                circle.setRadius(radius);
                
                break;
            case BOTTOM: 
                circle.setX( circle.getStartX() + deltaX );
                radius = circle.getRadius() +  (-deltaX);
                if(radius < 5){
                    radius = 5;
                }
                circle.setRadius( radius);
                
                break;
            case RIGHT: 
                circle.setY( circle.getStartY() + deltaY );
                radius = circle.getRadius() + deltaY;
                if(radius < 5){
                    radius = 5;
                }
                circle.setRadius(radius );
                break;
            case LEFT:
                radius = circle.getRadius() + (-deltaX);
                if(radius < 5){
                    radius = 5;
                }
                circle.setRadius(radius );
                break;
        }

        switch( control ) {
            case TOP:
                circle.getBottom().setX( circle.getX() - circle.getRadius() );
                circle.getRight().setY( circle.getY() + circle.getRadius() );
                break;
            case BOTTOM:
                circle.getTop().setX( circle.getX() + circle.getRadius() );
                circle.getLeft().setY( circle.getY() - circle.getRadius() );
                break;
            case RIGHT:
                circle.getTop().setY( circle.getY() + circle.getRadius() );
                circle.getLeft().setX( circle.getX() - circle.getRadius());
                break;
            case LEFT:
                circle.getBottom().setY( circle.getY() - circle.getRadius() );
                circle.getRight().setX( circle.getX() + circle.getRadius()  );
                break;
        }

        layer.draw();
    }

    public void showMagnetsPoints() {
        
    }

    public void hideMagnetPoints() {
        
    }

    public boolean collidesWith(EditableShape shape) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean separationOnAxes(List<Vector> axes, EditableShape shape) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<Vector> getAxes() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Projection project(Vector axis) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean isBeingDragged() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getId() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public double getCurrentDragX() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public double getCurrentDragY() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean isBeingResized() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<Shape> getMagnets() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
