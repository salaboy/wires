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
import com.emitrom.lienzo.client.core.shape.Arrow;
import com.emitrom.lienzo.client.core.types.Point2D;
import com.emitrom.lienzo.shared.core.types.ArrowType;
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

public class WiresArrow extends WiresBaseDynamicShape implements RequiresCollisionManager {

    private static final int BOUNDARY_SIZE = 10;

    private static final int BASE_WIDTH = 10;
    private static final int HEAD_WIDTH = 20;
    private static final int ARROW_ANGLE = 45;
    private static final int BASE_ANGLE = 30;

    private final Arrow arrow;
    private final Arrow bounding;

    private final Magnet magnet1;
    private final Magnet magnet2;

    private final ControlPoint controlPoint1;
    private final ControlPoint controlPoint2;

    public WiresArrow( final double x1,
                       final double y1,
                       final double x2,
                       final double y2 ) {
        id = UUID.uuid();
        arrow = new Arrow( new Point2D( x1,
                                        y1 ),
                           new Point2D( x2,
                                        y2 ),
                           BASE_WIDTH,
                           HEAD_WIDTH,
                           ARROW_ANGLE,
                           BASE_ANGLE,
                           ArrowType.AT_END );

        bounding = new Arrow( new Point2D( x1,
                                           y1 ),
                              new Point2D( x2,
                                           y2 ),
                              BASE_WIDTH,
                              HEAD_WIDTH,
                              ARROW_ANGLE,
                              BASE_ANGLE,
                              ArrowType.AT_END );
        bounding.setStrokeWidth( BOUNDARY_SIZE );
        bounding.setAlpha( 0.1 );

        add( arrow );
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
                                                             arrow.setStart( new Point2D( x,
                                                                                          y ) );
                                                             bounding.setStart( new Point2D( x,
                                                                                             y ) );
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
                                                             arrow.setEnd( new Point2D( x,
                                                                                        y ) );
                                                             bounding.setEnd( new Point2D( x,
                                                                                           y ) );
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
                selectionManager.selectShape( WiresArrow.this );
            }
        } );

        addNodeDragStartHandler( new NodeDragStartHandler() {
            @Override
            public void onNodeDragStart( final NodeDragStartEvent nodeDragStartEvent ) {
                selectionManager.deselectShape( WiresArrow.this );
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
        return Math.sqrt( Geometry.ptSegDistSq( arrow.getPoints().getPoint( 0 ).getX(),
                                                arrow.getPoints().getPoint( 0 ).getY(),
                                                arrow.getPoints().getPoint( 1 ).getX(),
                                                arrow.getPoints().getPoint( 1 ).getY(),
                                                _x,
                                                _y ) ) < BOUNDARY_SIZE;
    }

    @Override
    public String toString() {
        return "WiresArrow{" + "id=" + getId() + ",x = " + getX() + ", y = " + getY() + "}";
    }
}
