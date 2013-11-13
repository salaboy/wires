package org.kie.wires.client.shapes;

import com.emitrom.lienzo.client.core.shape.IPrimitive;
import com.emitrom.lienzo.client.core.shape.Layer;
import com.google.gwt.core.client.GWT;

public class ShapesUtils {

    public static final String LIGHT_BLUE = "#A8C6FA";
    
    public static int selectedShape;
    
    static void deselectAllOtherShapes(Layer layer){
      
        for (IPrimitive<?> iPrimitive : layer) {
            if(iPrimitive instanceof EditableShape){
               
                if(((EditableShape)iPrimitive).hashCode()  != selectedShape){
                   ((EditableShape)iPrimitive).hideDragPoints();
                }
            }
        }
      
    }
    
    static void nodeMouseClickHandler(final EditableShape shape) {
        GWT.log("click: " + ":" + shape.hashCode());
        selectedShape = shape.hashCode();
        shape.showDragPoints();
    }

    static void nodeMouseEnterHandler(final EditableShape shape) {
        GWT.log("enter: " + ":" + shape.getClass().getName());
        shape.showMagnetsPoints();
    }
    
    
    
    static void nodeMouseExitHandler(final EditableShape shape) {
        GWT.log("exit " + ":" + shape.getClass().getName() );

    }
      static void nodeMouseOutHandler(final EditableShape shape) {
        GWT.log("out " + ":" + shape.getClass().getName() );

    }
}
