package org.kie.wires.core.client.util;

import com.google.gwt.core.client.GWT;
import java.util.List;

import org.kie.wires.core.api.collision.StickableShape;
import org.kie.wires.core.api.shapes.EditableShape;

public class ShapesUtils {

    public static final String LIGHT_BLUE = "#A8C6FA";

    public static String selectedShapeId;

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
            if (!selectedShapeId.equals(shape.getId())) {
                shape.hideControlPoints();
                ((StickableShape) shape).hideMagnetPoints();
            }
        }

    }

    public static void deselectAllShapes(List<EditableShape> shapesInCanvas) {
        selectedShapeId = "";
        deselectAllOtherShapes(shapesInCanvas);
    }

    public static void nodeMouseClickHandler(final EditableShape shape) {
        selectedShapeId = shape.getId();
        shape.showControlPoints();
    }

    public static int getAccountShapesByCategory(ShapeCategory shapeCategory) {
        int account = 0;
        for (ShapeType shapeType : ShapeType.values()) {
            if (shapeType.getCategory().equals(shapeCategory)) {
                account++;
            }
        }
        return account;
    }

    public static int calculateHeight(int shapes) {
        int y = shapes > 1 ? getRow(shapes) : 0;
        y = y > 0 ? (y * ShapeFactoryUtil.HEIGHT_BOUNDING) + ShapeFactoryUtil.SPACE_BETWEEN_BOUNDING * y : y
                * ShapeFactoryUtil.HEIGHT_BOUNDING;
        return y + ShapeFactoryUtil.HEIGHT_BOUNDING + 15;
    }

    public static int getRow(int shapes) {
        return Math.round((shapes * ShapeFactoryUtil.WIDTH_BOUNDING) / ShapeFactoryUtil.WIDTH_STENCIL);
    }

}
