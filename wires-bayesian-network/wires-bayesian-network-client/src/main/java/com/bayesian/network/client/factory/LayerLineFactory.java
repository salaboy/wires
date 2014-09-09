package com.bayesian.network.client.factory;

import com.emitrom.lienzo.client.core.shape.Line;
import com.emitrom.lienzo.client.core.types.DragBounds;
import org.kie.wires.core.client.util.ShapesUtils;

public class LayerLineFactory extends LayerFactory<Line> {

    private static final String DESCRIPTION = "Line";

    private static int layers;

    public LayerLineFactory( final Integer lay ) {
        layers = lay;
        this.drawBoundingBox( null );
    }

    @Override
    public void drawBoundingBox( final String template ) {
        super.createBoundingBox( layers );
        this.drawLayer();
        super.createDescription( DESCRIPTION, layers );
    }

    @Override
    public void drawLayer() {
        Line editableLine = new Line( this.getX1(),
                                      this.getY1(),
                                      this.getX2(),
                                      this.getY2() );
        editableLine.setDragBounds( new DragBounds( 150,
                                                    260,
                                                    150,
                                                    150 ) );
        editableLine.setStrokeColor( ShapesUtils.RGB_STROKE_SHAPE ).setStrokeWidth( ShapesUtils.RGB_STROKE_WIDTH_SHAPE );
        templateShape.setShape( editableLine );
    }

    private double getX1() {
        return 12;
    }

    private double getY1() {
        return 8 + super.calculateY( layers );
    }

    private double getX2() {
        return 32;
    }

    private double getY2() {
        return 20 + super.calculateY( layers );
    }

}
