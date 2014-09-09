package org.kie.wires.core.client.shapes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
import org.kie.wires.core.api.collision.CollidableShape;
import org.kie.wires.core.api.collision.ControlPoint;
import org.kie.wires.core.api.collision.Magnet;
import org.kie.wires.core.api.collision.Projection;
import org.kie.wires.core.api.collision.Vector;
import org.kie.wires.core.api.shapes.WiresBaseGroupShape;
import org.kie.wires.core.client.collision.RectangleControlPointImpl;
import org.kie.wires.core.client.collision.RectangleMagnetImpl;
import org.kie.wires.core.client.util.UUID;

public class WiresRectangle extends WiresBaseGroupShape {

    private Rectangle rectangle;
    private Rectangle bounding;

    private double startX;
    private double startY;
    private double startWidth;
    private double startHeight;

    public WiresRectangle( final double width,
                           final double height ) {
        this( width,
              height,
              3 );
    }

    public WiresRectangle( final double width,
                           final double height,
                           final double cornerRadius ) {
        id = UUID.uuid();
        rectangle = new Rectangle( width,
                                   height,
                                   cornerRadius );
        bounding = new Rectangle( width + 12,
                                  height + 12,
                                  cornerRadius );
        bounding.setAlpha( 0.1 );
        add( rectangle );
        add( bounding );

        magnets.clear();
        addMagnet( new RectangleMagnetImpl( this,
                                            Magnet.MAGNET_TOP ) );
        addMagnet( new RectangleMagnetImpl( this,
                                            Magnet.MAGNET_RIGHT ) );
        addMagnet( new RectangleMagnetImpl( this,
                                            Magnet.MAGNET_BOTTOM ) );
        addMagnet( new RectangleMagnetImpl( this,
                                            Magnet.MAGNET_LEFT ) );

        controlPoints.clear();
        addControlPoint( new RectangleControlPointImpl( this,
                                                        ControlPoint.CONTROL_TOP_LEFT ) );
        addControlPoint( new RectangleControlPointImpl( this,
                                                        ControlPoint.CONTROL_TOP_RIGHT ) );
        addControlPoint( new RectangleControlPointImpl( this,
                                                        ControlPoint.CONTROL_BOTTOM_LEFT ) );
        addControlPoint( new RectangleControlPointImpl( this,
                                                        ControlPoint.CONTROL_BOTTOM_RIGHT ) );
    }

    public void init( final double x,
                      final double y ) {
        setX( x );
        setY( y );

        currentDragX = x;
        currentDragY = y;
        rectangle.setX( 6 );
        rectangle.setY( 6 );

        addNodeMouseClickHandler( new NodeMouseClickHandler() {
            public void onNodeMouseClick( NodeMouseClickEvent nodeMouseClickEvent ) {
                selectionManager.selectShape( WiresRectangle.this );
            }
        } );

        addNodeDragStartHandler( new NodeDragStartHandler() {
            public void onNodeDragStart( NodeDragStartEvent nodeDragStartEvent ) {
                selectionManager.deselectShape( WiresRectangle.this );
            }
        } );

        addNodeDragMoveHandler( new NodeDragMoveHandler() {
            public void onNodeDragMove( NodeDragMoveEvent nodeDragMoveEvent ) {
                beingDragged = true;
                currentDragX = nodeDragMoveEvent.getDragContext().getNode().getX() + nodeDragMoveEvent.getDragContext().getLocalAdjusted().getX();
                currentDragY = nodeDragMoveEvent.getDragContext().getNode().getY() + nodeDragMoveEvent.getDragContext().getLocalAdjusted().getY();
                Layer layer = getLayer();
                for ( Magnet m : magnets ) {
                    if ( !m.getAttachedControlPoints().isEmpty() ) {
                        List<ControlPoint> removeCp = new ArrayList<ControlPoint>();
                        for ( ControlPoint cp : m.getAttachedControlPoints() ) {
                            if ( cp.isAttached() && cp.getShape() != null ) {
                                cp.setControlPointVisible( true );
                                // All the coordinate for the control points should be relative and autocalculated
                                // TODO: refactor this
                                switch ( m.getType() ) {
                                    case Magnet.MAGNET_TOP:
                                        cp.updateShape( layer,
                                                        getX() + ( bounding.getWidth() / 2 ),
                                                        getY() );
                                        break;
                                    case Magnet.MAGNET_LEFT:
                                        cp.updateShape( layer,
                                                        getX(),
                                                        getY() + ( bounding.getHeight() / 2 ) );
                                        break;

                                    case Magnet.MAGNET_RIGHT:
                                        cp.updateShape( layer,
                                                        getX() + bounding.getWidth(),
                                                        getY() + ( bounding.getHeight() / 2 ) );
                                        break;

                                    case Magnet.MAGNET_BOTTOM:
                                        cp.updateShape( layer,
                                                        getX() + ( bounding.getWidth() / 2 ),
                                                        getY() + bounding.getHeight() );
                                        break;
                                }

                            } else {
                                GWT.log( "Removing non attached control point : " + cp + " from magnet" + m );
                                removeCp.add( cp );
                            }

                        }
                        for ( ControlPoint cp : removeCp ) {
                            m.getAttachedControlPoints().remove( cp );
                        }
                    }

                }
                layer.draw();
            }
        } );

        addNodeDragEndHandler( new NodeDragEndHandler() {
            public void onNodeDragEnd( NodeDragEndEvent event ) {
                beingDragged = false;
            }
        } );
    }

