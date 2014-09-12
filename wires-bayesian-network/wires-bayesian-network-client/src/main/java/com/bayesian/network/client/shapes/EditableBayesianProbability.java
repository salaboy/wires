package com.bayesian.network.client.shapes;

import java.io.Serializable;
import java.util.Map;

import com.emitrom.lienzo.client.core.shape.Rectangle;
import com.emitrom.lienzo.client.core.shape.Text;
import com.emitrom.lienzo.shared.core.types.ColorName;
import com.google.common.collect.Maps;
import org.kie.wires.core.client.shapes.dynamic.WiresRectangle;

public class EditableBayesianProbability extends WiresRectangle implements Serializable {

    private static final long serialVersionUID = 286548230036126637L;
    private Map<Text, Rectangle> parentNode;
    private Map<Text, Rectangle> porcentualOptions;
    private Map<Text, Rectangle> porcentualValues;
    private Map<Map<Text, Rectangle>, Map<Text, Rectangle>> incomingNodes;

    public EditableBayesianProbability() {
        super( 0,
               0,
               0,
               0 );
    }

    public EditableBayesianProbability( final double width,
                                        final double height,
                                        final double positionXNode,
                                        final double positionYNode ) {
        super( positionXNode,
               positionYNode,
               positionXNode + width,
               positionYNode + height );
        super.init( positionXNode,
                    positionYNode );
        super.getRectangle().setStrokeColor( ColorName.WHITE.getValue() );
        this.parentNode = Maps.newHashMap();
        this.porcentualOptions = Maps.newHashMap();
        this.porcentualValues = Maps.newHashMap();
        this.incomingNodes = Maps.newHashMap();
    }

    public void buildGrid() {
        drawComponents( this.parentNode );
        drawComponents( this.porcentualOptions );
        drawComponents( this.porcentualValues );
        if ( this.incomingNodes != null && !this.incomingNodes.isEmpty() ) {
            for ( Map.Entry<Map<Text, Rectangle>, Map<Text, Rectangle>> porc : incomingNodes.entrySet() ) {
                drawComponents( porc.getValue() );
                drawComponents( porc.getKey() );
            }

        }
    }

    private void drawComponents( final Map<Text, Rectangle> hash ) {
        for ( Map.Entry<Text, Rectangle> parent : hash.entrySet() ) {
            add( parent.getValue() );
            add( parent.getKey() );
        }
    }

    public Map<Text, Rectangle> getParentNode() {
        return parentNode;
    }

    public void setParentNode( final Map<Text, Rectangle> parentNode ) {
        this.parentNode = parentNode;
    }

    public Map<Text, Rectangle> getPorcentualOptions() {
        return porcentualOptions;
    }

    public void setPorcentualOptions( final Map<Text, Rectangle> porcentualOptions ) {
        this.porcentualOptions = porcentualOptions;
    }

    public Map<Text, Rectangle> getPorcentualValues() {
        return porcentualValues;
    }

    public void setPorcentualValues( final Map<Text, Rectangle> porcentualValues ) {
        this.porcentualValues = porcentualValues;
    }

    public Map<Map<Text, Rectangle>, Map<Text, Rectangle>> getIncomingNodes() {
        return incomingNodes;
    }

    public void setIncomingNodes( final Map<Map<Text, Rectangle>, Map<Text, Rectangle>> incomingNodes ) {
        this.incomingNodes = incomingNodes;
    }

}
