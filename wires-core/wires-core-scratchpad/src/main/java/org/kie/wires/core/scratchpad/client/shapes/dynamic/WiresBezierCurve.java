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

import com.emitrom.lienzo.client.core.event.NodeMouseClickEvent;
import com.emitrom.lienzo.client.core.event.NodeMouseClickHandler;
import com.emitrom.lienzo.client.core.shape.BezierCurve;
import com.emitrom.lienzo.client.core.shape.Line;
import org.kie.wires.core.api.controlpoints.ControlPoint;
import org.kie.wires.core.api.controlpoints.ControlPointMoveHandler;
import org.kie.wires.core.api.magnets.MagnetManager;
import org.kie.wires.core.api.magnets.RequiresMagnetManager;
import org.kie.wires.core.api.shapes.WiresBaseDynamicShape;
import org.kie.wires.core.client.controlpoints.ConnectibleControlPoint;
import org.kie.wires.core.client.controlpoints.DefaultControlPoint;
import org.kie.wires.core.client.util.UUID;

public class WiresBezierCurve extends WiresBaseDynamicShape implements RequiresMagnetManager {

    private static final int BOUNDARY_SIZE = 10;

    //We do not hide the boundary item for Lines as it makes selecting them very difficult
    private static final double ALPHA_DESELECTED = 0.01;
    private static final double ALPHA_SELECTED = 0.1;

    private final BezierCurve curve;
    private final BezierCurve bounding;
    private final Line controlLine1;
    private final Line controlLine2;

    private final ControlPoint controlPoint1;
    private final ControlPoint controlPoint2;
    private final ControlPoint controlPoint3;
    private final ControlPoint controlPoint4;

    public WiresBezierCurve( final BezierCurve shape ) {
        this( shape,
              shape.getControlPoints().getPoint( 0 ).getX(),
              shape.getControlPoints().getPoint( 0 ).getY(),
              shape.getControlPoints().getPoint( 1 ).getX(),
              shape.getControlPoints().getPoint( 1 ).getY(),
              shape.getControlPoints().getPoint( 2 ).getX(),
              shape.getControlPoints().getPoint( 2 ).getY(),
              shape.getControlPoints().getPoint( 3 ).getX(),
              shape.getControlPoints().getPoint( 3 ).getY() );
    }

