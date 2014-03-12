package com.bayesian.network.api.factory;

import org.kie.wires.core.client.util.ShapesUtils;

import com.emitrom.lienzo.client.core.shape.Rectangle;
import com.emitrom.lienzo.client.core.shape.Shape;
import com.emitrom.lienzo.client.core.shape.Text;
import com.emitrom.lienzo.shared.core.types.Color;

public class BaseFactory {

    private static final String defaultFillColor = ShapesUtils.RGB_FILL_SHAPE;
    private static final String defaultBorderColor = ShapesUtils.RGB_STROKE_SHAPE;

    protected void setAttributes(Shape<?> shape, String fillColor, double x, double y, String borderColor) {
        String fill = (fillColor == null) ? defaultFillColor : fillColor;
        String border = (borderColor == null) ? defaultBorderColor : borderColor;

        shape.setX(x).setY(y).setStrokeColor(border).setStrokeWidth(ShapesUtils.RGB_STROKE_WIDTH_SHAPE).setFillColor(fill)
                .setDraggable(false);
    }

    protected Rectangle drawComponent(String color, int positionX, int positionY, int width, int height, String borderColor,
            double radius) {
        if (borderColor == null) {
            borderColor = Color.rgbToBrowserHexColor(0, 0, 0);
        }
        Rectangle component = new Rectangle(width, height);
        setAttributes(component, color, positionX, positionY, borderColor);
        component.setCornerRadius(radius);
        return component;
    }

    protected Text drawText(String description, int fontSize, int positionX, int positionY) {
        return new Text(description, "Times", fontSize).setX(positionX).setY(positionY);
    }

}
