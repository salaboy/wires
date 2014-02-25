package org.kie.wires.client.bayesian.factory;

import org.kie.wires.client.factoryShapes.ShapeFactoryUtil;
import org.kie.wires.client.shapes.EditableRectangle;

import com.emitrom.lienzo.client.core.shape.Layer;
import com.emitrom.lienzo.client.core.shape.Rectangle;
import com.emitrom.lienzo.client.core.shape.Shape;
import com.emitrom.lienzo.client.core.shape.Text;
import com.emitrom.lienzo.shared.core.types.Color;

public class BaseFactory {
	
	private static final String defaultFillColor = ShapeFactoryUtil.RGB_FILL_SHAPE;
	private static final String defaultBorderColor = ShapeFactoryUtil.RGB_STROKE_SHAPE;
    
    protected Shape<?> drawComponent(String color, int positionX, int positionY, int width, int height, String borderColor,
            Layer layer, boolean isEditable) {
        if (borderColor == null) {
            borderColor = Color.rgbToBrowserHexColor(0, 0, 0);
        }
        Shape<?> component =  new Rectangle(width, height); 
        setAttributes(component, color, positionX, positionY, borderColor);
        
        return component;
        
    }
    
    protected Text drawText(String color, int positionX, int positionY, int width, int height, String borderColor,
            String description, int fontSize, Layer layer) {
        final Text text = new Text(description, "Times", fontSize);
        text.setX(positionX + 8).setY(positionY + 15);
       
        return text;
    }
    
    protected void setAttributes(Shape<?> shape, String fillColor, double x, double y, String borderColor) {
    	String fill = (fillColor == null ) ? defaultFillColor : fillColor;
    	String border = (borderColor == null) ? defaultBorderColor : borderColor; 
    	
        shape.setX(x).setY(y).setStrokeColor(border).setStrokeWidth(ShapeFactoryUtil.RGB_STROKE_WIDTH_SHAPE)
                .setFillColor(fill).setDraggable(false);

    }
    
}
