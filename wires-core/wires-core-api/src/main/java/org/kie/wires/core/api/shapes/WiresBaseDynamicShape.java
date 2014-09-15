/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kie.wires.core.api.shapes;

import java.util.ArrayList;
import java.util.List;

import com.emitrom.lienzo.client.core.shape.Layer;
import org.kie.wires.core.api.controlpoints.ControlPoint;
import org.kie.wires.core.api.controlpoints.HasControlPoints;
import org.kie.wires.core.api.magnets.HasMagnets;
import org.kie.wires.core.api.magnets.Magnet;

/**
 * A Shape that can be re-sized and have connectors attached
 */
public abstract class WiresBaseDynamicShape extends WiresBaseShape implements HasMagnets,
                                                                              HasControlPoints {

    protected List<ControlPoint> controlPoints = new ArrayList<ControlPoint>();
    protected List<Magnet> magnets = new ArrayList<Magnet>();

    private boolean showingMagnets = false;
    private boolean showingControlPoints = false;

    @Override
    public void addControlPoint( final ControlPoint cp ) {
        controlPoints.add( cp );
    }

    @Override
    public List<ControlPoint> getControlPoints() {
        return controlPoints;
    }

    @Override
    public void showControlPoints() {
        final Layer layer = getLayer();
        if ( !controlPoints.isEmpty() && !showingControlPoints ) {
            for ( ControlPoint cp : controlPoints ) {
                cp.setOffset( getLocation() );
                layer.add( cp );
            }
            showingControlPoints = true;
            getLayer().draw();
        }
    }

    @Override
    public void hideControlPoints() {
        final Layer layer = getLayer();
        if ( !controlPoints.isEmpty() && showingControlPoints ) {
            for ( ControlPoint cp : controlPoints ) {
                layer.remove( cp );
            }
            showingControlPoints = false;
            getLayer().draw();
        }
    }

    @Override
    public void addMagnet( final Magnet m ) {
        magnets.add( m );
    }

    @Override
    public List<Magnet> getMagnets() {
        return magnets;
    }

    @Override
    public void showMagnetsPoints() {
        final Layer layer = getLayer();
        if ( !magnets.isEmpty() && !showingMagnets ) {
            for ( Magnet m : magnets ) {
                if ( m.isEnabled() ) {
                    m.setOffset( getLocation() );
                    layer.add( m );
                }
            }
            showingMagnets = true;
            getLayer().draw();
        }
    }

    @Override
    public void hideMagnetPoints() {
        final Layer layer = getLayer();
        if ( !magnets.isEmpty() && showingMagnets ) {
            for ( Magnet m : magnets ) {
                if ( m.isEnabled() ) {
                    layer.remove( m );
                }
            }
            showingMagnets = false;
            getLayer().draw();
        }
    }

    @Override
    public void destroy() {
        hideControlPoints();
        hideMagnetPoints();
        super.destroy();
    }

}
