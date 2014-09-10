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
import com.emitrom.lienzo.client.core.shape.Circle;
import org.kie.wires.core.api.collision.CollidableShape;
import org.kie.wires.core.api.collision.Projection;
import org.kie.wires.core.api.collision.Vector;
import org.kie.wires.core.api.shapes.WiresBaseGroupShape;
import org.kie.wires.core.client.util.UUID;

public class WiresCircle extends WiresBaseGroupShape {

    private Circle circle;
    private Circle bounding;

    public WiresCircle( final double x,
                        final double y,
                        final double radius ) {
        id = UUID.uuid();
        circle = new Circle( radius );
        circle.setX( x );
        circle.setY( y );

        bounding = new Circle( radius );
        bounding.setX( x );
        bounding.setY( y );
        bounding.setAlpha( 0.1 );

        add( circle );
        add( bounding );

        magnets.clear();
        controlPoints.clear();
    }

    public void init( final double x,
                      final double y ) {
        setX( x );
        setY( y );

        addNodeMouseClickHandler( new NodeMouseClickHandler() {
            public void onNodeMouseClick( final NodeMouseClickEvent nodeMouseClickEvent ) {
                selectionManager.selectShape( WiresCircle.this );
            }
        } );

        addNodeDragStartHandler( new NodeDragStartHandler() {
            public void onNodeDragStart( NodeDragStartEvent nodeDragStartEvent ) {
                selectionManager.deselectShape( WiresCircle.this );
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
        return false;
    }

    @Override
    public boolean separationOnAxes( final List<Vector> axes,
                                     final CollidableShape shape ) {
        return false;
    }

    @Override
    public List<Vector> getAxes() {
        return Collections.EMPTY_LIST;
    }

    @Override
    public Projection project( final Vector axis ) {
        return new Projection( 0,
                               0 );
    }

}
