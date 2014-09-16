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
package org.kie.wires.core.scratchpad.client.canvas;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.IsWidget;
import org.kie.wires.core.api.containers.RequiresContainerManager;
import org.kie.wires.core.api.containers.WiresContainer;
import org.kie.wires.core.api.events.ClearEvent;
import org.kie.wires.core.api.events.ProgressEvent;
import org.kie.wires.core.api.events.ShapeAddedEvent;
import org.kie.wires.core.api.events.ShapeDeletedEvent;
import org.kie.wires.core.api.events.ShapeDragCompleteEvent;
import org.kie.wires.core.api.events.ShapeDragPreviewEvent;
import org.kie.wires.core.api.events.ShapeSelectedEvent;
import org.kie.wires.core.api.factories.ShapeDropContext;
import org.kie.wires.core.api.shapes.WiresBaseShape;
import org.kie.wires.core.client.canvas.Canvas;
import org.kie.wires.core.client.factories.ShapeFactoryCache;
import org.uberfire.client.annotations.WorkbenchMenu;
import org.uberfire.client.annotations.WorkbenchPartTitle;
import org.uberfire.client.annotations.WorkbenchPartView;
import org.uberfire.client.annotations.WorkbenchScreen;
import org.uberfire.mvp.Command;
import org.uberfire.workbench.model.menu.MenuFactory;
import org.uberfire.workbench.model.menu.Menus;

@SuppressWarnings("unused")
@Dependent
@WorkbenchScreen(identifier = "WiresCanvasScreen")
public class CanvasScreen extends Canvas {

    @Inject
    private Event<ClearEvent> clearEvent;

    @Inject
    private Event<ShapeSelectedEvent> shapeSelectedEvent;

    @Inject
    private Event<ShapeAddedEvent> shapeAddedEvent;

    @Inject
    private Event<ShapeDeletedEvent> shapeDeletedEvent;

    @Inject
    private ShapeFactoryCache factoriesCache;

    private Menus menus;

    private ShapeDropContext dropContext = new DefaultShapeDropContext();

    @PostConstruct
    public void setup() {
        this.menus = MenuFactory
                .newTopLevelMenu( "Clear grid" )
                .respondsWith( new Command() {
                    @Override
                    public void execute() {
                        clearEvent.fire( new ClearEvent() );
                        menus.getItems().get( 0 ).setEnabled( false );
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
        //Only Shapes that require a ContainerManager can be dropped into Containers
        if ( !( shapeDragPreviewEvent.getShape() instanceof RequiresContainerManager ) ) {
            dropContext.setContainer( null );
            return;
        }

        //Find a Container to drop the Shape into
        final double cx = getX( shapeDragPreviewEvent.getX() );
        final double cy = getY( shapeDragPreviewEvent.getY() );
        final WiresContainer container = getContainer( cx,
                                                       cy );
        dropContext.setContainer( container );
        canvasLayer.draw();
    }

    public void onDragCompleteHandler( @Observes ShapeDragCompleteEvent shapeDragCompleteEvent ) {
        final WiresBaseShape wiresShape = shapeDragCompleteEvent.getShape();

        //If there's no Shape to add then exit
        if ( wiresShape == null ) {
            return;
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

        //Add Shape to Canvas
        wiresShape.init( cx,
                         cy );

        //If we're adding the Shape to a Container notify the Container of a new child. We cannot add Shape
        //to the Container's underlying (impl) Group (which would be ideal) as we can no longer select the
        //child separately from the Group. We therefore just keep a reference to children in the Container
        //and move them when we move the Container
        final WiresContainer container = dropContext.getContainer();
        if ( container != null ) {
            container.attachShape( wiresShape );
            container.setSelected( false );
        }

        addShape( wiresShape );

        //Enable clearing of Canvas now a Shape has been added
        menus.getItems().get( 0 ).setEnabled( true );

        //Notify other Panels of a Shape being added
        shapeAddedEvent.fire( new ShapeAddedEvent( wiresShape ) );
    }

    private double getX( double xShapeEvent ) {
        double x = ( ( xShapeEvent - getAbsoluteLeft() ) < 0 ) ? 0 : xShapeEvent - getAbsoluteLeft();
        return x;
    }

    private double getY( double yShapeEvent ) {
        double y = ( ( yShapeEvent - getAbsoluteTop() < 0 ) ) ? 0 : yShapeEvent - getAbsoluteTop();
        return y;
    }

    public void clearPanel( @Observes ClearEvent event ) {
        clear();
    }

    @Override
    public void clear() {
        if ( Window.confirm( "Are you sure to clean the canvas?" ) ) {
            super.clear();
            menus.getItems().get( 0 ).setEnabled( false );
            menus.getItems().get( 1 ).setEnabled( false );
            menus.getItems().get( 2 ).setEnabled( false );
        }
    }

    @Override
    public void deleteShape( final WiresBaseShape shape ) {
        if ( Window.confirm( "Are you sure to remove the selected shape?" ) ) {
            shapeDeletedEvent.fire( new ShapeDeletedEvent( shape ) );
        }
    }

    public void onShapeDeleted( @Observes ShapeDeletedEvent event ) {
        super.deleteShape( event.getShape() );
        menus.getItems().get( 0 ).setEnabled( getShapesInCanvas().size() > 0 );
        menus.getItems().get( 1 ).setEnabled( isShapeSelected() );
        menus.getItems().get( 2 ).setEnabled( isShapeSelected() );
    }

    public void progress( @Observes ProgressEvent event ) {
        if ( !hasProgressBar() ) {
//            final ProgressBar progressBar = new ProgressBar( 300, 34, canvasLayer );
//            setProgressBar (progressBar );
//            progressBar.setX( 20 ).setY( 10 );
        }
    }

}
