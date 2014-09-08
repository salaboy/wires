package com.bayesian.network.client.shapes;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.emitrom.lienzo.client.core.shape.Rectangle;
import com.emitrom.lienzo.client.core.shape.Text;
import com.google.common.collect.Maps;
import org.kie.wires.core.client.shapes.WiresRectangle;

public class EditableBayesianNode extends WiresRectangle implements Serializable {

    private static final long serialVersionUID = -5490131652690005490L;
    private Rectangle header;
    private Text textHeader;
    private Map<Text, List<Rectangle>> porcentualBars;

    public EditableBayesianNode() {
        super( 0, 0 );
    }

    public EditableBayesianNode( final double width,
                                 final double height,
                                 final double positionXNode,
                                 final double positionYNode,
                                 final String fillColor ) {
        super( width,
               height );
        super.init( positionXNode,
                    positionYNode );
        this.porcentualBars = Maps.newHashMap();
        super.getRectangle().setFillColor( fillColor );
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
        return super.getRectangle();
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

}
