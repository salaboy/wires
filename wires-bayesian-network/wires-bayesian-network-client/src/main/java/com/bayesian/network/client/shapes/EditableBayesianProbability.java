package com.bayesian.network.client.shapes;

import java.io.Serializable;
import java.util.Map;

import com.emitrom.lienzo.client.core.shape.Rectangle;
import com.emitrom.lienzo.client.core.shape.Text;
import com.google.common.collect.Maps;
import org.kie.wires.core.api.shapes.WiresBaseShape;
import org.kie.wires.core.client.util.ShapesUtils;

public class EditableBayesianProbability extends WiresBaseShape implements Serializable {

    private static final long serialVersionUID = 286548230036126637L;

    private Map<Text, Rectangle> parentNode;
    private Map<Text, Rectangle> porcentualOptions;
    private Map<Text, Rectangle> porcentualValues;
    private Map<Map<Text, Rectangle>, Map<Text, Rectangle>> incomingNodes;

    private final Rectangle rectangle;

    public EditableBayesianProbability() {
        this( 0,
              0,
              0,
              0 );
    }

    public EditableBayesianProbability( final double width,
                                        final double height,
                                        final double positionXNode,
                                        final double positionYNode ) {
        rectangle = new Rectangle( width,
                                   height );
        rectangle.setStrokeColor( ShapesUtils.RGB_STROKE_SHAPE );
        rectangle.setStrokeWidth( ShapesUtils.RGB_STROKE_WIDTH_SHAPE );

        add( rectangle );

        init( positionXNode,
              positionYNode );

        this.parentNode = Maps.newHashMap();
        this.porcentualOptions = Maps.newHashMap();
        this.porcentualValues = Maps.newHashMap();
        this.incomingNodes = Maps.newHashMap();
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
