package org.kie.wires.client.shapes.collision.util;


import org.kie.wires.client.shapes.collision.api.CollidableShape;
import org.kie.wires.client.shapes.collision.api.Magnet;
import org.kie.wires.client.shapes.collision.api.StickableShape;
import com.emitrom.lienzo.client.core.event.NodeDragMoveEvent;
import com.emitrom.lienzo.client.core.shape.Shape;
import com.emitrom.lienzo.shared.core.types.ColorName;
import java.util.List;
import static org.kie.wires.client.canvas.CanvasScreen.shapesInCanvas;
import static org.kie.wires.client.factoryShapes.ShapeFactoryUtil.MAGNET_RGB_FILL_SHAPE;
import org.kie.wires.client.shapes.api.EditableShape;

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

    public static Magnet detectCollisions(EditableShape shapeActive, NodeDragMoveEvent event) {
        //GWT.log(" # of shapes in canvas: "+shapesInCanvas.size());
//        for (EditableShape shape : shapesInCanvas) {
//            if (shape.isBeingDragged() || shape.isBeingResized()) {
//                shapeActive = shape;
//            }
//        }
        Magnet selectedMagnet = null;
        if (shapeActive != null) {
            for (EditableShape shape : shapesInCanvas) {
                if (!shape.getId().equals(shapeActive.getId())
                        && ((CollidableShape) shapeActive).collidesWith(((CollidableShape) shape))) {

                    ((StickableShape) shape).showMagnetsPoints();

                    List<Magnet> magnets = ((StickableShape) shape).getMagnets();
                    double finalDistance = 1000;

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
                    if (selectedMagnet != null) {
                        ((Shape) selectedMagnet).setFillColor(ColorName.GREEN);

                    }

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
}
