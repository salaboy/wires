/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.kie.wires.client.shapes.collision.api;

/**
 *
 * @author salaboy
 */
public class Vector {
    private double x;
    private double y;

    public Vector() {
    }

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
    
 
    public double getMagnitude(){
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }

    public Vector add(Vector v1){
        Vector v = new Vector();
        v.setX(this.getX() + v1.getX() );
        v.setY(this.getY() + v1.getY());
        return v;
    }
    
    public Vector substract(Vector v1){
        Vector v = new Vector();
        v.setX(this.getX() - v1.getX() );
        v.setY(this.getY() - v1.getY());
        return v;
    }
    
    public double dotProduct(Vector v1){
        return this.getX() * v1.getX() +
                    this.getY() * v1.getY();
    }
    
    public Vector edge(Vector v1){
        return this.substract(v1);
    }

    public Vector perpendicular(){
        Vector v = new Vector();
        v.setX(this.getY());
        v.setY(0 - this.getX());
        return v;
    }

    public Vector normalize(){
        Vector v = new Vector();
        double m = this.getMagnitude();
        v.setX(this.getX() / m);
        v.setY(this.getY() / m);
        return v;
    }

    public Vector normal(){
        Vector p = this.perpendicular();
        return p.normalize();
    }
    
}
