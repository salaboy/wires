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
package org.kie.wires.core.client.shapes;

import java.util.List;

import com.emitrom.lienzo.client.core.event.NodeDragEndEvent;
import com.emitrom.lienzo.client.core.event.NodeDragEndHandler;
import com.emitrom.lienzo.client.core.event.NodeDragMoveEvent;
import com.emitrom.lienzo.client.core.event.NodeDragMoveHandler;
import com.emitrom.lienzo.client.core.event.NodeDragStartEvent;
import com.emitrom.lienzo.client.core.event.NodeDragStartHandler;
import com.emitrom.lienzo.client.core.event.NodeMouseClickEvent;
import com.emitrom.lienzo.client.core.event.NodeMouseClickHandler;
import com.emitrom.lienzo.client.core.shape.BezierCurve;
import org.kie.wires.core.api.collision.CollidableShape;
import org.kie.wires.core.api.collision.Projection;
import org.kie.wires.core.api.collision.Vector;
import org.kie.wires.core.api.shapes.WiresBaseGroupShape;
import org.kie.wires.core.client.util.UUID;

public class WiresBezierCurve extends WiresBaseGroupShape {

    private BezierCurve curve;
    private BezierCurve bounding;

    public WiresBezierCurve( final double x,
                             final double y,
                             final double controlX1,
                             final double controlY1,
                             final double controlX2,
                             final double controlY2,
                             final double endX,
                             final double endY ) {
        id = UUID.uuid();
        curve = new BezierCurve( x,
                                 y,
                                 controlX1,
                                 controlY1,
                                 controlX2,
                                 controlY2,
                                 endX,
                                 endY );
        curve.setX( x );
        curve.setY( y );

        bounding = new BezierCurve( x,
                                    y,
                                    controlX1,
                                    controlY1,
                                    controlX2,
                                    controlY2,
                                    endX,
                                    endY );
        bounding.setAlpha( 0.1 );

        add( curve );
        add( bounding );

        magnets.clear();
        controlPoints.clear();
    }

    @Override
    public void init( final double x,
                      final double y ) {
        setX( x );
        setY( y );

        addNodeMouseClickHandler( new NodeMouseClickHandler() {
            public void onNodeMouseClick( final NodeMouseClickEvent nodeMouseClickEvent ) {
                selectionManager.selectShape( WiresBezierCurve.this );
            }
        } );

        addNodeDragStartHandler( new NodeDragStartHandler() {
            public void onNodeDragStart( NodeDragStartEvent nodeDragStartEvent ) {
                selectionManager.deselectShape( WiresBezierCurve.this );
            }
        } );

        addNodeDragMoveHandler( new NodeDragMoveHandler() {
            public void onNodeDragMove( NodeDragMoveEvent nodeDragMoveEvent ) {
                beingDragged = true;
                currentDragX = nodeDragMoveEvent.getDragContext().getNode().getX() + nodeDragMoveEvent.getDragContext().getLocalAdjusted().getX();
                currentDragY = nodeDragMoveEvent.getDragContext().getNode().getY() + nodeDragMoveEvent.getDragContext().getLocalAdjusted().getY();
            }
        } );

        addNodeDragEndHandler( new NodeDragEndHandler() {
            public void onNodeDragEnd( NodeDragEndEvent event ) {
                beingDragged = false;
            }
        } );
    }

    @Override
    public boolean collidesWith( final CollidableShape shape ) {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    @Override
    public boolean separationOnAxes( final List<Vector> axes,
                                     final CollidableShape shape ) {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    @Override
    public List<Vector> getAxes() {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    @Override
    public Projection project( final Vector axis ) {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

}
