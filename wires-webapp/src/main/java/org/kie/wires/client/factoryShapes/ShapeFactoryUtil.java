package org.kie.wires.client.factoryShapes;

import com.emitrom.lienzo.shared.core.types.Color;

public class ShapeFactoryUtil {
    
    //stencil
    public static final int STENCIL_WIDTH = 260;
    
    //panel
    public static final int WIDTH_PANEL = 200;
    public static final int HEIGHT_PANEL = 300; 
    
    //bounding
    public static final int WIDTH_BOUNDING = 50;
    public static final int HEIGHT_BOUNDING = 50;
    public static final String RGB_FILL_BOUNDING = Color.rgbToBrowserHexColor(255, 255, 255);
    public static final String RGB_STROKE_BOUNDING = Color.rgbToBrowserHexColor(219, 217, 217);
    public static final int SPACE_BETWEEN_BOUNDING = 2;

    //description
    public static final String RGB_TEXT_DESCRIPTION = Color.rgbToBrowserHexColor(188, 187, 189);
    public static final String FONT_FAMILY_DESCRIPTION = "oblique normal";
    public static final double FONT_SIZE_DESCRIPTION = 10;
    
    //shapes
    public static final String RGB_STROKE_SHAPE = Color.rgbToBrowserHexColor(255, 0, 0);
    public static final String RGB_FILL_SHAPE = Color.rgbToBrowserHexColor(0, 255, 255);
    public static final int RGB_STROKE_WIDTH_SHAPE = 2;


    
    

}
