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

import com.emitrom.lienzo.client.core.event.NodeDragMoveEvent;
import com.emitrom.lienzo.client.core.event.NodeDragMoveHandler;
import com.emitrom.lienzo.client.core.event.NodeDragStartEvent;
import com.emitrom.lienzo.client.core.event.NodeDragStartHandler;
import com.emitrom.lienzo.client.core.shape.Layer;
import com.emitrom.lienzo.client.core.shape.Rectangle;
import com.emitrom.lienzo.shared.core.types.ColorName;
import org.kie.wires.core.api.shapes.ControlPoint;
import org.kie.wires.core.api.shapes.Magnet;
import org.kie.wires.core.client.shapes.dynamic.WiresRectangle;
import org.kie.wires.core.client.util.UUID;

import static org.kie.wires.core.client.util.ShapesUtils.*;

/**
 * @author salaboy
 */
public class RectangleControlPointImpl extends Rectangle implements ControlPoint {

    private double dragEventStartX;
    private double dragEventStartY;

    private String id;

    private WiresRectangle shape;

    private int controlType;

    private boolean attached = false;

    public RectangleControlPointImpl( final WiresRectangle shape,
                                      final int controlType ) {
        super( 12,
               12 );
        id = UUID.uuid();
        this.shape = shape;
        this.controlType = controlType;
        setFillColor( CP_RGB_FILL_COLOR );
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public WiresRectangle getShape() {
        return shape;
    }

    @Override
    public boolean isAttached() {
        return attached;
    }

    @Override
    public void placeControlPoint( final Layer layer ) {

        moveControlPoint();

        setDraggable( true )
                .setStrokeWidth( CP_RGB_STROKE_WIDTH_SHAPE )
                .setStrokeColor( ColorName.BLACK );

        addNodeDragStartHandler( new NodeDragStartHandler() {
            @Override
            public void onNodeDragStart( NodeDragStartEvent nodeDragStartEvent ) {
                shape.hideMagnetPoints();
                recordStartData( shape,
                                 nodeDragStartEvent );
            }
        } );

        addNodeDragMoveHandler( new NodeDragMoveHandler() {
            @Override
            public void onNodeDragMove( NodeDragMoveEvent nodeDragMoveEvent ) {
                nodeDragMove( shape,
                              nodeDragMoveEvent );
                layer.draw();
            }

        } );
    }

    @Override
    public void moveControlPoint() {
        switch ( controlType ) {
            case CONTROL_TOP_LEFT:
                setControlPointX( shape.getX() );
                setControlPointY( shape.getY() );
                break;
            case CONTROL_BOTTOM_LEFT:
                setControlPointX( shape.getX() );
                setControlPointY( shape.getY() + shape.getBounding().getHeight() - 12 );
                break;
            case CONTROL_TOP_RIGHT:
                setControlPointX( shape.getX() + shape.getBounding().getWidth() - 12 );
                setControlPointY( shape.getY() );
                break;
            case CONTROL_BOTTOM_RIGHT:
                setControlPointX( shape.getX() + shape.getBounding().getWidth() - 12 );
                setControlPointY( shape.getY() + shape.getBounding().getHeight() - 12 );
                break;
        }
    }

    public void recordStartData( final WiresRectangle destination,
                                 final NodeDragStartEvent nodeDragStartEvent ) {
        dragEventStartX = nodeDragStartEvent.getX();
        dragEventStartY = nodeDragStartEvent.getY();
        destination.setStartX( destination.getX() );
        destination.setStartY( destination.getY() );
        destination.setStartHeight( destination.getRectangle().getHeight() );
        destination.setStartWidth( destination.getRectangle().getWidth() );
    }

    public void nodeDragMove( final WiresRectangle rect,
                              final double deltaX,
                              final double deltaY ) {
        switch ( controlType ) {
            case CONTROL_TOP_LEFT:
                rect.setX( rect.getStartX() + deltaX );
                rect.setY( rect.getStartY() + deltaY );

                rect.getRectangle().setWidth( rect.getStartWidth() - deltaX );
                rect.getRectangle().setHeight( rect.getStartHeight() - deltaY );

                rect.getBounding().setWidth( rect.getRectangle().getWidth() + 12 );
                rect.getBounding().setHeight( rect.getRectangle().getHeight() + 12 );

                break;
            case CONTROL_BOTTOM_LEFT:
                rect.setX( rect.getStartX() + deltaX );

                rect.getRectangle().setWidth( rect.getStartWidth() - deltaX );
                rect.getRectangle().setHeight( rect.getStartHeight() + deltaY );

                rect.getBounding().setWidth( rect.getRectangle().getWidth() + 12 );
                rect.getBounding().setHeight( rect.getRectangle().getHeight() + 12 );

                break;
            case CONTROL_TOP_RIGHT:
                rect.setY( rect.getStartY() + deltaY );

                rect.getRectangle().setWidth( rect.getStartWidth() + deltaX );
                rect.getRectangle().setHeight( rect.getStartHeight() - deltaY );

                rect.getBounding().setWidth( rect.getRectangle().getWidth() + 12 );
                rect.getBounding().setHeight( rect.getRectangle().getHeight() + 12 );

                break;
            case CONTROL_BOTTOM_RIGHT:
                rect.getRectangle().setWidth( rect.getStartWidth() + deltaX );
                rect.getRectangle().setHeight( rect.getStartHeight() + deltaY );

                rect.getBounding().setWidth( rect.getRectangle().getWidth() + 12 );
                rect.getBounding().setHeight( rect.getRectangle().getHeight() + 12 );
                break;
        }

        for ( ControlPoint cp : shape.getControlPoints() ) {
            if ( !cp.getId().equals( this.getID() ) ) {
                cp.moveControlPoint();
            }
        }

    }

    public void nodeDragMove( final WiresRectangle rect,
                              final NodeDragMoveEvent nodeDragMoveEvent ) {

        double deltaX = nodeDragMoveEvent.getX() - getDragEventStartX();
        double deltaY = nodeDragMoveEvent.getY() - getDragEventStartY();
        nodeDragMove( rect,
                      deltaX,
                      deltaY );

    }

    public double getDragEventStartX() {
        return dragEventStartX;
    }

    public double getDragEventStartY() {
        return dragEventStartY;
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
    public String toString() {
        return "RectangleControlPointImpl{" + "id=" + id + ", controlType=" + controlType + "' X=" + getX() + " - Y= " + getY() + "}";
    }

    @Override
    public void setControlPointVisible( final boolean visible ) {
        setVisible( visible );
    }

    @Override
    public void updateShape( final Layer layer,
                             final double x,
                             final double y ) {
        nodeDragMove( shape,
                      x,
                      y );
        for ( Magnet m : shape.getMagnets() ) {
            m.placeMagnetPoints();
        }
        layer.draw();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + ( this.id != null ? this.id.hashCode() : 0 );
        hash = 29 * hash + ( this.shape != null ? this.shape.hashCode() : 0 );
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
        final RectangleControlPointImpl other = (RectangleControlPointImpl) obj;
        if ( ( this.id == null ) ? ( other.id != null ) : !this.id.equals( other.id ) ) {
            return false;
        }
        if ( this.shape != other.shape && ( this.shape == null || !this.shape.equals( other.shape ) ) ) {
            return false;
        }
        return true;
    }

    @Override
    public double getControlPointX() {
        return getX();
    }

    @Override
    public double getControlPointY() {
        return getY();
    }

}
