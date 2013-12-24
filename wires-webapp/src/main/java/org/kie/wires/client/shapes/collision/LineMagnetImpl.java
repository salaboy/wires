/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kie.wires.client.shapes.collision;

import com.emitrom.lienzo.client.core.shape.Circle;
import com.emitrom.lienzo.client.core.shape.Layer;
import com.emitrom.lienzo.client.core.shape.Line;
import com.emitrom.lienzo.client.core.types.Point2DArray;
import com.emitrom.lienzo.shared.core.types.ColorName;
import java.util.ArrayList;
import java.util.List;
import org.kie.wires.client.util.UUID;

/**
 *
 * @author salaboy
 */
public class LineMagnetImpl extends Circle implements Magnet {

    private List<ControlPoint> attachedControlPoints = new ArrayList<ControlPoint>();

    private String id;
    private Line shape;
    
    public LineMagnetImpl(Line shape) {
       this(5);
       this.shape = shape;
    }

    public LineMagnetImpl(double radius) {
        super(radius);
        setFillColor(ColorName.YELLOW);
        this.id = UUID.uuid();
    }

    public void placeMagnetPoints( Layer layer, int control) {
        Point2DArray points = shape.getPoints();

        switch (control) {
            case MAGNET_START:
                setX(shape.getX() + points.getPoint(0).getX());
                setY(shape.getY() + points.getPoint(0).getY());
                break;
            case MAGNET_END:
                setX(shape.getX() + points.getPoint(1).getX());
                setY(shape.getY() + points.getPoint(1).getY());
                break;

        }
        layer.add(this);
    }

    public void setMagnetActive(boolean active) {
        if (active) {
            setScale(2);
            setAlpha(0.5);
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
        return "LineMagnetImpl{" + "id=" + id + '}';
    }    

    public void setMagnetVisible(boolean visible) {
        setVisible(visible);
    }

}
