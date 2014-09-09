/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kie.wires.core.client.collision;

import java.util.ArrayList;
import java.util.List;

import com.emitrom.lienzo.client.core.shape.Circle;
import org.kie.wires.core.api.collision.ControlPoint;
import org.kie.wires.core.api.collision.Magnet;
import org.kie.wires.core.api.shapes.WiresBaseGroupShape;
import org.kie.wires.core.client.shapes.WiresRectangle;
import org.kie.wires.core.client.util.ShapesUtils;
import org.kie.wires.core.client.util.UUID;

/**
 * @author salaboy
 */
public class RectangleMagnetImpl extends Circle implements Magnet {

    private final List<ControlPoint> attachedControlPoints = new ArrayList<ControlPoint>();

    private String id;

    private WiresRectangle shape;

    private int type;

    public RectangleMagnetImpl( final WiresRectangle shape,
                                final int type ) {
        super( 10 );
        this.id = UUID.uuid();
        this.shape = shape;
        this.type = type;
        setFillColor( ShapesUtils.MAGNET_RGB_FILL_SHAPE );
    }

    @Override
    public void placeMagnetPoints() {
        switch ( type ) {
            case MAGNET_TOP:
                setX( shape.getX() + ( shape.getBounding().getWidth() / 2 ) );
                setY( shape.getY() );
                break;
            case MAGNET_BOTTOM:
                setX( shape.getX() + ( shape.getBounding().getWidth() / 2 ) );
                setY( shape.getY() + shape.getBounding().getHeight() );
                break;
            case MAGNET_RIGHT:
                setX( shape.getX() + shape.getBounding().getWidth() );
                setY( shape.getY() + ( shape.getBounding().getHeight() / 2 ) );
                break;
            case MAGNET_LEFT:
                setX( shape.getX() );
                setY( shape.getY() + ( shape.getBounding().getHeight() / 2 ) );
                break;
        }

    }

    @Override
    public void setMagnetActive( final boolean active ) {
        if ( active ) {
            setScale( 2 );
            setAlpha( 0.3 );
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
        return "RectangleMagnetImpl{" + "id=" + id + ", shape=" + shape + ", type=" + type + " x = " + getX() + " , y = " + getY() + "}";
    }

    @Override
    public void setMagnetVisible( final boolean visible ) {
        setVisible( visible );
    }

    @Override
    public WiresBaseGroupShape getShape() {
        return shape;
    }

    @Override
    public int getType() {
        return type;
    }

}
