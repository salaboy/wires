package org.kie.wires.core.client.util;


import com.emitrom.lienzo.client.core.event.NodeDragMoveEvent;
import com.emitrom.lienzo.client.core.shape.Shape;
import com.emitrom.lienzo.shared.core.types.ColorName;
import com.google.gwt.core.client.GWT;
import java.util.List;
import org.kie.wires.core.api.collision.CollidableShape;
import org.kie.wires.core.api.collision.ControlPoint;
import org.kie.wires.core.api.collision.Magnet;
import org.kie.wires.core.api.collision.StickableShape;
import org.kie.wires.core.api.shapes.EditableShape;
import org.kie.wires.core.client.canvas.Canvas;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author salaboy
 */
public class CollisionDetectionUtil {
    private final static String MAGNET_RGB_FILL_SHAPE = "#f2f2f2";

    public static Magnet detectCollisions(ControlPoint cp, EditableShape shapeActive, NodeDragMoveEvent event) {
        Magnet selectedMagnet = null;
        if (shapeActive != null) {
            for (EditableShape shape : Canvas.shapesInCanvas) {
                if (!shape.getId().equals(shapeActive.getId())
                        && ((CollidableShape) shapeActive).collidesWith(((CollidableShape) shape))) {

                    ((StickableShape) shape).showMagnetsPoints();

                    List<Magnet> magnets = ((StickableShape) shape).getMagnets();
                    double finalDistance = Double.MAX_VALUE;

                    for (Magnet magnet : magnets) {
                        double deltaX = event.getX() - magnet.getX();
                        double deltaY = event.getY() - magnet.getY();
                        double distance = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));

                        if (finalDistance > distance) {
                            finalDistance = distance;
                            selectedMagnet = magnet;
                        }
                        magnet.setMagnetActive(false);
                        ((Shape) magnet).setFillColor(MAGNET_RGB_FILL_SHAPE);
                    }
                    
                   

                }else{
                    ((StickableShape) shape).hideMagnetPoints();
                }

            }
        }
        return selectedMagnet;
    }

    public static void attachControlPointToMagnet(Magnet selectedMagnet, EditableShape shapeActive) {
        if (selectedMagnet != null && shapeActive != null) {
            ((StickableShape) shapeActive).attachControlPointToMagent(selectedMagnet);
            if (!selectedMagnet.getAttachedControlPoints().isEmpty()) {
                ((Shape) selectedMagnet).setFillColor(ColorName.RED);
            }
        }
    }
    
    public static void detachControlPointFromMagnet(EditableShape shapeActive){
       
    }
}
