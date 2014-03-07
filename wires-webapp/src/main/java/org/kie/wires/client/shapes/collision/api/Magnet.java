/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kie.wires.client.shapes.collision.api;

import com.emitrom.lienzo.client.core.shape.Layer;
import java.util.List;

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
    
}
