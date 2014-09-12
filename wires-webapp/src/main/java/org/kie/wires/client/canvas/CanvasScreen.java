package org.kie.wires.client.canvas;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.IsWidget;
import org.kie.wires.core.api.events.ClearEvent;
import org.kie.wires.core.api.events.ProgressEvent;
import org.kie.wires.core.api.events.ShapeAddedEvent;
import org.kie.wires.core.api.events.ShapeDragCompleteEvent;
import org.kie.wires.core.api.events.ShapeSelectedEvent;
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
    private ShapeFactoryCache factoriesCache;

    private Menus menus;

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
                            removeShape( getSelectedShape() );
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

    public void myResponseObserver( @Observes ShapeDragCompleteEvent shapeDragCompleteEvent ) {
        final WiresBaseShape wiresShape = shapeDragCompleteEvent.getShape();

        if ( wiresShape == null ) {
            return;
        }
        if ( shapeDragCompleteEvent.getX() < getAbsoluteLeft() || shapeDragCompleteEvent.getY() < getAbsoluteTop() ) {
            return;
        } else if ( shapeDragCompleteEvent.getX() > getAbsoluteLeft() + getOffsetWidth() || shapeDragCompleteEvent.getY() > getAbsoluteTop() + getOffsetHeight() ) {
            return;
        }

        wiresShape.init( this.getX( shapeDragCompleteEvent.getX() ),
                         this.getY( shapeDragCompleteEvent.getY() ) );
        addShape( wiresShape );
        menus.getItems().get( 0 ).setEnabled( true );

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
    public void removeShape( final WiresBaseShape shape ) {
        if ( Window.confirm( "Are you sure to remove the selected shape?" ) ) {
            super.removeShape( shape );
            menus.getItems().get( 0 ).setEnabled( getShapesInCanvas().size() > 0 );
            menus.getItems().get( 1 ).setEnabled( isShapeSelected() );
            menus.getItems().get( 2 ).setEnabled( isShapeSelected() );
        }
    }

    public void progress( @Observes ProgressEvent event ) {
        if ( !hasProgressBar() ) {
//            final ProgressBar progressBar = new ProgressBar( 300, 34, canvasLayer );
//            setProgressBar (progressBar );
//            progressBar.setX( 20 ).setY( 10 );
        }
    }

}
