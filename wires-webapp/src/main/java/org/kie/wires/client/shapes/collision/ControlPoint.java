/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kie.wires.client.shapes.collision;

import com.emitrom.lienzo.client.core.shape.Layer;
import com.emitrom.lienzo.client.core.shape.Shape;

/**
 *
 * @author salaboy
 */
public interface ControlPoint {

    static final int CONTROL_START = 0;
    static final int CONTROL_END = 1;
    
    static final int CONTROL_TOP_LEFT = 2;
    static final int CONTROL_BOTTOM_LEFT = 3;
    static final int CONTROL_TOP_RIGHT = 4;
    static final int CONTROL_BOTTOM_RIGHT = 5;
    
    String getId();
    
    void placeControlPoint(Layer layer);
    
    void setControlPointX(double x);
    
    void setControlPointY(double y);
    
    void setControlPointVisible(boolean visible);
    
    void moveControlPoint();
    
    Shape getShape();
    
    int getControlType();
    
    void udpateShape(Layer layer, double x, double y);
    
}