    public double getStartX() {
        return startX;
    }

    public void setStartX( final double startX ) {
        this.startX = startX;
    }

    public double getStartY() {
        return startY;
    }

    public void setStartY( final double startY ) {
        this.startY = startY;
    }

    public double getStartWidth() {
        return startWidth;
    }

    public void setStartWidth( final double startWidth ) {
        this.startWidth = startWidth;
    }

    public double getStartHeight() {
        return startHeight;
    }

    public void setStartHeight( final double startHeight ) {
        this.startHeight = startHeight;
    }

    public List<Vector> getAxes() {
        Vector v1 = new Vector();
        Vector v2 = new Vector();
        List<Vector> axes = new ArrayList<Vector>();

        // THIS IS HARDCODED HERE BUT IT CAN BE A LOOP FOR POLYGONS
        // top - left
        v1.setX( getX() );
        v1.setY( getY() );

        // top - right
        v2.setX( getX() + bounding.getWidth() );
        v2.setY( getY() );

        axes.add( v1.edge( v2 ).normal() );

        v1 = new Vector();
        v2 = new Vector();
        // top - right 
        v1.setX( getX() + bounding.getWidth() );
        v1.setY( getY() );

        // bottom - right
        v2.setX( getX() + bounding.getWidth() );
        v2.setY( getY() + bounding.getHeight() );

        axes.add( v1.edge( v2 ).normal() );

        v1 = new Vector();
        v2 = new Vector();
        // bottom - right
        v1.setX( getX() + bounding.getWidth() );
        v1.setY( getY() + bounding.getHeight() );

        // bottom - left
        v2.setX( getX() );
        v2.setY( getY() + bounding.getHeight() );

        axes.add( v1.edge( v2 ).normal() );

        v1 = new Vector();
        v2 = new Vector();
        // bottom - left
        v1.setX( getX() );
        v1.setY( getY() + bounding.getHeight() );

        // top - left
        v2.setX( getX() );
        v2.setY( getY() );

        axes.add( v1.edge( v2 ).normal() );

        return axes;
    }

    public Projection project( final Vector axis ) {
        List<Double> scalars = new ArrayList<Double>();
        Vector v1 = new Vector();

        // top - left
        v1.setX( getX() );
        v1.setY( getY() );

        scalars.add( v1.dotProduct( axis ) );

        v1 = new Vector();
        // top - right
        v1.setX( getX() + bounding.getWidth() );
        v1.setY( getY() );

        scalars.add( v1.dotProduct( axis ) );

        v1 = new Vector();
        // bottom - right
        v1.setX( getX() + bounding.getWidth() );
        v1.setY( getY() + bounding.getHeight() );

        scalars.add( v1.dotProduct( axis ) );

        v1 = new Vector();
        // bottom - left
        v1.setX( getX() );
        v1.setY( getY() + bounding.getHeight() );

        scalars.add( v1.dotProduct( axis ) );

        Double min = Collections.min( scalars );
        Double max = Collections.max( scalars );

        return new Projection( min, max );

    }

    public boolean collidesWith( final CollidableShape shape ) {
        List<Vector> axes = getAxes();
        axes.addAll( shape.getAxes() );
        return !separationOnAxes( axes, shape );
    }

    public boolean separationOnAxes( final List<Vector> axes,
                                     final CollidableShape shape ) {
        for ( int i = 0; i < axes.size(); ++i ) {
            Vector axis = axes.get( i );
            Projection projection1 = shape.project( axis );
            Projection projection2 = this.project( axis );

            if ( !projection1.overlaps( projection2 ) ) {
                return true; // there is no need to continue testing
            }
        }
        return false;
    }

    public double getCurrentDragX() {
        return currentDragX;
    }

    public double getCurrentDragY() {
        return currentDragY;
    }

    public void setCurrentDragX( final double currentDragX ) {
        this.currentDragX = currentDragX;
    }

    public void setCurrentDragY( final double currentDragY ) {
        this.currentDragY = currentDragY;
    }

    public void setBeingResized( final boolean beingResized ) {
        this.beingResized = beingResized;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public Rectangle getBounding() {
        return bounding;
    }

    @Override
    public String toString() {
        return "WiresRectangle{" + "id=" + getId() + ",x = " + getX() + ", y = " + getY() + ", bounding width = " + getBounding().getWidth() + ", bounding height = " + getBounding().getHeight() + " beingDragged= " + beingDragged + "}";
    }

}
