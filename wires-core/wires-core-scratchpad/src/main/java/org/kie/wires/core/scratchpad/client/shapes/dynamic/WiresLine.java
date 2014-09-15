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
import com.emitrom.lienzo.client.core.shape.Line;
import com.emitrom.lienzo.shared.core.types.LineCap;
import org.kie.wires.core.api.magnets.MagnetManager;
import org.kie.wires.core.client.util.GeometryUtil;
import org.kie.wires.core.api.magnets.RequiresMagnetManager;
import org.kie.wires.core.api.controlpoints.ControlPoint;
import org.kie.wires.core.api.controlpoints.ControlPointMoveHandler;
import org.kie.wires.core.api.magnets.Magnet;
import org.kie.wires.core.api.shapes.WiresBaseDynamicShape;
import org.kie.wires.core.client.controlpoints.ConnectibleControlPoint;
import org.kie.wires.core.client.util.UUID;

public class WiresLine extends WiresBaseDynamicShape implements RequiresMagnetManager {

    private static final int BOUNDARY_SIZE = 10;

    //We do not hide the boundary item for Lines as it makes selecting them very difficult
    private static final double ALPHA_DESELECTED = 0.01;
    private static final double ALPHA_SELECTED = 0.1;

    private final Line line;
    private final Line bounding;

    private final ConnectibleControlPoint controlPoint1;
    private final ConnectibleControlPoint controlPoint2;

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
        bounding.setAlpha( ALPHA_DESELECTED );

        add( line );
        add( bounding );

        magnets.clear();

        controlPoints.clear();
        //We use ControlPointAttachedHandlers for Lines as they have both a Magnet and ControlPoint at the same position.
        //When a ControlPoint is attached to a Magnet if we do not disable the Magnet at the same Position as the ControlPoint
        //it is possible to connect another ControlPoint to the Magnet that is in the same position as the ControlPoint
        //already attached to a Magnet that can lead to recursion between ControlPointMoveHandlers and Magnets.
        controlPoint1 = new ConnectibleControlPoint( x1,
                                                     y1,
                                                     this,
                                                     new ControlPointMoveHandler() {
                                                         @Override
                                                         public void onMove( final double x,
                                                                             final double y ) {
                                                             line.getPoints().getPoint( 0 ).setX( x );
                                                             line.getPoints().getPoint( 0 ).setY( y );
                                                             bounding.getPoints().getPoint( 0 ).setX( x );
                                                             bounding.getPoints().getPoint( 0 ).setY( y );
                                                         }
                                                     } );

        controlPoint2 = new ConnectibleControlPoint( x2,
                                                     y2,
                                                     this,
                                                     new ControlPointMoveHandler() {
                                                         @Override
                                                         public void onMove( final double x,
                                                                             final double y ) {
                                                             line.getPoints().getPoint( 1 ).setX( x );
                                                             line.getPoints().getPoint( 1 ).setY( y );
                                                             bounding.getPoints().getPoint( 1 ).setX( x );
                                                             bounding.getPoints().getPoint( 1 ).setY( y );
                                                         }
                                                     } );
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
        bounding.setAlpha( isSelected ? ALPHA_SELECTED : ALPHA_DESELECTED );
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
        return Math.sqrt( GeometryUtil.ptSegDistSq( line.getPoints().getPoint( 0 ).getX(),
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