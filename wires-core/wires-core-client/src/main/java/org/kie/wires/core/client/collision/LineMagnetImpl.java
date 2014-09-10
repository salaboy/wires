/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kie.wires.core.client.collision;

import java.util.ArrayList;
import java.util.List;

import com.emitrom.lienzo.client.core.shape.Circle;
import com.emitrom.lienzo.client.core.types.Point2DArray;
import org.kie.wires.core.api.shapes.ControlPoint;
import org.kie.wires.core.api.shapes.Magnet;
import org.kie.wires.core.client.shapes.dynamic.WiresLine;
import org.kie.wires.core.client.util.UUID;

import static org.kie.wires.core.client.util.ShapesUtils.*;

/**
 * @author salaboy
 */
public class LineMagnetImpl extends Circle implements Magnet {

    private final List<ControlPoint> attachedControlPoints = new ArrayList<ControlPoint>();

    private String id;
    private WiresLine shape;

    private int type;

    public LineMagnetImpl( final WiresLine shape,
                           final int type ) {
        this( 6 );
        this.shape = shape;
        this.type = type;
    }

    public LineMagnetImpl( final double radius ) {
        super( radius );
        setFillColor( MAGNET_RGB_FILL_SHAPE );
        this.id = UUID.uuid();
    }

    @Override
    public void placeMagnetPoints() {
        Point2DArray points = shape.getLine().getPoints();

        switch ( type ) {
            case MAGNET_START:
                setX( shape.getX() + points.getPoint( 0 ).getX() );
                setY( shape.getY() + points.getPoint( 0 ).getY() );
                break;
            case MAGNET_END:
                setX( shape.getX() + points.getPoint( 1 ).getX() );
                setY( shape.getY() + points.getPoint( 1 ).getY() );
                break;
        }
    }

    @Override
    public void setMagnetActive( final boolean active ) {
        if ( active ) {
            setScale( 2 );
            setAlpha( 0.5 );
        } else {
            setScale( 1 );
            setAlpha( 1 );
        }
    }

    @Override
    public void attachControlPoint( final ControlPoint controlPoint ) {
        attachedControlPoints.add( controlPoint );
    }

    @Override
    public List<ControlPoint> getAttachedControlPoints() {
        return attachedControlPoints;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "LineMagnetImpl{" + "id=" + id + '}';
    }

    @Override
    public WiresLine getShape() {
        return shape;
    }

    @Override
    public int getType() {
        return type;
    }

}
