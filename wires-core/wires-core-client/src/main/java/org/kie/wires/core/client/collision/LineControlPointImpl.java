/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kie.wires.core.client.collision;

import com.emitrom.lienzo.client.core.event.NodeDragEndEvent;
import com.emitrom.lienzo.client.core.event.NodeDragEndHandler;
import com.emitrom.lienzo.client.core.event.NodeDragMoveEvent;
import com.emitrom.lienzo.client.core.event.NodeDragMoveHandler;
import com.emitrom.lienzo.client.core.event.NodeDragStartEvent;
import com.emitrom.lienzo.client.core.event.NodeDragStartHandler;
import com.emitrom.lienzo.client.core.shape.Layer;
import com.emitrom.lienzo.client.core.shape.Rectangle;
import com.emitrom.lienzo.client.core.types.Point2DArray;
import com.emitrom.lienzo.shared.core.types.ColorName;
import org.kie.wires.core.api.collision.CollisionManager;
import org.kie.wires.core.api.collision.ControlPoint;
import org.kie.wires.core.api.collision.Magnet;
import org.kie.wires.core.api.collision.RequiresCollisionManager;
import org.kie.wires.core.api.shapes.WiresBaseGroupShape;
import org.kie.wires.core.client.shapes.WiresLine;
import org.kie.wires.core.client.util.UUID;

import static org.kie.wires.core.client.util.ShapesUtils.*;

/**
 * @author salaboy
 */
