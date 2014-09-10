/*
 * Copyright 2014 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kie.wires.core.client.shapes.dynamic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.emitrom.lienzo.client.core.event.NodeDragMoveEvent;
import com.emitrom.lienzo.client.core.event.NodeDragMoveHandler;
import com.emitrom.lienzo.client.core.event.NodeDragStartEvent;
import com.emitrom.lienzo.client.core.event.NodeDragStartHandler;
import com.emitrom.lienzo.client.core.event.NodeMouseClickEvent;
import com.emitrom.lienzo.client.core.event.NodeMouseClickHandler;
import com.emitrom.lienzo.client.core.shape.Line;
import com.emitrom.lienzo.client.core.types.Point2D;
import com.emitrom.lienzo.client.core.types.Point2DArray;
import com.emitrom.lienzo.shared.core.types.LineCap;
import org.kie.wires.core.api.collision.CollisionManager;
import org.kie.wires.core.api.collision.Projection;
import org.kie.wires.core.api.collision.RequiresCollisionManager;
import org.kie.wires.core.api.collision.Vector;
import org.kie.wires.core.api.shapes.ControlPoint;
import org.kie.wires.core.api.shapes.Magnet;
import org.kie.wires.core.api.shapes.WiresBaseDynamicShape;
import org.kie.wires.core.api.shapes.WiresShape;
import org.kie.wires.core.client.collision.LineControlPointImpl;
import org.kie.wires.core.client.collision.LineMagnetImpl;
import org.kie.wires.core.client.util.UUID;

public class WiresLine extends WiresBaseDynamicShape implements RequiresCollisionManager {

    private Line line;
    private Line bounding;

    private double currentDragX = 0;
    private double currentDragY = 0;

    private LineControlPointImpl controlPointStart = new LineControlPointImpl( this,
                                                                               ControlPoint.CONTROL_START );
    private LineControlPointImpl controlPointEnd = new LineControlPointImpl( this,
                                                                             ControlPoint.CONTROL_END );

    public WiresLine( final double x1,
                      final double y1,
                      final double x2,
                      final double y2 ) {
        id = UUID.uuid();
        line = new Line( x1,
                         y1,
                         x2,
                         y2 );
        line.setLineCap( LineCap.ROUND );
        line.setStrokeWidth( 3 );

        bounding = new Line( x1,
                             y1,
                             x2,
                             y2 );
        bounding.setStrokeWidth( 10 );
        bounding.setAlpha( 0.1 );
        add( line );
        add( bounding );

        magnets.clear();
        addMagnet( new LineMagnetImpl( this,
                                       Magnet.MAGNET_START ) );
        addMagnet( new LineMagnetImpl( this,
                                       Magnet.MAGNET_END ) );

        controlPoints.clear();
        addControlPoint( controlPointStart );
        addControlPoint( controlPointEnd );
    }

    @Override
    public void setCollisionManager( final CollisionManager manager ) {
        controlPointStart.setCollisionManager( manager );
        controlPointEnd.setCollisionManager( manager );
    }

    public Line getLine() {
        return line;
    }

    public Line getBounding() {
        return bounding;
    }

    @Override
    public void init( final double x,
                      final double y ) {
        setX( x );
        setY( y );

        currentDragX = x;
        currentDragY = y;

        addNodeMouseClickHandler( new NodeMouseClickHandler() {
            public void onNodeMouseClick( final NodeMouseClickEvent nodeMouseClickEvent ) {
                selectionManager.selectShape( WiresLine.this );
            }
        } );

        addNodeDragStartHandler( new NodeDragStartHandler() {
            public void onNodeDragStart( NodeDragStartEvent nodeDragStartEvent ) {
                selectionManager.deselectShape( WiresLine.this );
            }
        } );

        addNodeDragMoveHandler( new NodeDragMoveHandler() {
            public void onNodeDragMove( NodeDragMoveEvent nodeDragMoveEvent ) {
                currentDragX = nodeDragMoveEvent.getDragContext().getNode().getX() + nodeDragMoveEvent.getDragContext().getLocalAdjusted().getX();
                currentDragY = nodeDragMoveEvent.getDragContext().getNode().getY() + nodeDragMoveEvent.getDragContext().getLocalAdjusted().getY();
            }
        } );
    }

    @Override
    public boolean collidesWith( final WiresShape shape ) {
        List<Vector> axes = getAxes();
        axes.addAll( shape.getAxes() );
        return !separationOnAxes( axes, shape );
    }

    @Override
    public boolean separationOnAxes( final List<Vector> axes,
                                     final WiresShape shape ) {
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

    @Override
    public List<Vector> getAxes() {
        Vector v1 = new Vector();
        Vector v2 = new Vector();
        List<Vector> axes = new ArrayList<Vector>();

        // THIS IS HARDCODED HERE BUT IT CAN BE A LOOP FOR POLYGONS
        // start
        Point2DArray points = bounding.getPoints();
        Point2D startPoint = points.getPoint( 0 );
        v1.setX( currentDragX + startPoint.getX() );
        v1.setY( currentDragY + startPoint.getY() );

        Point2D endPoint = points.getPoint( 1 );

        // end
        v2.setX( currentDragX + endPoint.getX() );
        v2.setY( currentDragY + endPoint.getY() );

        axes.add( v1.edge( v2 ).normal() );

        return axes;
    }

    @Override
    public Projection project( final Vector axis ) {
        List<Double> scalars = new ArrayList<Double>();
        Vector v1 = new Vector();

        Point2DArray points = bounding.getPoints();
        Point2D startPoint = points.getPoint( 0 );
        // start
        v1.setX( currentDragX + startPoint.getX() );
        v1.setY( currentDragY + startPoint.getY() );

        scalars.add( v1.dotProduct( axis ) );

        Point2D endPoint = points.getPoint( 1 );
        v1 = new Vector();
        // end
        v1.setX( currentDragX + endPoint.getX() );
        v1.setY( currentDragY + endPoint.getY() );

        scalars.add( v1.dotProduct( axis ) );

        Double min = Collections.min( scalars );
        Double max = Collections.max( scalars );

        return new Projection( min, max );
    }

    @Override
    public String toString() {
        return "WiresLine{" + "id=" + getId() + ",x = " + getX() + ", y = " + getY() + "}";
    }
}
