package com.bayesian.network.client.shapes;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.emitrom.lienzo.client.core.shape.Rectangle;
import com.emitrom.lienzo.client.core.shape.Text;
import com.google.common.collect.Maps;
import org.kie.wires.core.api.shapes.WiresBaseShape;
import org.kie.wires.core.client.util.ShapesUtils;

public class EditableBayesianNode extends WiresBaseShape implements Serializable {

    private static final long serialVersionUID = -5490131652690005490L;

    private Rectangle rectangle;
    private Rectangle header;
    private Text textHeader;
    private Map<Text, List<Rectangle>> porcentualBars;

    public EditableBayesianNode() {
        this( 0,
              0,
              0,
              0,
              "" );
    }

    public EditableBayesianNode( final double width,
                                 final double height,
                                 final double positionXNode,
                                 final double positionYNode,
                                 final String fillColor ) {
        rectangle = new Rectangle( width,
                                   height );
        rectangle.setStrokeColor( ShapesUtils.RGB_STROKE_SHAPE );
        rectangle.setStrokeWidth( ShapesUtils.RGB_STROKE_WIDTH_SHAPE );
        rectangle.setFillColor( fillColor );

        add( rectangle );

        init( positionXNode,
              positionYNode );

        this.porcentualBars = Maps.newHashMap();
    }

    @Override
    public void setSelected( final boolean isSelected ) {
        //We don't support visual changes when selected
    }

    @Override
    public boolean contains( double cx,
                             double cy ) {
        //We don't have any ControlPoints so no need to worry about whether we contain a given point
        return false;
    }

    public void buildNode() {
        add( this.header );
        add( this.textHeader );
        for ( Map.Entry<Text, List<Rectangle>> porcenualBar : this.porcentualBars.entrySet() ) {
            for ( Rectangle rec : porcenualBar.getValue() ) {
                add( rec );
            }
            add( porcenualBar.getKey() );
        }
    }

    public Rectangle getParentNode() {
        return rectangle;
    }

    public Rectangle getHeader() {
        return header;
    }

    public void setHeader( final Rectangle header ) {
        this.header = header;
    }

    public Text getTextHeader() {
        return textHeader;
    }

    public void setTextHeader( final Text textHeader ) {
        this.textHeader = textHeader;
    }

    public Map<Text, List<Rectangle>> getPorcentualsBar() {
        return porcentualBars;
    }

    public void setPorcentualBars( final Map<Text, List<Rectangle>> porcentualBars ) {
        this.porcentualBars = porcentualBars;
    }

    public double getWidth() {
        return rectangle.getWidth();
    }

}
