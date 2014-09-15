package org.kie.wires.core.client.canvas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;

import com.emitrom.lienzo.client.core.shape.GridLayer;
import com.emitrom.lienzo.client.core.shape.IPrimitive;
import com.emitrom.lienzo.client.core.shape.Layer;
import com.emitrom.lienzo.client.core.shape.Line;
import com.emitrom.lienzo.client.widget.LienzoPanel;
import com.emitrom.lienzo.shared.core.types.ColorName;
import com.google.gwt.user.client.ui.Composite;
import org.kie.wires.core.api.collision.CollisionManager;
import org.kie.wires.core.api.collision.RequiresCollisionManager;
import org.kie.wires.core.api.selection.SelectionManager;
import org.kie.wires.core.api.shapes.HasControlPoints;
import org.kie.wires.core.api.shapes.HasMagnets;
import org.kie.wires.core.api.shapes.Magnet;
import org.kie.wires.core.api.shapes.WiresBaseShape;
import org.kie.wires.core.api.shapes.WiresShape;

/**
 * This is the root Canvas provided by Wires
 */
public class Canvas extends Composite implements SelectionManager,
                                                 CollisionManager {

    private LienzoPanel panel;
    private Layer canvasLayer;
    private List<WiresShape> shapesInCanvas = new ArrayList<WiresShape>();
    private WiresBaseShape selectedShape;
    private ProgressBar progressBar;

    @PostConstruct
    public void init() {
        panel = new LienzoPanel( 1000,
                                 1000 );

        initWidget( panel );

        canvasLayer = new Layer();

        Line line1 = new Line( 0,
                               0,
                               0,
                               0 ).setStrokeColor( ColorName.BLUE ).setAlpha( 0.5 ); // primary lines
        Line line2 = new Line( 0,
                               0,
                               0,
                               0 ).setStrokeColor( ColorName.GREEN ).setAlpha( 0.5 ); // secondary dashed-lines
        line2.setDashArray( 2,
                            2 );

        GridLayer gridLayer = new GridLayer( 100,
                                             line1,
                                             25,
                                             line2 );
        panel.setBackgroundLayer( gridLayer );

        panel.getScene().add( canvasLayer );
    }

    public boolean hasProgressBar() {
        return this.progressBar != null;
    }

    public ProgressBar getProgressBar() {
        return this.progressBar;
    }

    public void setProgressBar( final ProgressBar progressBar ) {
        this.progressBar = progressBar;
        canvasLayer.add( progressBar );
        canvasLayer.draw();
    }

    public List<WiresShape> getShapesInCanvas() {
        return Collections.unmodifiableList( this.shapesInCanvas );
    }

    public void addShape( final WiresBaseShape shape ) {
        shape.setSelectionManager( this );
        if ( shape instanceof RequiresCollisionManager ) {
            ( (RequiresCollisionManager) shape ).setCollisionManager( this );
        }
        canvasLayer.add( shape );
        shapesInCanvas.add( shape );
        canvasLayer.draw();
    }

    public void deleteShape( final WiresBaseShape shape ) {
        shape.destroy();
        deselectShape( shape );
        canvasLayer.remove( shape );
        shapesInCanvas.remove( shape );
        canvasLayer.draw();
    }

    public void clear() {
        for ( WiresShape shape : shapesInCanvas ) {
            shape.destroy();
            canvasLayer.remove( (IPrimitive<?>) shape );
        }
        clearSelection();
        shapesInCanvas.clear();
        canvasLayer.draw();
    }

    @Override
    public Magnet getMagnet( final WiresShape activeShape,
                             final double cx,
                             final double cy ) {
        if ( activeShape == null ) {
            return null;
        }

        Magnet selectedMagnet = null;
        for ( WiresShape shape : getShapesInCanvas() ) {
            if ( !shape.getId().equals( activeShape.getId() ) ) {
                if ( shape instanceof HasMagnets ) {
                    final HasMagnets mShape = (HasMagnets) shape;
                    if ( shape.contains( cx,
                                         cy ) ) {
                        mShape.showMagnetsPoints();
                        double finalDistance = Double.MAX_VALUE;
                        final List<Magnet> magnets = mShape.getMagnets();
                        for ( Magnet magnet : magnets ) {
                            magnet.setActive( false );

                            if ( magnet.isEnabled() ) {
                                double deltaX = cx - magnet.getX() - magnet.getOffset().getX();
                                double deltaY = cy - magnet.getY() - magnet.getOffset().getY();
                                double distance = Math.sqrt( Math.pow( deltaX, 2 ) + Math.pow( deltaY, 2 ) );

                                if ( finalDistance > distance ) {
                                    finalDistance = distance;
                                    selectedMagnet = magnet;
                                }
                            }
                        }
                        if ( selectedMagnet != null ) {
                            selectedMagnet.setActive( true );
                        }

                    } else {
                        mShape.hideMagnetPoints();
                    }
                }
            }
        }

        return selectedMagnet;
    }

    @Override
    public void clearSelection() {
        selectedShape = null;
        for ( WiresShape shape : getShapesInCanvas() ) {
            shape.setSelected( false );
            if ( shape instanceof HasControlPoints ) {
                ( (HasControlPoints) shape ).hideControlPoints();
            }
            if ( shape instanceof HasMagnets ) {
                ( (HasMagnets) shape ).hideMagnetPoints();
            }
        }
        canvasLayer.draw();
    }

    @Override
    public void selectShape( final WiresBaseShape shape ) {
        if ( shape == null ) {
            return;
        }
        clearSelection();
        selectedShape = shape;
        selectedShape.setSelected( true );
        if ( shape instanceof HasControlPoints ) {
            ( (HasControlPoints) selectedShape ).showControlPoints();
        }
        canvasLayer.draw();
    }

    @Override
    public void deselectShape( final WiresBaseShape shape ) {
        if ( shape == null ) {
            return;
        }
        selectedShape = null;
        if ( shape instanceof HasControlPoints ) {
            ( (HasControlPoints) shape ).hideControlPoints();
        }
        if ( shape instanceof HasMagnets ) {
            ( (HasMagnets) shape ).hideMagnetPoints();
        }
        canvasLayer.draw();
    }

    @Override
    public boolean isShapeSelected() {
        return selectedShape != null;
    }

    @Override
    public WiresBaseShape getSelectedShape() {
        return selectedShape;
    }

}
