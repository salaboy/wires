/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kie.wires.core.api.shapes;

import com.emitrom.lienzo.client.core.shape.Layer;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.event.Event;

import org.kie.wires.core.api.collision.CollidableShape;
import org.kie.wires.core.api.collision.ControlPoint;
import org.kie.wires.core.api.collision.Magnet;
import org.kie.wires.core.api.collision.StickableShape;
import org.kie.wires.core.api.events.ShapeSelectedEvent;

import com.emitrom.lienzo.client.core.shape.Group;
import com.emitrom.lienzo.client.core.shape.Shape;


public abstract class WiresBaseGroupShape extends Group implements EditableShape, CollidableShape, StickableShape {

    protected List<ControlPoint> controlPoints = new ArrayList<ControlPoint>();
    protected List<Magnet> magnets = new ArrayList<Magnet>();
    private Event<ShapeSelectedEvent> selected;

    private boolean showingMagnets = false;
    private boolean showingControlPoints = false;

    public WiresBaseGroupShape() {
    }

    public void addControlPoint(ControlPoint cp) {
        controlPoints.add(cp);
    }

    public void addMagnet(Magnet m) {
        magnets.add(m);
    }

    public List<ControlPoint> getControlPoints() {
        return controlPoints;
    }

    public List<Magnet> getMagnets() {
        return magnets;
    }

    public void init() {

        setDraggable(true);

    }

    public Event<ShapeSelectedEvent> getSelected() {
        return selected;
    }

    public void setSelected(Event<ShapeSelectedEvent> selected) {
        this.selected = selected;
    }

    public void showControlPoints() {
        final Layer layer = getLayer();
        if (!controlPoints.isEmpty() && !showingControlPoints) {
            for (ControlPoint cp : controlPoints) {
                layer.add((Shape) cp);
                cp.placeControlPoint(layer);
            }
            showingControlPoints = true;
        }

    }

    public void hideControlPoints() {

        // can be null, afer the main Shape is dragged, and control points are forcibly removed
        Layer layer = getLayer();
        if (!controlPoints.isEmpty() && showingControlPoints) {
            for (ControlPoint cp : controlPoints) {
                layer.remove((Shape) cp);
            }
            showingControlPoints = false;
        }

    }

    public void showMagnetsPoints() {
        final Layer layer = getLayer();
        if (!magnets.isEmpty() && !showingMagnets) {
            for (Magnet m : magnets) {
                layer.add((Shape) m);
                m.placeMagnetPoints();
            }

            showingMagnets = true;
        }

    }

    @Override
    public void hideMagnetPoints() {
        Layer layer = getLayer();
        if (!magnets.isEmpty() && showingMagnets) {
            for (Magnet m : magnets) {
                layer.remove((Shape) m);
            }

            showingMagnets = false;
        }

    }

    public void attachControlPointToMagent(Magnet selectedMagnet) {
        double[] distances = new double[controlPoints.size()];
        for (int i = 0; i < controlPoints.size(); i++) {
            double pointX = ((Shape) controlPoints.get(i)).getX();
            double pointY = ((Shape) controlPoints.get(i)).getY();

            double deltaX = selectedMagnet.getX() - pointX;
            double deltaY = selectedMagnet.getY() - pointY;

            distances[i] = Math.sqrt(Math.pow(deltaX, 2)
                    + Math.pow(deltaY, 2));

        }
        int minIndex = min(distances);

        if (!selectedMagnet.getAttachedControlPoints().contains(controlPoints.get(minIndex))) {
            selectedMagnet.attachControlPoint(controlPoints.get(minIndex));
            controlPoints.get(minIndex).setControlPointX(selectedMagnet.getX());
            controlPoints.get(minIndex).setControlPointY(selectedMagnet.getY());
            // I need to clean up all the other magnets of the shape to make sure that 
            // no other magnet has the same shape attached
            for (Magnet m : selectedMagnet.getShape().getMagnets()) {
                if (!m.getId().equals(selectedMagnet.getId())) {
                    if (m.getAttachedControlPoints().contains(controlPoints.get(minIndex))) {
                        m.getAttachedControlPoints().remove(controlPoints.get(minIndex));
                    }
                }
            }
        }

    }

    private static int min(double[] values) {
        double min = Double.MAX_VALUE;
        int i = 0;
        for (double value : values) {
            if (value < min) {
                min = value;
                i++;
            }
        }

        return i-1;
    }

}
