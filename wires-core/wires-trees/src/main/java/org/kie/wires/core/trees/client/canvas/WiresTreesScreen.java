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
package org.kie.wires.core.trees.client.canvas;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.emitrom.lienzo.client.core.types.Point2D;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.IsWidget;
import org.kie.wires.core.api.events.ClearEvent;
import org.kie.wires.core.api.events.ShapeAddedEvent;
import org.kie.wires.core.api.events.ShapeDeletedEvent;
import org.kie.wires.core.api.events.ShapeDragCompleteEvent;
import org.kie.wires.core.api.events.ShapeDragPreviewEvent;
import org.kie.wires.core.api.events.ShapeSelectedEvent;
import org.kie.wires.core.api.shapes.WiresBaseShape;
import org.kie.wires.core.client.canvas.WiresCanvas;
import org.kie.wires.core.trees.client.shapes.WiresTreeNode;
import org.uberfire.client.annotations.WorkbenchMenu;
import org.uberfire.client.annotations.WorkbenchPartTitle;
import org.uberfire.client.annotations.WorkbenchPartView;
import org.uberfire.client.annotations.WorkbenchScreen;
import org.uberfire.mvp.Command;
import org.uberfire.workbench.model.menu.MenuFactory;
import org.uberfire.workbench.model.menu.Menus;

@Dependent
@WorkbenchScreen(identifier = "WiresTreesScreen")
public class WiresTreesScreen extends WiresCanvas {

    private static final int MAX_PROXIMITY = 200;

    @Inject
    private Event<ClearEvent> clearEvent;

    @Inject
    private Event<ShapeSelectedEvent> shapeSelectedEvent;

    @Inject
    private Event<ShapeAddedEvent> shapeAddedEvent;

    @Inject
    private Event<ShapeDeletedEvent> shapeDeletedEvent;

    private Menus menus;

    private WiresTreeNodeDropContext dropContext = new WiresTreeNodeDropContext();

    private WiresTreeNodeConnector connector = null;

    @PostConstruct
    public void setup() {
        this.menus = MenuFactory
                .newTopLevelMenu( "Clear grid" )
                .respondsWith( new Command() {
                    @Override
                    public void execute() {
                        clear();
                    }
                } )
                .endMenu()
                .newTopLevelMenu( "Delete selected" )
                .respondsWith( new Command() {
                    @Override
                    public void execute() {
                        if ( isShapeSelected() ) {
                            deleteShape( getSelectedShape() );
                        }
                    }
                } )
                .endMenu()
                .newTopLevelMenu( "Clear selection" )
                .respondsWith( new Command() {
                    @Override
                    public void execute() {
                        if ( isShapeSelected() ) {
                            clearSelection();
                            menus.getItems().get( 1 ).setEnabled( false );
                            menus.getItems().get( 2 ).setEnabled( false );
                        }
                    }
                } )
                .endMenu()
                .build();
        menus.getItems().get( 0 ).setEnabled( false );
        menus.getItems().get( 1 ).setEnabled( false );
        menus.getItems().get( 2 ).setEnabled( false );
    }

    @WorkbenchPartTitle
    @Override
    public String getTitle() {
        return "Canvas";
    }

    @WorkbenchPartView
    public IsWidget getView() {
        return this;
    }

    @WorkbenchMenu
    public Menus getMenus() {
        return menus;
    }

    @Override
    public void selectShape( final WiresBaseShape shape ) {
        shapeSelectedEvent.fire( new ShapeSelectedEvent( shape ) );
    }

    public void onShapeSelected( @Observes ShapeSelectedEvent event ) {
        super.selectShape( event.getShape() );
        menus.getItems().get( 1 ).setEnabled( isShapeSelected() );
        menus.getItems().get( 2 ).setEnabled( isShapeSelected() );
    }

    @Override
    public void deselectShape( final WiresBaseShape shape ) {
        super.deselectShape( shape );
        menus.getItems().get( 1 ).setEnabled( isShapeSelected() );
        menus.getItems().get( 2 ).setEnabled( isShapeSelected() );
    }

