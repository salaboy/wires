package org.kie.wires.client.shapes.util;

import org.kie.wires.client.shapes.api.EditableShape;
import org.kie.wires.client.canvas.CanvasScreen;
import org.kie.wires.client.shapes.api.WiresBaseGroupShape;

public class ShapesUtils {

    public static final String LIGHT_BLUE = "#A8C6FA";
    
    public static int selectedShape;
    
    public static void deselectAllOtherShapes(){
        for (WiresBaseGroupShape shape: CanvasScreen.shapesInCanvas) {
                if(shape.hashCode()  != selectedShape){
                   shape.hideControlPoints();
                   shape.hideMagnetPoints();
                }
        }
      
    }
    
    public static void deselectAllShapes(){
        selectedShape = 0;
        deselectAllOtherShapes();
    }
    public static void nodeMouseClickHandler(final EditableShape shape) {
//        GWT.log("click: " + ":" + shape.hashCode() + "Showing control points!");
        selectedShape = shape.hashCode();
        shape.showControlPoints();
    }

    
    

    
    
}
