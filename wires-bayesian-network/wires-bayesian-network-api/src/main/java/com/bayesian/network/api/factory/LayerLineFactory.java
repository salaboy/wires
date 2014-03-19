package com.bayesian.network.api.factory;

import org.kie.wires.core.client.shapes.WiresLine;
import org.kie.wires.core.client.util.ShapesUtils;

import com.emitrom.lienzo.client.core.shape.Group;
import com.emitrom.lienzo.client.core.shape.Line;
import com.emitrom.lienzo.client.core.shape.Shape;
import com.emitrom.lienzo.client.core.types.DragBounds;
import com.emitrom.lienzo.client.widget.LienzoPanel;

public class LayerLineFactory extends LayerFactory<Line> {

    private static final String DESCRIPTION = "Line";

    private static int layers;
    

    public LayerLineFactory() {

    }

    public LayerLineFactory(Group group, LienzoPanel panel, Integer lay) {
        layers = lay;
        this.drawBoundingBox(group, null);
    }

    @Override
    public void drawBoundingBox(Group group, String template) {
        final Double x = this.getX1() + 218;
        final Double y = this.getY1() + 5;
        super.createOptions(x.intValue(), y.intValue());
        super.createBoundingBox(group, layers);
        group.add(this.drawLayer());
        group.add(super.createDescription(DESCRIPTION, layers));
    }

    @Override
    public Shape<Line> drawLayer() {
        WiresLine editableLine = new WiresLine(this.getX1(), this.getY1(), this.getX2(), this.getY2());
        editableLine.getLine().setDragBounds(new DragBounds(150, 260, 150, 150));
        editableLine.getLine().setStrokeColor(ShapesUtils.RGB_STROKE_SHAPE).setStrokeWidth(ShapesUtils.RGB_STROKE_WIDTH_SHAPE)
                .setDraggable(false);
        return editableLine.getLine();
    }

    private double getX1() {
        return 12;
    }

    private double getY1() {
        return 8 + super.calculateY(layers);
    }

    private double getX2() {
        return 32;
    }

    private double getY2() {
        return 20 + super.calculateY(layers);
    }
    

}
