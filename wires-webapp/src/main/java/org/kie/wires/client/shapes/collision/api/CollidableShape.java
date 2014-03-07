/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.kie.wires.client.shapes.collision.api;

import java.util.List;

/**
 *
 * @author salaboy
 */
public interface CollidableShape {
    
    boolean collidesWith(CollidableShape shape);
    
    boolean separationOnAxes(List<Vector> axes, CollidableShape shape);
    
    List<Vector> getAxes();
    
    Projection project(Vector axis);
    
}
