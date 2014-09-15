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
import com.emitrom.lienzo.client.core.shape.Rectangle;
import org.kie.wires.core.api.controlpoints.ControlPoint;
import org.kie.wires.core.api.controlpoints.ControlPointMoveHandler;
import org.kie.wires.core.api.magnets.Magnet;
import org.kie.wires.core.api.shapes.WiresBaseDynamicShape;
import org.kie.wires.core.client.controlpoints.DefaultControlPoint;
import org.kie.wires.core.client.magnets.DefaultMagnet;
import org.kie.wires.core.client.util.UUID;

public class WiresRectangle extends WiresBaseDynamicShape {

    private static final int BOUNDARY_SIZE = 10;

    private final Rectangle rectangle;
    private final Rectangle bounding;

    private final Magnet magnet1;
    private final Magnet magnet2;
    private final Magnet magnet3;
    private final Magnet magnet4;

    private final ControlPoint controlPoint1;
    private final ControlPoint controlPoint2;
    private final ControlPoint controlPoint3;
    private final ControlPoint controlPoint4;

    public WiresRectangle( final double x1,
                           final double y1,
                           final double x2,
                           final double y2 ) {
        this( x1,
              y1,
              x2,
              y2,
              5 );
    }

    public WiresRectangle( final double x1,
                           final double y1,
                           final double x2,
                           final double y2,
                           final double cornerRadius ) {
        final double width = Math.abs( x2 - x1 );
        final double height = Math.abs( y2 - y1 );

        id = UUID.uuid();
        rectangle = new Rectangle( width,
                                   height,
                                   cornerRadius );
        rectangle.setX( x1 );
        rectangle.setY( y1 );

        bounding = new Rectangle( width + BOUNDARY_SIZE,
                                  height + BOUNDARY_SIZE,
                                  cornerRadius );
        bounding.setX( x1 - ( BOUNDARY_SIZE / 2 ) );
        bounding.setY( y1 - ( BOUNDARY_SIZE / 2 ) );
        bounding.setStrokeWidth( BOUNDARY_SIZE );
        bounding.setVisible( false );
        bounding.setAlpha( 0.1 );

        add( rectangle );
        add( bounding );

        magnets.clear();
        magnet1 = new DefaultMagnet( x1,
                                     y1 + ( height / 2 ) );
        magnet2 = new DefaultMagnet( x2,
                                     y1 + ( height / 2 ) );
        magnet3 = new DefaultMagnet( x1 + ( width / 2 ),
                                     y1 );
        magnet4 = new DefaultMagnet( x1 + ( width / 2 ),
                                     y2 );
        addMagnet( magnet1 );
        addMagnet( magnet2 );
        addMagnet( magnet3 );
        addMagnet( magnet4 );

        controlPoints.clear();
        final double px1 = rectangle.getX();
        final double py1 = rectangle.getY();
        controlPoint1 = new DefaultControlPoint( px1,
                                                 py1,
                                                 new ControlPointMoveHandler() {
                                                     @Override
                                                     public void onMove( final double x,
                                                                         final double y ) {
                                                         controlPoint2.setY( controlPoint1.getY() );
                                                         controlPoint3.setX( controlPoint1.getX() );
                                                         rectangle.setX( x );
                                                         rectangle.setY( y );
                                                         rectangle.setWidth( controlPoint2.getX() - controlPoint1.getX() );
                                                         rectangle.setHeight( controlPoint3.getY() - controlPoint1.getY() );
                                                         bounding.setX( x - ( BOUNDARY_SIZE / 2 ) );
                                                         bounding.setY( y - ( BOUNDARY_SIZE / 2 ) );
                                                         bounding.setWidth( rectangle.getWidth() + BOUNDARY_SIZE );
                                                         bounding.setHeight( rectangle.getHeight() + BOUNDARY_SIZE );
                                                         magnet1.setX( x );
                                                         magnet1.setY( y + ( rectangle.getHeight() / 2 ) );
                                                         magnet2.setY( y + ( rectangle.getHeight() / 2 ) );
                                                         magnet3.setX( x + ( rectangle.getWidth() / 2 ) );
                                                         magnet3.setY( y );
                                                         magnet4.setX( x + ( rectangle.getWidth() / 2 ) );
                                                     }
                                                 }
        );

        final double px2 = rectangle.getX() + rectangle.getWidth();
        final double py2 = rectangle.getY();
        controlPoint2 = new DefaultControlPoint( px2,
                                                 py2,
                                                 new ControlPointMoveHandler() {
                                                     @Override
                                                     public void onMove( double x,
                                                                         double y ) {
                                                         controlPoint1.setY( controlPoint2.getY() );
                                                         controlPoint4.setX( controlPoint2.getX() );
                                                         rectangle.setY( y );
                                                         rectangle.setWidth( controlPoint2.getX() - controlPoint1.getX() );
                                                         rectangle.setHeight( controlPoint3.getY() - controlPoint1.getY() );
                                                         bounding.setY( y - ( BOUNDARY_SIZE / 2 ) );
                                                         bounding.setWidth( rectangle.getWidth() + BOUNDARY_SIZE );
                                                         bounding.setHeight( rectangle.getHeight() + BOUNDARY_SIZE );
                                                         magnet1.setY( y + ( rectangle.getHeight() / 2 ) );
                                                         magnet2.setX( x );
                                                         magnet2.setY( y + ( rectangle.getHeight() / 2 ) );
                                                         magnet3.setX( x - ( rectangle.getWidth() / 2 ) );
                                                         magnet3.setY( y );
                                                         magnet4.setX( x - ( rectangle.getWidth() / 2 ) );
                                                     }
                                                 }
        );

        final double px3 = rectangle.getX();
        final double py3 = rectangle.getY() + rectangle.getHeight();
        controlPoint3 = new DefaultControlPoint( px3,
                                                 py3,
                                                 new ControlPointMoveHandler() {
                                                     @Override
                                                     public void onMove( double x,
                                                                         double y ) {
                                                         controlPoint1.setX( controlPoint3.getX() );
                                                         controlPoint4.setY( controlPoint3.getY() );
                                                         rectangle.setX( x );
                                                         rectangle.setWidth( controlPoint2.getX() - controlPoint1.getX() );
                                                         rectangle.setHeight( controlPoint3.getY() - controlPoint1.getY() );
                                                         bounding.setX( x - ( BOUNDARY_SIZE / 2 ) );
                                                         bounding.setWidth( rectangle.getWidth() + BOUNDARY_SIZE );
                                                         bounding.setHeight( rectangle.getHeight() + BOUNDARY_SIZE );
                                                         magnet1.setX( x );
                                                         magnet1.setY( y - ( rectangle.getHeight() / 2 ) );
                                                         magnet2.setY( y - ( rectangle.getHeight() / 2 ) );
                                                         magnet3.setX( x + ( rectangle.getWidth() / 2 ) );
                                                         magnet4.setX( x + ( rectangle.getWidth() / 2 ) );
                                                         magnet4.setY( y );
                                                     }
                                                 }
        );

        final double px4 = rectangle.getX() + rectangle.getWidth();
        final double py4 = rectangle.getY() + rectangle.getHeight();
        controlPoint4 = new DefaultControlPoint( px4,
                                                 py4,
                                                 new ControlPointMoveHandler() {
                                                     @Override
                                                     public void onMove( double x,
                                                                         double y ) {
                                                         controlPoint2.setX( controlPoint4.getX() );
                                                         controlPoint3.setY( controlPoint4.getY() );
                                                         rectangle.setWidth( controlPoint2.getX() - controlPoint1.getX() );
                                                         rectangle.setHeight( controlPoint3.getY() - controlPoint1.getY() );
                                                         bounding.setWidth( rectangle.getWidth() + BOUNDARY_SIZE );
                                                         bounding.setHeight( rectangle.getHeight() + BOUNDARY_SIZE );
                                                         magnet1.setY( y - ( rectangle.getHeight() / 2 ) );
                                                         magnet2.setX( x );
                                                         magnet2.setY( y - ( rectangle.getHeight() / 2 ) );
                                                         magnet3.setX( x - ( rectangle.getWidth() / 2 ) );
                                                         magnet4.setX( x - ( rectangle.getWidth() / 2 ) );
                                                         magnet4.setY( y );
                                                     }
                                                 }
        );
        addControlPoint( controlPoint1 );
        addControlPoint( controlPoint2 );
        addControlPoint( controlPoint3 );
        addControlPoint( controlPoint4 );
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
                selectionManager.selectShape( WiresRectangle.this );
            }
        } );

        addNodeDragMoveHandler( new NodeDragMoveHandler() {
            @Override
            public void onNodeDragMove( final NodeDragMoveEvent nodeDragMoveEvent ) {
                magnet1.setOffset( getLocation() );
                magnet2.setOffset( getLocation() );
                magnet3.setOffset( getLocation() );
                magnet4.setOffset( getLocation() );
                controlPoint1.setOffset( getLocation() );
                controlPoint2.setOffset( getLocation() );
                controlPoint3.setOffset( getLocation() );
                controlPoint4.setOffset( getLocation() );
                getLayer().draw();
            }
        } );
    }

    @Override
    public boolean contains( final double cx,
                             final double cy ) {
        final double _x = cx - getX();
        final double _y = cy - getY();
        if ( _x < rectangle.getX() ) {
            return false;
        } else if ( _x > rectangle.getX() + rectangle.getWidth() ) {
            return false;
        } else if ( _y < rectangle.getY() ) {
            return false;
        } else if ( _y > rectangle.getY() + rectangle.getHeight() ) {
            return false;
        }
        return true;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    @Override
    public String toString() {
        return "WiresRectangle{" + "id=" + getId() + ",x = " + getX() + ", y = " + getY() + "}";
    }

}
