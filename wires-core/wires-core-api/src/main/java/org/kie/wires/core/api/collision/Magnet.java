/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kie.wires.core.api.collision;

import java.util.List;

import org.kie.wires.core.api.shapes.WiresBaseGroupShape;


/**
 *
 * @author salaboy
 */
public interface Magnet {

    static final int MAGNET_START = 0;
    static final int MAGNET_END = 1;

    static final int MAGNET_TOP = 2;
    static final int MAGNET_BOTTOM = 3;
    static final int MAGNET_RIGHT = 4;
    static final int MAGNET_LEFT = 5;

    void placeMagnetPoints();
    
    double getX();
    
    double getY();
    
    void setMagnetActive(boolean active);
    
    void attachControlPoint(ControlPoint controlPoint);
    
    List<ControlPoint> getAttachedControlPoints();
    
    String getId();
    
    void setMagnetVisible(boolean visible);
    
    WiresBaseGroupShape getShape();
    
    int getType();
    
}
