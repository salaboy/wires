package com.bayesian.network.client.factory;

import com.emitrom.lienzo.client.core.shape.Rectangle;
import com.emitrom.lienzo.client.core.shape.Shape;
import com.emitrom.lienzo.client.core.shape.Text;
import org.kie.wires.core.client.shapes.TemplateShape;
import org.kie.wires.core.client.util.ShapeFactoryUtil;
import org.kie.wires.core.client.util.ShapesUtils;

public abstract class LayerFactory<T extends Shape<T>> {

    public LayerFactory() {
        this.templateShape = new TemplateShape();
    }

    private static final int LAYERS_BY_ROW = 1;

    protected TemplateShape templateShape;

    protected abstract void drawBoundingBox( String template );

    protected abstract void drawLayer();

    protected void createBoundingBox( final int layers ) {
        final Rectangle boundingBox = new Rectangle( ShapeFactoryUtil.WIDTH_BOUNDING_LAYER,
                                                     ShapeFactoryUtil.HEIGHT_BOUNDING_LAYER );
        boundingBox.setX( getXBoundingBox() ).setY( this.getYBoundingBox( layers ) )
                .setStrokeColor( ShapeFactoryUtil.RGB_STROKE_BOUNDING ).setStrokeWidth( 1 )
                .setFillColor( ShapeFactoryUtil.RGB_FILL_BOUNDING ).setDraggable( false );
        templateShape.setBounding( boundingBox );
    }

    private double getXBoundingBox() {
        return 0;
    }

    private double getYBoundingBox( final int layers ) {
        return calculateY( layers );
    }

    protected double getYText( final int layers ) {
        return 20 + calculateY( layers );
    }

    protected int calculateX( final int layers ) {
        int x = layers > 1 ? ( this.getPositionInRow( layers ) - 1 ) : 0;
        return x > 0 ? ( ShapeFactoryUtil.HEIGHT_BOUNDING_LAYER * x ) : 0;
    }

    protected int calculateY( final int layers ) {
        int y = layers > 1 ? layers - 1 : 0;
        return y * ShapeFactoryUtil.HEIGHT_BOUNDING_LAYER;
    }

    private int getPositionInRow( final int shapes ) {
        return ( shapes - this.layersByRow() ) >= 1 ? ( shapes - ( this.layersByRow() * getRow( shapes ) ) ) : shapes;
    }

    private int getRow( final int layers ) {
        return Math.round( ( layers * ShapeFactoryUtil.WIDTH_BOUNDING_LAYER ) / ShapeFactoryUtil.WIDTH_STENCIL );
    }

    private int layersByRow() {
        return LAYERS_BY_ROW;
    }

    protected void createDescription( final String description,
                                      final int shapes ) {
        Text text = new Text( description, ShapeFactoryUtil.FONT_FAMILY_DESCRIPTION, ShapeFactoryUtil.FONT_SIZE_DESCRIPTION );
        text.setX( 45 ).setY( this.getYText( shapes ) ).setFillColor( ShapeFactoryUtil.RGB_TEXT_DESCRIPTION );
        templateShape.setDescription( text );
    }

    protected void setAttributes( final Shape<?> floatingShape,
                                  final double x,
                                  final double y ) {
        floatingShape.setX( x ).setY( y ).setStrokeColor( ShapesUtils.RGB_STROKE_SHAPE )
                .setStrokeWidth( ShapesUtils.RGB_STROKE_WIDTH_SHAPE ).setFillColor( ShapesUtils.RGB_FILL_SHAPE )
                .setDraggable( false );
    }

    public TemplateShape getLayer() {
        return templateShape;
    }

}
