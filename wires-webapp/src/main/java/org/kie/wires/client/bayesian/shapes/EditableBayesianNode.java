package org.kie.wires.client.bayesian.shapes;

import org.kie.wires.client.factoryShapes.ShapeFactoryUtil;
import org.kie.wires.client.shapes.EditableRectangle;
import org.kie.wires.client.util.BayesianUtils;

import com.emitrom.lienzo.client.core.shape.Layer;
import com.emitrom.lienzo.client.core.shape.Rectangle;
import com.emitrom.lienzo.client.core.shape.Shape;
import com.emitrom.lienzo.client.core.shape.Text;
import com.emitrom.lienzo.shared.core.types.Color;
import com.xstream.bayesian.client.model.BayesVariable;

/**
 * 
 * @author salaboy
 */
public class EditableBayesianNode extends EditableRectangle {

    public EditableBayesianNode(double width, double height) {
        super(width, height);
    }

    public void init(double x, double y, Layer layer, BayesVariable node) {
        super.init(x, y, layer);
        // header
        add(this.getHeader());
        drawText(node.getName(), BayesianUtils.FONT_SIZE_HEADER_NODE, BayesianUtils.LABEL_POSITION_X_DEFAULT,
                BayesianUtils.LABEL_POSITION_Y_DEFAULT);
        // porcentual bar
        drawPorcentualbar(node);
    }

    private Rectangle getHeader() {
        return new Rectangle(getRectangle().getWidth(), BayesianUtils.HEIGHT_HEADER);
    }

    private void drawPorcentualbar(BayesVariable node) {
        String fillColor = BayesianUtils.getNodeColors()[0][1];
        int positionY, widthFill;
        int positionX = (int) 64;
        positionY = (int) 15;
        positionY = (node.getOutcomes().size() > 3) ? positionY - 10 : positionY;
        String borderColor = fillColor;
        for (int i = 0; i < node.getOutcomes().size(); i++) {
            // Porcentual bar
            positionY += 14;
            drawText(node.getOutcomes().get(i), BayesianUtils.FONT_SIZE_PORCENTUAL_BAR, BayesianUtils.LABEL_POSITION_X_DEFAULT,
                    positionY + 7);
            drawComponent(Color.rgbToBrowserHexColor(255, 255, 255), positionX, positionY, BayesianUtils.WIDTH_PORCENTUAL_BAR,
                    BayesianUtils.HEIGHT_PORCENTUAL_BAR, borderColor, true);
            // fill bar
            widthFill = calculatePorcentage(node.getProbabilities(), BayesianUtils.WIDTH_PORCENTUAL_BAR, i);
            drawComponent(fillColor, positionX, positionY, widthFill, BayesianUtils.HEIGHT_PORCENTUAL_BAR, borderColor, false);
        }
    }

    private void drawText(String description, int fontSize, int positionX, int positionY) {
        final Text text = new Text(description, "Times", fontSize);
        text.setX(positionX).setY(positionY);
        add(text);
    }

    private void drawComponent(String color, int positionX, int positionY, int width, int height, String borderColor,
            boolean isEditable) {
        if (borderColor == null) {
            borderColor = Color.rgbToBrowserHexColor(0, 0, 0);
        }
        Shape<?> component = new Rectangle(width, height);
        setAttributes(component, color, positionX, positionY, borderColor);
        add(component);
    }

    protected void setAttributes(Shape<?> shape, String fillColor, double x, double y, String borderColor) {
        String fill = (fillColor == null) ? BayesianUtils.DEFAULT_PORCENTUAL_FILL_COLOR : fillColor;
        String border = (borderColor == null) ? BayesianUtils.DEFAULT_PORCENTUAL_BORDER_COLOR : borderColor;

        shape.setX(x).setY(y).setStrokeColor(border).setStrokeWidth(ShapeFactoryUtil.RGB_STROKE_WIDTH_SHAPE).setFillColor(fill)
                .setDraggable(false);

    }

    private int calculatePorcentage(double probabilities[][], int maxWidthFill, int position) {
        double porcentual = 0;
        if (position == 0) {
            porcentual = probabilities[0][0];
        } else if (position == 1) {
            porcentual = probabilities[0][1];
        }
        porcentual *= 100;
        return (int) ((porcentual * maxWidthFill) / 100);
    }

}
