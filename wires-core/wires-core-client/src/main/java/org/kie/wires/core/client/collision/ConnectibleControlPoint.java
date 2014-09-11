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
package org.kie.wires.core.client.collision;

import com.emitrom.lienzo.client.core.event.NodeDragEndEvent;
import com.emitrom.lienzo.client.core.event.NodeDragEndHandler;
import com.emitrom.lienzo.client.core.event.NodeDragMoveEvent;
import com.emitrom.lienzo.client.core.event.NodeDragMoveHandler;
import org.kie.wires.core.api.collision.CollisionManager;
import org.kie.wires.core.api.collision.RequiresCollisionManager;
import org.kie.wires.core.api.shapes.ControlPointMoveHandler;
import org.kie.wires.core.api.shapes.HasControlPoints;
import org.kie.wires.core.api.shapes.Magnet;

/**
 * A Control Point that can be connected to Magnets
 */
public class ConnectibleControlPoint extends DefaultControlPoint implements RequiresCollisionManager {

    private Magnet selectedMagnet;
    private HasControlPoints shape;
    private CollisionManager collisionManager;

    public ConnectibleControlPoint( final double x,
                                    final double y,
                                    final HasControlPoints shape,
                                    final ControlPointMoveHandler handler ) {
        super( x,
               y,
               handler );
        this.shape = shape;
    }

    @Override
    public void setCollisionManager( final CollisionManager collisionManager ) {
        this.collisionManager = collisionManager;
    }

    @Override
    protected void setupHandlers( final ControlPointMoveHandler handler ) {
        addNodeDragMoveHandler( new NodeDragMoveHandler() {

            @Override
            public void onNodeDragMove( final NodeDragMoveEvent nodeDragMoveEvent ) {
                handler.onMove( ConnectibleControlPoint.this.getX(),
                                ConnectibleControlPoint.this.getY() );

                selectedMagnet = collisionManager.getMagnet( shape,
                                                             ConnectibleControlPoint.this.getX() + getOffset().getX(),
                                                             ConnectibleControlPoint.this.getY() + getOffset().getY() );
                if ( selectedMagnet != null ) {
                    selectedMagnet.detachControlPoint( ConnectibleControlPoint.this );
                }

                getLayer().draw();
            }
        } );

        addNodeDragEndHandler( new NodeDragEndHandler() {

            @Override
            public void onNodeDragEnd( final NodeDragEndEvent nodeDragEndEvent ) {
                if ( selectedMagnet != null ) {
                    double deltaX = ( getX() + getOffset().getX() ) - ( selectedMagnet.getX() + selectedMagnet.getOffset().getX() );
                    double deltaY = ( getY() + getOffset().getY() ) - ( selectedMagnet.getY() + selectedMagnet.getOffset().getY() );
                    double distance = Math.sqrt( Math.pow( deltaX, 2 ) + Math.pow( deltaY, 2 ) );
                    if ( distance < 30 ) {
                        selectedMagnet.attachControlPoint( ConnectibleControlPoint.this );
                        final double x = selectedMagnet.getX() + selectedMagnet.getOffset().getX() - getOffset().getX();
                        final double y = selectedMagnet.getY() + selectedMagnet.getOffset().getY() - getOffset().getY();
                        setX( x );
                        setY( y );
                        handler.onMove( x,
                                        y );
                    }
                }

                getLayer().draw();
            }
        } );
    }

    @Override
    public String toString() {
        return "ConnectibleControlPoint{" + "id=" + getId() + "}";
    }

}
