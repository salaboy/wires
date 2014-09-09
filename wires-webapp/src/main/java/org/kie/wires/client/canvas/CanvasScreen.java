package org.kie.wires.client.canvas;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.bayesian.network.client.events.ReadyEvent;
import com.bayesian.network.client.screen.BayesianScreen;
import com.google.gwt.core.client.GWT;
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
import org.uberfire.client.annotations.WorkbenchPartTitle;
import org.uberfire.client.annotations.WorkbenchPartView;
import org.uberfire.client.annotations.WorkbenchScreen;

@SuppressWarnings("unused")
@Dependent
@WorkbenchScreen(identifier = "WiresCanvasScreen")
public class CanvasScreen extends Canvas {

    @Inject
    private Event<ShapeSelectedEvent> shapeSelectedEvent;

    @Inject
    private Event<ReadyShape> readyShape;

    @Inject
    private BayesianScreen bayesianScreen;

    @Inject
    private LayersGroup layerGroup;

    @WorkbenchPartTitle
    @Override
    public String getTitle() {
        return "Canvas";
    }

    @WorkbenchPartView
    public IsWidget getView() {
        return this;
    }

    @Override
    public void onShapeSelected( final WiresBaseGroupShape shape ) {
        super.onShapeSelected( shape );
        shapeSelectedEvent.fire( new ShapeSelectedEvent( shape ) );
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
        this.addShape( wiresShape );

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

    public void progress( @Observes ProgressEvent event ) {
        if ( !hasProgressBar() ) {
//            final ProgressBar progressBar = new ProgressBar( 300, 34, canvasLayer );
//            setProgressBar (progressBar );
//            progressBar.setX( 20 ).setY( 10 );
        }
    }

}