public class LineControlPointImpl extends Rectangle implements ControlPoint,
                                                               RequiresCollisionManager {

    private String id;

    private double initialStartPointX;
    private double initialStartPointY;

    private double initialEndPointX;
    private double initialEndPointY;

    private double dragEventStartX;
    private double dragEventStartY;

    private double dragEventEndX;
    private double dragEventEndY;

    private WiresLine shape;
    private int controlType = 0;

    private Magnet selectedMagnet = null;
    private CollisionManager collisionManager;

    private boolean attached = false;

    public LineControlPointImpl( final WiresLine shape,
                                 final int controlType ) {
        super( 12,
               12 );
        id = UUID.uuid();
        this.shape = shape;
        this.controlType = controlType;
        setFillColor( CP_RGB_FILL_COLOR );
        setStrokeWidth( CP_RGB_STROKE_WIDTH_SHAPE );
    }

    @Override
    public void setCollisionManager( final CollisionManager manager ) {
        this.collisionManager = manager;
    }

    @Override
    public int getControlType() {
        return controlType;
    }

    @Override
    public WiresBaseGroupShape getShape() {
        return shape;
    }

    public void setShape( final WiresLine shape ) {
        this.shape = shape;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void placeControlPoint( final Layer layer ) {
        moveControlPoint();

        switch ( controlType ) {
            case ControlPoint.CONTROL_START:

                addNodeDragStartHandler( new NodeDragStartHandler() {

                    @Override
                    public void onNodeDragStart( NodeDragStartEvent nodeDragStartEvent ) {
                        dragEventStartX = nodeDragStartEvent.getX();
                        dragEventStartY = nodeDragStartEvent.getY();
                        initialStartPointX = shape.getBounding().getPoints().getPoint( 0 ).getX();
                        initialStartPointY = shape.getBounding().getPoints().getPoint( 0 ).getY();
                        shape.hideMagnetPoints();
                    }
                } );

                addNodeDragMoveHandler( new NodeDragMoveHandler() {

                    @Override
                    public void onNodeDragMove( NodeDragMoveEvent nodeDragMoveEvent ) {
                        shape.setBeingResized( true );
                        double deltaX = nodeDragMoveEvent.getX() - dragEventStartX;
                        double deltaY = nodeDragMoveEvent.getY() - dragEventStartY;

                        Point2DArray array = shape.getBounding().getPoints();
                        array.getPoint( 0 ).setX( initialStartPointX + deltaX );
                        array.getPoint( 0 ).setY( initialStartPointY + deltaY );

                        Point2DArray arrayLine = shape.getLine().getPoints();
                        arrayLine.getPoint( 0 ).setX( initialStartPointX + deltaX );
                        arrayLine.getPoint( 0 ).setY( initialStartPointY + deltaY );

                        selectedMagnet = collisionManager.getMagnet( shape,
                                                                     nodeDragMoveEvent.getX(),
                                                                     nodeDragMoveEvent.getY() );

                        layer.draw();
                    }
                } );

                addNodeDragEndHandler( new NodeDragEndHandler() {

                    @Override
                    public void onNodeDragEnd( NodeDragEndEvent nodeDragEndEvent ) {
                        shape.setBeingResized( false );

                        if ( selectedMagnet != null ) {
                            double deltaX = getControlPointX() - selectedMagnet.getX();
                            double deltaY = getControlPointY() - selectedMagnet.getY();
                            double distance = Math.sqrt( Math.pow( deltaX, 2 ) + Math.pow( deltaY, 2 ) );
                            if ( distance < 30 ) {
                                collisionManager.attachControlPointToMagnet( selectedMagnet,
                                                                             shape );
                                attached = true;
                            } else {
                                attached = false;
                            }
                        }

                        layer.draw();
                    }
                } );

                setDraggable( true ).setStrokeWidth( 1 ).setStrokeColor( ColorName.BLACK );

                break;
            case ControlPoint.CONTROL_END:

                addNodeDragStartHandler( new NodeDragStartHandler() {

                    @Override
                    public void onNodeDragStart( NodeDragStartEvent nodeDragStartEvent ) {
                        shape.hideMagnetPoints();
                        dragEventEndX = nodeDragStartEvent.getX();
                        dragEventEndY = nodeDragStartEvent.getY();
                        initialEndPointX = shape.getBounding().getPoints().getPoint( 1 ).getX();
                        initialEndPointY = shape.getBounding().getPoints().getPoint( 1 ).getY();
                        shape.hideMagnetPoints();
                    }
                } );

                addNodeDragMoveHandler( new NodeDragMoveHandler() {

                    @Override
                    public void onNodeDragMove( NodeDragMoveEvent nodeDragMoveEvent ) {
                        shape.setBeingResized( true );
                        double deltaX = nodeDragMoveEvent.getX() - dragEventEndX;
                        double deltaY = nodeDragMoveEvent.getY() - dragEventEndY;

                        Point2DArray array = shape.getBounding().getPoints();
                        array.getPoint( 1 ).setX( initialEndPointX + deltaX );
                        array.getPoint( 1 ).setY( initialEndPointY + deltaY );

                        Point2DArray arrayLine = shape.getLine().getPoints();
                        arrayLine.getPoint( 1 ).setX( initialEndPointX + deltaX );
                        arrayLine.getPoint( 1 ).setY( initialEndPointY + deltaY );

                        selectedMagnet = collisionManager.getMagnet( shape,
                                                                     nodeDragMoveEvent.getX(),
                                                                     nodeDragMoveEvent.getY() );

                        layer.draw();
                    }
                } );

                addNodeDragEndHandler( new NodeDragEndHandler() {

                    @Override
                    public void onNodeDragEnd( NodeDragEndEvent nodeDragEndEvent ) {
                        shape.setBeingResized( false );
                        if ( selectedMagnet != null ) {
                            double deltaX = getControlPointX() - selectedMagnet.getX();
                            double deltaY = getControlPointY() - selectedMagnet.getY();
                            double distance = Math.sqrt( Math.pow( deltaX, 2 ) + Math.pow( deltaY, 2 ) );
                            if ( distance < 30 ) {
                                collisionManager.attachControlPointToMagnet( selectedMagnet,
                                                                             shape );
                                attached = true;
                            } else {
                                attached = false;
                            }
                        }
                    }
                } );

                setDraggable( true )
                        .setStrokeWidth( 1 )
                        .setStrokeColor( ColorName.BLACK );

                break;
        }

    }

    @Override
    public void moveControlPoint() {
        Point2DArray array = shape.getBounding().getPoints();
        switch ( controlType ) {
            case ControlPoint.CONTROL_START:

                setControlPointX( shape.getX() + array.getPoint( 0 ).getX() - 6 );
                setControlPointY( shape.getY() + array.getPoint( 0 ).getY() - 6 );

                break;
            case ControlPoint.CONTROL_END:

                setControlPointX( shape.getX() + array.getPoint( 1 ).getX() - 6 );
                setControlPointY( shape.getY() + array.getPoint( 1 ).getY() - 6 );

                break;
        }

    }

    @Override
    public void updateShape( final Layer layer,
                             final double x,
                             final double y ) {
        Point2DArray array = shape.getBounding().getPoints();
        Point2DArray arrayLine = shape.getLine().getPoints();
        setControlPointX( x - 6 );
        setControlPointY( y - 6 );
        switch ( controlType ) {
            case ControlPoint.CONTROL_START:

                array.getPoint( 0 ).setX( x - shape.getX() );
                array.getPoint( 0 ).setY( y - shape.getY() );

                arrayLine.getPoint( 0 ).setX( x - shape.getX() );
                arrayLine.getPoint( 0 ).setY( y - shape.getY() );

                break;
            case ControlPoint.CONTROL_END:

                array.getPoint( 1 ).setX( x - ( shape.getX() ) );
                array.getPoint( 1 ).setY( y - ( shape.getY() ) );

                arrayLine.getPoint( 1 ).setX( x - ( shape.getX() ) );
                arrayLine.getPoint( 1 ).setY( y - ( shape.getY() ) );

                break;
        }
        layer.draw();

    }

    @Override
    public void setControlPointX( final double x ) {
        setX( x );
    }

    @Override
    public void setControlPointY( final double y ) {
        setY( y );
    }

    @Override
    public double getControlPointX() {
        return getX();
    }

    @Override
    public double getControlPointY() {
        return getY();
    }

    @Override
    public String toString() {
        return "LineControlPointImpl{" + "id=" + id + ", controlType=" + controlType + ", attached=" + attached + '}';
    }

    @Override
    public boolean isAttached() {
        return attached;
    }

    public void setControlPointVisible( final boolean visible ) {
        setVisible( visible );
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + ( this.id != null ? this.id.hashCode() : 0 );
        hash = 89 * hash + (int) ( Double.doubleToLongBits( this.initialStartPointX ) ^ ( Double.doubleToLongBits( this.initialStartPointX ) >>> 32 ) );
        hash = 89 * hash + (int) ( Double.doubleToLongBits( this.initialStartPointY ) ^ ( Double.doubleToLongBits( this.initialStartPointY ) >>> 32 ) );
        hash = 89 * hash + (int) ( Double.doubleToLongBits( this.initialEndPointX ) ^ ( Double.doubleToLongBits( this.initialEndPointX ) >>> 32 ) );
        hash = 89 * hash + (int) ( Double.doubleToLongBits( this.initialEndPointY ) ^ ( Double.doubleToLongBits( this.initialEndPointY ) >>> 32 ) );
        hash = 89 * hash + ( this.shape != null ? this.shape.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object obj ) {
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        final LineControlPointImpl other = (LineControlPointImpl) obj;
        if ( ( this.id == null ) ? ( other.id != null ) : !this.id.equals( other.id ) ) {
            return false;
        }
        if ( Double.doubleToLongBits( this.initialStartPointX ) != Double.doubleToLongBits( other.initialStartPointX ) ) {
            return false;
        }
        if ( Double.doubleToLongBits( this.initialStartPointY ) != Double.doubleToLongBits( other.initialStartPointY ) ) {
            return false;
        }
        if ( Double.doubleToLongBits( this.initialEndPointX ) != Double.doubleToLongBits( other.initialEndPointX ) ) {
            return false;
        }
        if ( Double.doubleToLongBits( this.initialEndPointY ) != Double.doubleToLongBits( other.initialEndPointY ) ) {
            return false;
        }
        if ( this.shape != other.shape && ( this.shape == null || !this.shape.equals( other.shape ) ) ) {
            return false;
        }
        return true;
    }

}
