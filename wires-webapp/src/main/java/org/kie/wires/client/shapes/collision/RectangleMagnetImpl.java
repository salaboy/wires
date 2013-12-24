/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kie.wires.client.shapes.collision;

import com.emitrom.lienzo.client.core.shape.Circle;
import com.emitrom.lienzo.client.core.shape.Layer;
import com.emitrom.lienzo.client.core.shape.Rectangle;
import com.emitrom.lienzo.shared.core.types.ColorName;
import java.util.ArrayList;
import java.util.List;
import static org.kie.wires.client.shapes.collision.Magnet.*;
import org.kie.wires.client.util.UUID;

/**
 *
 * @author salaboy
 */
public class RectangleMagnetImpl extends Circle implements Magnet {

    private final List<ControlPoint> attachedControlPoints = new ArrayList<ControlPoint>();
    
    private String id;
    
    private Rectangle shape;
    
    public RectangleMagnetImpl(Rectangle shape) {
        this(5);
        this.shape = shape;
    }

    public RectangleMagnetImpl(double radius) {
        super(radius);
        setFillColor(ColorName.YELLOW);
        this.id = UUID.uuid();
    }

    public void placeMagnetPoints(Layer layer, int control) {
        switch (control) {
            case MAGNET_TOP:
                setX(shape.getX() + (shape.getWidth() / 2));
                setY(shape.getY());
                break;
            case MAGNET_BOTTOM:
                setX(shape.getX() + (shape.getWidth() / 2));
                setY(shape.getY() + shape.getHeight());
                break;
            case MAGNET_RIGHT:
                setX(shape.getX() + shape.getWidth());
                setY(shape.getY() + (shape.getHeight() / 2));
                break;
            case MAGNET_LEFT:
                setX(shape.getX());
                setY(shape.getY() + (shape.getHeight() / 2));
                break;
        }
        layer.add(this);
    }
    
     public void setMagnetActive(boolean active) {
        if (active) {
            setScale(2);
            setAlpha(0.3);
        } else {
            setScale(1);
            setAlpha(1);
        }
    }

    public void attachControlPoint(ControlPoint controlPoint) {
        attachedControlPoints.add(controlPoint);
    }

    public List<ControlPoint> getAttachedControlPoints() {
        return attachedControlPoints;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "RectangleMagnetImpl{" + "id=" + id + '}';
    }

    public void setMagnetVisible(boolean visible) {
        setVisible(visible);
    }
    
    
    

}
