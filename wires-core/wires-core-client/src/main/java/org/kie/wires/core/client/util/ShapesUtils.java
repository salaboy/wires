package org.kie.wires.core.client.util;

import java.util.ArrayList;
import java.util.List;

import org.kie.wires.core.api.collision.StickableShape;
import org.kie.wires.core.api.shapes.EditableShape;

public class ShapesUtils {

    public static final String LIGHT_BLUE = "#A8C6FA";

    public static int selectedShape;
    
    // Control Points
    public static final String CP_RGB_FILL_COLOR = "#0000FF";
    public static final int CP_RGB_STROKE_WIDTH_SHAPE = 1;

    // Magnets
    public static final String MAGNET_RGB_FILL_SHAPE = "#f2f2f2";

    // shapes
    public static final String RGB_STROKE_SHAPE = "#999999";
    public static final String RGB_FILL_SHAPE = "#f2f2f2";
    public static final int RGB_STROKE_WIDTH_SHAPE = 2;
    public static final int RGB_STROKE_WIDTH_LINE = 4;

    public static void deselectAllOtherShapes(List<EditableShape> shapesInCanvas) {
        for (EditableShape shape : shapesInCanvas) {
            if (shape.hashCode() != selectedShape) {
                shape.hideControlPoints();
                ((StickableShape) shape).hideMagnetPoints();
            }
        }

    }

    public static void deselectAllShapes(List<EditableShape> shapesInCanvas) {
        selectedShape = 0;
        deselectAllOtherShapes(shapesInCanvas);
    }

    public static void nodeMouseClickHandler(final EditableShape shape) {
        selectedShape = shape.hashCode();
        shape.showControlPoints();
    }

}
