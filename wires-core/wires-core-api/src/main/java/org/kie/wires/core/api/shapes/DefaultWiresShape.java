/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kie.wires.core.api.shapes;

import com.emitrom.lienzo.client.core.event.NodeDragEndEvent;
import com.emitrom.lienzo.client.core.event.NodeDragEndHandler;
import com.emitrom.lienzo.client.core.event.NodeDragMoveEvent;
import com.emitrom.lienzo.client.core.event.NodeDragMoveHandler;
import org.kie.wires.core.api.containers.ContainerManager;
import org.kie.wires.core.api.containers.RequiresContainerManager;
import org.kie.wires.core.api.containers.WiresContainer;

/**
 * A Shape that can be re-sized and have connectors attached. It CAN be added to Containers.
 */
public abstract class DefaultWiresShape extends WiresBaseDynamicShape implements RequiresContainerManager {

    protected ContainerManager containerManager;

    private WiresContainer boundContainer;

    @Override
    public void setContainerManager( final ContainerManager containerManager ) {
        this.containerManager = containerManager;
    }

    @Override
    public void init( double cx,
                      double cy ) {
        super.init( cx,
                    cy );

        addNodeDragMoveHandler( new NodeDragMoveHandler() {

            @Override
            public void onNodeDragMove( final NodeDragMoveEvent nodeDragMoveEvent ) {
                boundContainer = containerManager.getContainer( DefaultWiresShape.this.getX(),
                                                                DefaultWiresShape.this.getY() );
                if ( boundContainer != null ) {
                    boundContainer.detachShape( DefaultWiresShape.this );
                }

                getLayer().draw();
            }
        } );

        addNodeDragEndHandler( new NodeDragEndHandler() {

            @Override
            public void onNodeDragEnd( final NodeDragEndEvent nodeDragEndEvent ) {
                if ( boundContainer != null ) {
                    boundContainer.attachShape( DefaultWiresShape.this );
                    boundContainer.setHover( false );
                }

                getLayer().draw();
            }
        } );
    }

}
