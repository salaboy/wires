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
package org.kie.wires.core.scratchpad.client.shapes.dynamic;

import com.emitrom.lienzo.client.core.event.NodeDragMoveEvent;
import com.emitrom.lienzo.client.core.event.NodeDragMoveHandler;
import com.emitrom.lienzo.client.core.event.NodeMouseClickEvent;
import com.emitrom.lienzo.client.core.event.NodeMouseClickHandler;
import com.emitrom.lienzo.client.core.shape.Arrow;
import com.emitrom.lienzo.client.core.types.Point2D;
import com.emitrom.lienzo.shared.core.types.ArrowType;
import org.kie.wires.core.api.magnets.MagnetManager;
import org.kie.wires.core.client.util.GeometryUtil;
import org.kie.wires.core.api.magnets.RequiresMagnetManager;
import org.kie.wires.core.api.controlpoints.ControlPoint;
import org.kie.wires.core.api.controlpoints.ControlPointMoveHandler;
import org.kie.wires.core.api.magnets.Magnet;
import org.kie.wires.core.api.shapes.WiresBaseDynamicShape;
import org.kie.wires.core.client.controlpoints.ConnectibleControlPoint;
import org.kie.wires.core.client.util.UUID;

public class WiresArrow extends WiresBaseDynamicShape implements RequiresMagnetManager {

    private static final int BOUNDARY_SIZE = 10;

    private static final int BASE_WIDTH = 10;
    private static final int HEAD_WIDTH = 20;
    private static final int ARROW_ANGLE = 45;
    private static final int BASE_ANGLE = 30;

    private final Arrow arrow;
    private final Arrow bounding;

    private final ConnectibleControlPoint controlPoint1;
    private final ConnectibleControlPoint controlPoint2;

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
        bounding.setVisible( false );
        bounding.setAlpha( 0.1 );

        add( arrow );
        add( bounding );

        magnets.clear();

        controlPoints.clear();
        controlPoint1 = new ConnectibleControlPoint( x1,
                                                     y1,
                                                     this,
                                                     new ControlPointMoveHandler() {
                                                         @Override
                                                         public void onMove( final double x,
                                                                             final double y ) {
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
    public void setMagnetManager( final MagnetManager manager ) {
        for ( ControlPoint cp : getControlPoints() ) {
            if ( cp instanceof RequiresMagnetManager ) {
                ( (RequiresMagnetManager) cp ).setMagnetManager( manager );
            }
        }
    }

    @Override
    public void setSelected( final boolean isSelected ) {
        bounding.setVisible( isSelected );
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

        addNodeDragMoveHandler( new NodeDragMoveHandler() {
            @Override
            public void onNodeDragMove( final NodeDragMoveEvent nodeDragMoveEvent ) {
                controlPoint1.setOffset( getLocation() );
                controlPoint2.setOffset( getLocation() );
                final Magnet boundMagnet1 = controlPoint1.getBoundMagnet();
                final Magnet boundMagnet2 = controlPoint2.getBoundMagnet();
                if ( boundMagnet1 != null ) {
                    boundMagnet1.detachControlPoint( controlPoint1 );
                }
                if ( boundMagnet2 != null ) {
                    boundMagnet2.detachControlPoint( controlPoint2 );
                }
                getLayer().draw();
            }
        } );
    }

    @Override
    public boolean contains( final double cx,
                             final double cy ) {
        final double _x = cx - getX();
        final double _y = cy - getY();
        return Math.sqrt( GeometryUtil.ptSegDistSq( arrow.getPoints().getPoint( 0 ).getX(),
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
