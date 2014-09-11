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

import com.emitrom.lienzo.client.core.event.NodeDragMoveEvent;
import com.emitrom.lienzo.client.core.event.NodeDragMoveHandler;
import com.emitrom.lienzo.client.core.event.NodeDragStartEvent;
import com.emitrom.lienzo.client.core.event.NodeDragStartHandler;
import com.emitrom.lienzo.client.core.event.NodeMouseClickEvent;
import com.emitrom.lienzo.client.core.event.NodeMouseClickHandler;
import com.emitrom.lienzo.client.core.shape.Line;
import com.emitrom.lienzo.shared.core.types.LineCap;
import org.kie.wires.core.api.collision.CollisionManager;
import org.kie.wires.core.api.collision.Geometry;
import org.kie.wires.core.api.collision.RequiresCollisionManager;
import org.kie.wires.core.api.shapes.ControlPoint;
import org.kie.wires.core.api.shapes.ControlPointMoveHandler;
import org.kie.wires.core.api.shapes.Magnet;
import org.kie.wires.core.api.shapes.WiresBaseDynamicShape;
import org.kie.wires.core.client.collision.ConnectibleControlPoint;
import org.kie.wires.core.client.collision.DefaultMagnet;
import org.kie.wires.core.client.util.UUID;

public class WiresLine extends WiresBaseDynamicShape implements RequiresCollisionManager {

    private static final int BOUNDARY_SIZE = 10;

    private final Line line;
    private final Line bounding;

    private final Magnet magnet1;
    private final Magnet magnet2;

    private final ControlPoint controlPoint1;
    private final ControlPoint controlPoint2;

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
        bounding.setStrokeWidth( BOUNDARY_SIZE );
        bounding.setAlpha( 0.1 );

        add( line );
        add( bounding );

        magnets.clear();
        magnet1 = new DefaultMagnet( x1,
                                     y1 );
        magnet2 = new DefaultMagnet( x2,
                                     y2 );
        addMagnet( magnet1 );
        addMagnet( magnet2 );

        controlPoints.clear();
        controlPoint1 = new ConnectibleControlPoint( x1,
                                                     y1,
                                                     this,
                                                     new ControlPointMoveHandler() {
                                                         @Override
                                                         public void onMove( final double x,
                                                                             final double y ) {
                                                             magnet1.setX( x );
                                                             magnet1.setY( y );
                                                             line.getPoints().getPoint( 0 ).setX( x );
                                                             line.getPoints().getPoint( 0 ).setY( y );
                                                             bounding.getPoints().getPoint( 0 ).setX( x );
                                                             bounding.getPoints().getPoint( 0 ).setY( y );
                                                         }
                                                     }
        );

        controlPoint2 = new ConnectibleControlPoint( x2,
                                                     y2,
                                                     this,
                                                     new ControlPointMoveHandler() {
                                                         @Override
                                                         public void onMove( final double x,
                                                                             final double y ) {
                                                             magnet2.setX( x );
                                                             magnet2.setY( y );
                                                             line.getPoints().getPoint( 1 ).setX( x );
                                                             line.getPoints().getPoint( 1 ).setY( y );
                                                             bounding.getPoints().getPoint( 1 ).setX( x );
                                                             bounding.getPoints().getPoint( 1 ).setY( y );
                                                         }
                                                     }
        );
        addControlPoint( controlPoint1 );
        addControlPoint( controlPoint2 );
    }

    @Override
    public void setCollisionManager( final CollisionManager manager ) {
        for ( ControlPoint cp : getControlPoints() ) {
            if ( cp instanceof RequiresCollisionManager ) {
                ( (RequiresCollisionManager) cp ).setCollisionManager( manager );
            }
        }
    }

    @Override
    public void init( final double cx,
                      final double cy ) {
        setX( cx );
        setY( cy );

        addNodeMouseClickHandler( new NodeMouseClickHandler() {
            @Override
            public void onNodeMouseClick( final NodeMouseClickEvent nodeMouseClickEvent ) {
                selectionManager.selectShape( WiresLine.this );
            }
        } );

        addNodeDragStartHandler( new NodeDragStartHandler() {
            @Override
            public void onNodeDragStart( final NodeDragStartEvent nodeDragStartEvent ) {
                selectionManager.deselectShape( WiresLine.this );
            }
        } );

        addNodeDragMoveHandler( new NodeDragMoveHandler() {
            @Override
            public void onNodeDragMove( final NodeDragMoveEvent nodeDragMoveEvent ) {
                magnet1.setOffset( getLocation() );
                magnet2.setOffset( getLocation() );
                getLayer().draw();
            }
        } );
    }

    @Override
    public boolean contains( final double cx,
                             final double cy ) {
        final double _x = cx - getX();
        final double _y = cy - getY();
        return Math.sqrt( Geometry.ptSegDistSq( line.getPoints().getPoint( 0 ).getX(),
                                                line.getPoints().getPoint( 0 ).getY(),
                                                line.getPoints().getPoint( 1 ).getX(),
                                                line.getPoints().getPoint( 1 ).getY(),
                                                _x,
                                                _y ) ) < BOUNDARY_SIZE;
    }

    @Override
    public String toString() {
        return "WiresLine{" + "id=" + getId() + ",x = " + getX() + ", y = " + getY() + "}";
    }
}