    public void onDragPreviewHandler( @Observes ShapeDragPreviewEvent shapeDragPreviewEvent ) {
        //We can only connect WiresTreeNodes to each other
        if ( !( shapeDragPreviewEvent.getShape() instanceof WiresTreeNode ) ) {
            dropContext.setContext( null );
            return;
        }

        //Find a Parent Node to attach the Shape to
        final double cx = getX( shapeDragPreviewEvent.getX() );
        final double cy = getY( shapeDragPreviewEvent.getY() );
        final WiresTreeNode child = (WiresTreeNode) shapeDragPreviewEvent.getShape();
        final WiresTreeNode prospectiveParent = getParentNode( child,
                                                               cx,
                                                               cy );

        //If there is a prospective parent show the line between child and parent
        if ( prospectiveParent != null ) {
            if ( connector == null ) {
                connector = new WiresTreeNodeConnector();
                canvasLayer.add( connector );
                connector.moveToBottom();
            }
            connector.getPoints().getPoint( 0 ).set( prospectiveParent.getLocation() );
            connector.getPoints().getPoint( 1 ).set( new Point2D( cx,
                                                                  cy ) );
        } else if ( connector != null ) {
            canvasLayer.remove( connector );
            connector = null;
        }

        dropContext.setContext( prospectiveParent );
        canvasLayer.draw();
    }

    public void onDragCompleteHandler( @Observes ShapeDragCompleteEvent shapeDragCompleteEvent ) {
        final WiresBaseShape wiresShape = shapeDragCompleteEvent.getShape();

        //If there's no Shape to add then exit
        if ( wiresShape == null ) {
            return;
        }

        //Hide the temporary connector
        if ( connector != null ) {
            canvasLayer.remove( connector );
            connector = null;
        }

        //Get Shape's co-ordinates relative to the Canvas
        final double cx = getX( shapeDragCompleteEvent.getX() );
        final double cy = getY( shapeDragCompleteEvent.getY() );

        //If the Shape was dropped outside the bounds of the Canvas then exit
        if ( cx < 0 || cy < 0 ) {
            return;
        }
        if ( cx > getOffsetWidth() || cy > getOffsetHeight() ) {
            return;
        }

        //Add the new Node to it's parent (unless this is the first node)
        final WiresTreeNode parent = dropContext.getContext();
        boolean addShape = getShapesInCanvas().size() == 0 || getShapesInCanvas().size() > 0 && parent != null;
        boolean addChildToParent = parent != null;

        if ( addShape ) {
            wiresShape.init( cx,
                             cy );
            addShape( wiresShape );
        }
        if ( addChildToParent ) {
            parent.addChildNode( (WiresTreeNode) wiresShape );
        }

        //Enable clearing of Canvas now a Shape has been added
        menus.getItems().get( 0 ).setEnabled( true );

        //Notify other Panels of a Shape being added
        shapeAddedEvent.fire( new ShapeAddedEvent( wiresShape ) );
    }

    private double getX( double xShapeEvent ) {
        return xShapeEvent - getAbsoluteLeft();
    }

    private double getY( double yShapeEvent ) {
        return yShapeEvent - getAbsoluteTop();
    }

    @Override
    public void clear() {
        if ( Window.confirm( "Are you sure to clean the canvas?" ) ) {
            super.clear();
            clearEvent.fire( new ClearEvent() );
        }
    }

    @Override
    public void deleteShape( final WiresBaseShape shape ) {
        if ( Window.confirm( "Are you sure to remove the selected shape?" ) ) {
            shapeDeletedEvent.fire( new ShapeDeletedEvent( shape ) );
        }
    }

    @Override
    public void forceDeleteShape( final WiresBaseShape shape ) {
        shapeDeletedEvent.fire( new ShapeDeletedEvent( shape ) );
    }

    public void onShapeDeleted( @Observes ShapeDeletedEvent event ) {
        super.deleteShape( event.getShape() );
        menus.getItems().get( 0 ).setEnabled( getShapesInCanvas().size() > 0 );
        menus.getItems().get( 1 ).setEnabled( isShapeSelected() );
        menus.getItems().get( 2 ).setEnabled( isShapeSelected() );
    }

    protected WiresTreeNode getParentNode( final WiresTreeNode dragShape,
                                           final double cx,
                                           final double cy ) {
        WiresTreeNode prospectiveParent = null;
        double finalDistance = Double.MAX_VALUE;
        for ( WiresBaseShape ws : getShapesInCanvas() ) {
            if ( ws instanceof WiresTreeNode ) {
                final WiresTreeNode node = (WiresTreeNode) ws;
                if ( node.acceptChildNode( dragShape ) ) {
                    double deltaX = cx - node.getX();
                    double deltaY = cy - node.getY();
                    double distance = Math.sqrt( Math.pow( deltaX, 2 ) + Math.pow( deltaY, 2 ) );

                    if ( finalDistance > distance ) {
                        finalDistance = distance;
                        prospectiveParent = node;
                    }
                }
            }
        }

        //If we're too far away from a parent we might as well not have a parent
        if ( finalDistance > MAX_PROXIMITY ) {
            prospectiveParent = null;
        }
        return prospectiveParent;
    }

}