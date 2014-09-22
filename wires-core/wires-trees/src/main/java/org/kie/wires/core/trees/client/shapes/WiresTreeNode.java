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
package org.kie.wires.core.trees.client.shapes;

import java.util.ArrayList;
import java.util.List;

import com.emitrom.lienzo.client.core.event.NodeDragMoveEvent;
import com.emitrom.lienzo.client.core.event.NodeDragMoveHandler;
import com.emitrom.lienzo.client.core.shape.Circle;
import org.kie.wires.core.api.shapes.RequiresShapesManager;
import org.kie.wires.core.api.shapes.ShapesManager;
import org.kie.wires.core.api.shapes.WiresBaseShape;
import org.kie.wires.core.trees.client.canvas.WiresTreeNodeConnector;

public class WiresTreeNode extends WiresBaseShape implements RequiresShapesManager {

    private static final int BOUNDARY_SIZE = 10;

    private final Circle circle;
    private final Circle bounding;

    private WiresTreeNode parent;
    private List<WiresTreeNode> children = new ArrayList<WiresTreeNode>();
    private List<WiresTreeNodeConnector> connectors = new ArrayList<WiresTreeNodeConnector>();

    private ShapesManager shapesManager;

    public WiresTreeNode( final Circle shape ) {
        circle = shape;

        bounding = new Circle( circle.getRadius() + ( BOUNDARY_SIZE / 2 ) );
        bounding.setStrokeWidth( BOUNDARY_SIZE );
        bounding.setAlpha( 0.1 );

        add( circle );
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
    public boolean contains( final double cx,
                             final double cy ) {
        return false;
    }

    @Override
    public void init( final double cx,
                      final double cy ) {
        super.init( cx,
                    cy );

        //Update connectors when this Node moves
        addNodeDragMoveHandler( new NodeDragMoveHandler() {

            @Override
            public void onNodeDragMove( final NodeDragMoveEvent nodeDragMoveEvent ) {
                for ( WiresTreeNodeConnector connector : connectors ) {
                    connector.getPoints().getPoint( 0 ).set( getLocation() );
                }
                getLayer().draw();
            }
        } );
    }

    @Override
    public void destroy() {
        //Remove children
        final List<WiresTreeNode> cloneChildren = new ArrayList<WiresTreeNode>( children );
        for ( WiresTreeNode child : cloneChildren ) {
            shapesManager.forceDeleteShape( child );
        }
        children.clear();

        //Remove connectors to children
        final List<WiresTreeNodeConnector> cloneConnectors = new ArrayList<WiresTreeNodeConnector>( connectors );
        for ( WiresTreeNodeConnector connector : cloneConnectors ) {
            getLayer().remove( connector );
        }
        connectors.clear();

        //Remove from its parent
        final WiresTreeNode parent = getParentNode();
        if ( parent != null ) {
            parent.removeChildNode( this );
        }
        super.destroy();
    }

    public WiresTreeNode getParentNode() {
        return parent;
    }

    public void setParentNode( final WiresTreeNode parent ) {
        this.parent = parent;
    }

    public boolean acceptChildNode( final WiresTreeNode child ) {
        //Accept all types of WiresTreeNode by default
        return true;
    }

    public void addChildNode( final WiresTreeNode child ) {
        final WiresTreeNodeConnector connector = new WiresTreeNodeConnector();
        connector.getPoints().getPoint( 0 ).set( getLocation() );
        connector.getPoints().getPoint( 1 ).set( child.getLocation() );
        getLayer().add( connector );
        connector.moveToBottom();

        children.add( child );
        connectors.add( connector );
        child.setParentNode( this );

        //Update connectors when child Node moves
        child.addNodeDragMoveHandler( new NodeDragMoveHandler() {
            @Override
            public void onNodeDragMove( final NodeDragMoveEvent nodeDragMoveEvent ) {
                connector.getPoints().getPoint( 1 ).set( child.getLocation() );
            }
        } );
    }

    public void removeChildNode( final WiresTreeNode child ) {
        child.setParentNode( null );
        final int index = children.indexOf( child );
        final WiresTreeNodeConnector connector = connectors.get( index );
        children.remove( child );
        connectors.remove( connector );
        getLayer().remove( connector );
    }

}
