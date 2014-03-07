/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kie.wires.client.shapes.collision;

import org.kie.wires.client.shapes.collision.api.ControlPoint;
import org.kie.wires.client.shapes.collision.api.Magnet;
import com.emitrom.lienzo.client.core.shape.Circle;
import java.util.ArrayList;
import java.util.List;
import static org.kie.wires.client.factoryShapes.ShapeFactoryUtil.MAGNET_RGB_FILL_SHAPE;
import org.kie.wires.client.shapes.WiresRectangle;
import static org.kie.wires.client.shapes.collision.api.Magnet.*;
import org.kie.wires.client.util.UUID;

/**
 *
 * @author salaboy
 */
public class RectangleMagnetImpl extends Circle implements Magnet {

    private final List<ControlPoint> attachedControlPoints = new ArrayList<ControlPoint>();
    
    private String id;
    
    private WiresRectangle shape;
    
    private int type;
    
    public RectangleMagnetImpl(WiresRectangle shape, int type) {
        this(6);
        this.shape = shape;
        this.type = type;
    }

    public RectangleMagnetImpl(double radius) {
        super(radius);
        setFillColor(MAGNET_RGB_FILL_SHAPE);
        this.id = UUID.uuid();
    }

    public void placeMagnetPoints() {
        switch (type) {
            case MAGNET_TOP:
                setX(shape.getX() + (shape.getRectangle().getWidth() / 2));
                setY(shape.getY());
                break;
            case MAGNET_BOTTOM:
                setX(shape.getX() + (shape.getRectangle().getWidth() / 2));
                setY(shape.getY() + shape.getRectangle().getHeight());
                break;
            case MAGNET_RIGHT:
                setX(shape.getX() + shape.getRectangle().getWidth());
                setY(shape.getY() + (shape.getRectangle().getHeight() / 2));
                break;
            case MAGNET_LEFT:
                setX(shape.getX());
                setY(shape.getY() + (shape.getRectangle().getHeight() / 2));
                break;
        }
        
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
