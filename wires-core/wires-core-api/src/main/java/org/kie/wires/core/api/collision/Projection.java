/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.kie.wires.core.api.collision;

/**
 *
 * @author salaboy
 */
public class Projection {
    private double min;
    private double max;

    public Projection() {
    }

    public Projection(double min, double max) {
        this.min = min;
        this.max = max;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }
    
    public boolean overlaps(Projection projection){
        return this.max > projection.min && projection.max > this.min;
    }
}
