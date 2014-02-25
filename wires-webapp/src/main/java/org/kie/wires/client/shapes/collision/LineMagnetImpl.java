/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kie.wires.client.shapes.collision;

import com.emitrom.lienzo.client.core.shape.Circle;
import com.emitrom.lienzo.client.core.types.Point2DArray;
import java.util.ArrayList;
import java.util.List;
import static org.kie.wires.client.factoryShapes.ShapeFactoryUtil.MAGNET_RGB_FILL_SHAPE;
import org.kie.wires.client.shapes.EditableLine;
import org.kie.wires.client.util.UUID;

/**
 *
 * @author salaboy
 */
public class LineMagnetImpl extends Circle implements Magnet {

    private final List<ControlPoint> attachedControlPoints = new ArrayList<ControlPoint>();

    private String id;
    private EditableLine shape;
    
    private int type;
    
    public LineMagnetImpl(EditableLine shape, int type) {
       this(6);
       this.shape = shape;
       this.type = type;
    }

    public LineMagnetImpl(double radius) {
        super(radius);
        setFillColor(MAGNET_RGB_FILL_SHAPE);
        this.id = UUID.uuid();
    }

    public void placeMagnetPoints( ) {
        Point2DArray points = shape.getLine().getPoints();

        switch (type) {
            case MAGNET_START:
                setX(shape.getX() + points.getPoint(0).getX());
                setY(shape.getY() + points.getPoint(0).getY());
                break;
            case MAGNET_END:
                setX(shape.getX() + points.getPoint(1).getX());
                setY(shape.getY() + points.getPoint(1).getY());
                break;

        }
        
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
