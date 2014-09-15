package org.kie.wires.core.client.util;

import java.util.Set;

import org.kie.wires.core.api.factories.categories.Category;
import org.kie.wires.core.api.factories.ShapeFactory;

public class ShapesUtils {

    public static final String LIGHT_BLUE = "#A8C6FA";

    // Control Points
    public static final String CP_RGB_FILL_COLOR = "#0000FF";
    public static final int CP_RGB_STROKE_WIDTH_SHAPE = 1;

    // Magnets
    public static final String MAGNET_RGB_FILL_SHAPE = "#f2f2f2";
    public static final String MAGNET_ACTIVE_RGB_FILL_SHAPE = "#00ff00";

    // shapes
    public static final String RGB_STROKE_SHAPE = "#999999";
    public static final String RGB_FILL_SHAPE = "#f2f2f2";
    public static final int RGB_STROKE_WIDTH_SHAPE = 2;
    public static final int RGB_STROKE_WIDTH_LINE = 4;

    public static int getNumberOfShapesInCategory( final Category shapeCategory,
                                                   final Set<ShapeFactory> factories ) {
        int account = 0;
        for ( ShapeFactory factory : factories ) {
            if ( factory.getCategory().equals( shapeCategory ) ) {
                account++;
            }
        }
        return account;
    }

    public static int calculateHeight( int shapes ) {
        int y = shapes > 1 ? getRow( shapes ) : 0;
        y = y > 0 ? ( y * ShapeFactoryUtil.HEIGHT_BOUNDING ) + ShapeFactoryUtil.SPACE_BETWEEN_BOUNDING * y : y
                * ShapeFactoryUtil.HEIGHT_BOUNDING;
        return y + ShapeFactoryUtil.HEIGHT_BOUNDING + 15;
    }

    public static int getRow( int shapes ) {
        return Math.round( ( shapes * ShapeFactoryUtil.WIDTH_BOUNDING ) / ShapeFactoryUtil.WIDTH_STENCIL );
    }

}
