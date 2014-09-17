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
package org.kie.wires.core.scratchpad.client.shapes.containers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.emitrom.lienzo.client.core.event.NodeDragMoveEvent;
import com.emitrom.lienzo.client.core.event.NodeDragMoveHandler;
import com.emitrom.lienzo.client.core.event.NodeDragStartEvent;
import com.emitrom.lienzo.client.core.event.NodeDragStartHandler;
import com.emitrom.lienzo.client.core.shape.Circle;
import com.emitrom.lienzo.client.core.types.Point2D;
import org.kie.wires.core.api.containers.WiresContainer;
import org.kie.wires.core.api.controlpoints.ControlPoint;
import org.kie.wires.core.api.controlpoints.ControlPointMoveHandler;
import org.kie.wires.core.api.magnets.Magnet;
import org.kie.wires.core.api.shapes.RequiresShapesManager;
import org.kie.wires.core.api.shapes.ShapesManager;
import org.kie.wires.core.api.shapes.WiresBaseDynamicShape;
import org.kie.wires.core.api.shapes.WiresBaseShape;
import org.kie.wires.core.client.controlpoints.DefaultControlPoint;
import org.kie.wires.core.client.magnets.DefaultMagnet;
import org.kie.wires.core.client.util.ShapesUtils;
import org.kie.wires.core.client.util.UUID;
import org.uberfire.commons.data.Pair;

public class WiresCircularContainer extends WiresBaseDynamicShape implements WiresContainer,
                                                                             RequiresShapesManager {

    private static final int BOUNDARY_SIZE = 10;

    private final Circle circle;
    private final Circle bounding;
    private final String circleStrokeColour;
    private final String circleFillColour;

    private final Magnet magnet1;
    private final Magnet magnet2;
    private final Magnet magnet3;
    private final Magnet magnet4;

    private final ControlPoint controlPoint1;

    private List<WiresBaseShape> children = new ArrayList<WiresBaseShape>();
    private List<Pair<WiresBaseShape, Point2D>> dragStartLocations = new ArrayList<Pair<WiresBaseShape, Point2D>>();

    private ShapesManager shapesManager;

    public WiresCircularContainer( final Circle shape ) {
        this.id = UUID.uuid();
        this.circle = shape;
        this.circleFillColour = shape.getFillColor();
        this.circleStrokeColour = shape.getStrokeColor();

        final double radius = circle.getRadius();
        bounding = new Circle( radius + ( BOUNDARY_SIZE / 2 ) );
        bounding.setStrokeWidth( BOUNDARY_SIZE );
        bounding.setAlpha( 0.1 );

        add( circle );

        magnets.clear();
        magnet1 = new DefaultMagnet( 0 - radius,
                                     0 );
        magnet2 = new DefaultMagnet( radius,
                                     0 );
        magnet3 = new DefaultMagnet( 0,
                                     0 - radius );
        magnet4 = new DefaultMagnet( 0,
                                     radius );
        addMagnet( magnet1 );
        addMagnet( magnet2 );
        addMagnet( magnet3 );
        addMagnet( magnet4 );

        controlPoints.clear();
        controlPoint1 = new DefaultControlPoint( radius,
                                                 0,
                                                 new ControlPointMoveHandler() {
                                                     @Override
                                                     public void onMove( final double x,
                                                                         final double y ) {
                                                         final double r = Math.sqrt( Math.pow( x, 2 ) + Math.pow( y, 2 ) );
                                                         magnet1.setX( 0 - r );
                                                         magnet2.setX( r );
                                                         magnet3.setY( 0 - r );
                                                         magnet4.setY( r );
                                                         circle.setRadius( r );
                                                         bounding.setRadius( r + ( BOUNDARY_SIZE / 2 ) );
                                                     }
                                                 }
        );
        addControlPoint( controlPoint1 );
    }

    @Override
    public void setShapesManager( final ShapesManager shapesManager ) {
        this.shapesManager = shapesManager;
    }

    @Override
    public void setSelected( final boolean isSelected ) {
        if ( isSelected ) {
            add( bounding );
        } else {
            remove( bounding );
        }
    }

    @Override
    public void init( final double cx,
                      final double cy ) {
        super.init( cx,
                    cy );

        addNodeDragStartHandler( new NodeDragStartHandler() {
            @Override
            public void onNodeDragStart( final NodeDragStartEvent nodeDragStartEvent ) {
                dragStartLocations.clear();
                for ( WiresBaseShape shape : children ) {
                    dragStartLocations.add( new Pair( shape,
                                                      new Point2D( shape.getLocation().getX(),
                                                                   shape.getLocation().getY() ) ) );
                }
            }
        } );

        addNodeDragMoveHandler( new NodeDragMoveHandler() {
            @Override
            public void onNodeDragMove( final NodeDragMoveEvent nodeDragMoveEvent ) {
                final double deltaX = nodeDragMoveEvent.getDragContext().getDx();
                final double deltaY = nodeDragMoveEvent.getDragContext().getDy();
                final Point2D delta = new Point2D( deltaX,
                                                   deltaY );
                for ( Pair<WiresBaseShape, Point2D> dragStartLocation : dragStartLocations ) {
                    dragStartLocation.getK1().setLocation( dragStartLocation.getK2().plus( delta ) );
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
        return Math.sqrt( Math.pow( _x, 2 ) + Math.pow( _y, 2 ) ) < circle.getRadius() + BOUNDARY_SIZE;
    }

    @Override
    public void attachShape( final WiresBaseShape shape ) {
        children.add( shape );
    }

    @Override
    public void detachShape( final WiresBaseShape shape ) {
        children.remove( shape );
    }

    @Override
    public List<WiresBaseShape> getContainedShapes() {
        return Collections.unmodifiableList( children );
    }

    @Override
    public void setHover( final boolean isHover ) {
        if ( isHover ) {
            circle.setFillColor( ShapesUtils.RGB_FILL_HOVER_CONTAINER );
            circle.setStrokeColor( ShapesUtils.RGB_STROKE_HOVER_CONTAINER );
        } else {
            circle.setFillColor( circleFillColour );
            circle.setStrokeColor( circleStrokeColour );
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        for ( WiresBaseShape shape : children ) {
            shapesManager.forceDeleteShape( shape );
        }
    }

    @Override
    public String toString() {
        return "WiresCircularContainer{" + "id=" + getId() + ",x = " + getX() + ", y = " + getY() + "}";
    }

}
