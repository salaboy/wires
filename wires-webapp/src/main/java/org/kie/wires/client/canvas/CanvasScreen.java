package org.kie.wires.client.canvas;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.bayesian.network.client.events.ReadyEvent;
import com.bayesian.network.client.screen.BayesianScreen;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.IsWidget;
import org.kie.wires.client.layers.LayersGroup;
import org.kie.wires.core.api.events.ClearEvent;
import org.kie.wires.core.api.events.ProgressEvent;
import org.kie.wires.core.api.events.ReadyShape;
import org.kie.wires.core.api.events.ShapeAddEvent;
import org.kie.wires.core.api.events.ShapeSelectedEvent;
import org.kie.wires.core.api.shapes.WiresBaseGroupShape;
import org.kie.wires.core.client.canvas.Canvas;
import org.kie.wires.core.client.shapes.WiresCircle;
import org.kie.wires.core.client.shapes.WiresLine;
import org.kie.wires.core.client.shapes.WiresRectangle;
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
    private Event<ShapeSelectedEvent> shapeSelectedEvent;

    @Inject
    private Event<ClearEvent> clearEvent;

    @Inject
    private Event<ReadyShape> readyShape;

    @Inject
    private BayesianScreen bayesianScreen;

    @Inject
    private LayersGroup layerGroup;

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
    public void selectShape( final WiresBaseGroupShape shape ) {
        super.selectShape( shape );
        shapeSelectedEvent.fire( new ShapeSelectedEvent( shape ) );
        menus.getItems().get( 1 ).setEnabled( isShapeSelected() );
        menus.getItems().get( 2 ).setEnabled( isShapeSelected() );
    }

    public void myResponseObserver( @Observes ShapeAddEvent shapeAddEvent ) {
        String shape = shapeAddEvent.getShape();

        //TODO This is nasty. We need to create a WiresXXX shape from the event
        WiresBaseGroupShape wiresShape = null;
        if ( shape.equals( "WiresRectangle" ) ) {
            wiresShape = new WiresRectangle( 70,
                                             40 );
        } else if ( shape.equals( "WiresCircle" ) ) {
            wiresShape = new WiresCircle( 0,
                                          0,
                                          30 );
        } else if ( shape.equals( "WiresLine" ) ) {
            wiresShape = new WiresLine( 0,
                                        0,
                                        30,
                                        30 );
        }

        if ( shapeAddEvent.getX() < getAbsoluteLeft() || shapeAddEvent.getY() < getAbsoluteTop() ) {
            return;
        } else if ( shapeAddEvent.getX() > getAbsoluteLeft() + getOffsetWidth() || shapeAddEvent.getY() > getAbsoluteTop() + getOffsetHeight() ) {
            return;
        }

        wiresShape.setDraggable( true );
        wiresShape.init( this.getX( shapeAddEvent.getX() ),
                         this.getY( shapeAddEvent.getY() ) );
        addShape( wiresShape );
        menus.getItems().get( 0 ).setEnabled( true );

        readyShape.fire( new ReadyShape( shape ) );
    }

    private int getX( int xShapeEvent ) {
        int x = ( ( xShapeEvent - getAbsoluteLeft() ) < 1 ) ? 1 : xShapeEvent - getAbsoluteLeft();
        return 25 * Math.abs( x / 25 );
    }

    private int getY( int yShapeEvent ) {
        int y = ( ( yShapeEvent - getAbsoluteTop() < 1 ) ) ? 1 : yShapeEvent - getAbsoluteTop();
        return 25 * Math.abs( y / 25 );
    }

    public void addNodes( @Observes ReadyEvent event ) {
        GWT.log( "---event.getBayesianNodes() " + event.getBayesianNodes().size() );
        addShape( event.getBayesianNodes().get( 0 ) );
    }

    public void clearPanel( @Observes ClearEvent event ) {
        clear();
    }

    @Override
    public void clear() {
        if ( Window.confirm( "Are you sure to clean the canvas?" ) ) {
            super.clear();
        }
    }

    @Override
    public void removeShape( final WiresBaseGroupShape shape ) {
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