    public WiresBezierCurve( final BezierCurve shape,
                             final double x,
                             final double y,
                             final double controlX1,
                             final double controlY1,
                             final double controlX2,
                             final double controlY2,
                             final double endX,
                             final double endY ) {
        id = UUID.uuid();
        curve = shape;

        bounding = new BezierCurve( x,
                                    y,
                                    controlX1,
                                    controlY1,
                                    controlX2,
                                    controlY2,
                                    endX,
                                    endY );
        bounding.setStrokeWidth( BOUNDARY_SIZE );
        bounding.setAlpha( ALPHA_DESELECTED );
        bounding.setAlpha( 0.1 );

        controlLine1 = new Line( x,
                                 y,
                                 controlX1,
                                 controlY1 );
        controlLine1.setAlpha( 0.5 );
        controlLine1.setVisible( false );
        controlLine1.setStrokeColor( "#0000ff" );
        controlLine1.setDashArray( 2, 2 );
        controlLine2 = new Line( controlX2,
                                 controlY2,
                                 endX,
                                 endY );
        controlLine2.setAlpha( 0.5 );
        controlLine2.setVisible( false );
        controlLine2.setStrokeColor( "#0000ff" );
        controlLine2.setDashArray( 2, 2 );

        add( curve );
        add( bounding );
        add( controlLine1 );
        add( controlLine2 );

        magnets.clear();

        controlPoints.clear();
        controlPoint1 = new ConnectibleControlPoint( curve.getControlPoints().getPoint( 0 ).getX(),
                                                     curve.getControlPoints().getPoint( 0 ).getY(),
                                                     this,
                                                     new ControlPointMoveHandler() {
                                                         @Override
                                                         public void onMove( final double x,
                                                                             final double y ) {
                                                             curve.getControlPoints().getPoint( 0 ).setX( x );
                                                             curve.getControlPoints().getPoint( 0 ).setY( y );
                                                             bounding.getControlPoints().getPoint( 0 ).setX( x );
                                                             bounding.getControlPoints().getPoint( 0 ).setY( y );
                                                             controlLine1.getPoints().getPoint( 0 ).setX( x );
                                                             controlLine1.getPoints().getPoint( 0 ).setY( y );
                                                         }
                                                     }
        );

        controlPoint2 = new DefaultControlPoint( curve.getControlPoints().getPoint( 1 ).getX(),
                                                 curve.getControlPoints().getPoint( 1 ).getY(),
                                                 new ControlPointMoveHandler() {
                                                     @Override
                                                     public void onMove( final double x,
                                                                         final double y ) {
                                                         curve.getControlPoints().getPoint( 1 ).setX( x );
                                                         curve.getControlPoints().getPoint( 1 ).setY( y );
                                                         bounding.getControlPoints().getPoint( 1 ).setX( x );
                                                         bounding.getControlPoints().getPoint( 1 ).setY( y );
                                                         controlLine1.getPoints().getPoint( 1 ).setX( x );
                                                         controlLine1.getPoints().getPoint( 1 ).setY( y );
                                                     }
                                                 }
        );

        controlPoint3 = new DefaultControlPoint( curve.getControlPoints().getPoint( 2 ).getX(),
                                                 curve.getControlPoints().getPoint( 2 ).getY(),
                                                 new ControlPointMoveHandler() {
                                                     @Override
                                                     public void onMove( final double x,
                                                                         final double y ) {
                                                         curve.getControlPoints().getPoint( 2 ).setX( x );
                                                         curve.getControlPoints().getPoint( 2 ).setY( y );
                                                         bounding.getControlPoints().getPoint( 2 ).setX( x );
                                                         bounding.getControlPoints().getPoint( 2 ).setY( y );
                                                         controlLine2.getPoints().getPoint( 0 ).setX( x );
                                                         controlLine2.getPoints().getPoint( 0 ).setY( y );
                                                     }
                                                 }
        );

        controlPoint4 = new ConnectibleControlPoint( curve.getControlPoints().getPoint( 3 ).getX(),
                                                     curve.getControlPoints().getPoint( 3 ).getY(),
                                                     this,
                                                     new ControlPointMoveHandler() {
                                                         @Override
                                                         public void onMove( final double x,
                                                                             final double y ) {
                                                             curve.getControlPoints().getPoint( 3 ).setX( x );
                                                             curve.getControlPoints().getPoint( 3 ).setY( y );
                                                             bounding.getControlPoints().getPoint( 3 ).setX( x );
                                                             bounding.getControlPoints().getPoint( 3 ).setY( y );
                                                             controlLine2.getPoints().getPoint( 1 ).setX( x );
                                                             controlLine2.getPoints().getPoint( 1 ).setY( y );
                                                         }
                                                     }
        );

        addControlPoint( controlPoint1 );
        addControlPoint( controlPoint2 );
        addControlPoint( controlPoint3 );
        addControlPoint( controlPoint4 );
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
        controlLine1.setVisible( isSelected );
        controlLine2.setVisible( isSelected );
    }

    @Override
    public void init( final double cx,
                      final double cy ) {
        setX( cx );
        setY( cy );

        addNodeMouseClickHandler( new NodeMouseClickHandler() {
            public void onNodeMouseClick( final NodeMouseClickEvent nodeMouseClickEvent ) {
                selectionManager.selectShape( WiresBezierCurve.this );
            }
        } );
    }

    @Override
    public boolean contains( final double cx,
                             final double cy ) {
        return false;
    }

}
