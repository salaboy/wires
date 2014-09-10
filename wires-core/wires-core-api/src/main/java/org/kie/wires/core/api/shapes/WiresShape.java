package org.kie.wires.core.api.shapes;

import java.util.List;

import org.kie.wires.core.api.collision.Projection;
import org.kie.wires.core.api.collision.Vector;

public interface WiresShape {

    String getId();

    void init( final double x,
               final double y );

    void destroy();

    boolean collidesWith( final WiresShape shape );

    boolean separationOnAxes( final List<Vector> axes,
                              final WiresShape shape );

    List<Vector> getAxes();

    Projection project( Vector axis );

}
