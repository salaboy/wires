package com.bayesian.network.client.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.enterprise.event.Event;

import com.bayesian.network.client.events.LayerEvent;
import com.bayesian.network.client.events.ProbabilityEvent;
import com.bayesian.network.client.events.ReadyEvent;
import com.bayesian.network.client.shapes.EditableBayesianNode;
import com.bayesian.network.client.utils.BayesianUtils;
import com.bayesian.parser.client.model.BayesNetwork;
import com.bayesian.parser.client.model.BayesVariable;
import com.bayesian.parser.client.service.BayesianService;
import com.emitrom.lienzo.client.core.shape.Rectangle;
import com.emitrom.lienzo.client.core.shape.Text;
import com.emitrom.lienzo.shared.core.types.Color;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gwt.user.client.Window;
import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.common.client.api.ErrorCallback;
import org.jboss.errai.common.client.api.RemoteCallback;
import org.kie.wires.core.client.shapes.ProgressBar;

public class BayesianFactory extends BaseFactory {

    private Caller<BayesianService> bayesianService;
    private Event<LayerEvent> layerEvent;
    private Event<ProbabilityEvent> probabilityEvent;
    private Event<ReadyEvent> readyEvent;
    private List<BayesVariable> nodes;
    private String[][] colors;

    private List<EditableBayesianNode> bayesianNodes = new ArrayList<EditableBayesianNode>();

    public BayesianFactory( final Caller<BayesianService> bayesianService,
                            final String xml03File,
                            final Event<LayerEvent> layerEvent,
                            final Event<ProbabilityEvent> probabilityEvent,
                            final Event<ReadyEvent> readyEvent ) {
        this.bayesianService = bayesianService;
        this.layerEvent = layerEvent;
        this.probabilityEvent = probabilityEvent;
        this.readyEvent = readyEvent;
        init( xml03File );
    }

    public void init( final String xml03File ) {
        clearScreen();
        buildBayesNetworkFromXML( xml03File );
    }

    private void buildBayesNetworkFromXML( final String xml03File ) {
        bayesianService.call( new RemoteCallback<BayesNetwork>() {
                                  @Override
                                  public void callback( final BayesNetwork response ) {
                                      nodes = Lists.newArrayList();
                                      for ( BayesVariable bay : response.getNodos() ) {
                                          drawBayesianNode( bay );
                                      }
                                      layerEvent.fire( new LayerEvent( nodes ) );
                                      //readyEvent.fire(new ReadyEvent(bayesianNodes));

                                  }
                              }, new ErrorCallback<Object>() {

                                  @Override
                                  public boolean error( Object message,
                                                        Throwable throwable ) {
                                      Window.alert( "Sorry.. the " + xml03File + " could not be read.." );
                                      ProgressBar.setInfinite( false );
                                      return false;
                                  }
                              }
                            ).buildXml03( BayesianUtils.XML3_RESOURCE_PATH + xml03File );
    }

    private void drawBayesianNode( BayesVariable node ) {
        colors = BayesianUtils.getNodeColors();
        double position[][] = node.getPosition();
        int positionX = (int) ( BayesianUtils.POSITION_X_BASE + Math.round( position[ 0 ][ 0 ] ) );
        int positionY = (int) ( BayesianUtils.POSITION_Y_BASE + Math.round( position[ 0 ][ 1 ] ) );
        String fillNodeColor = colors[ 0 ][ 0 ];

        EditableBayesianNode bayesianNode = new EditableBayesianNode( BayesianUtils.WIDTH_NODE,
                                                                      BayesianUtils.HEIGHT_NODE,
                                                                      positionX,
                                                                      positionY,
                                                                      fillNodeColor );

        this.setHeader( node,
                        bayesianNode );
        this.setPorcentualBar( node,
                               bayesianNode );

        bayesianNode.buildNode();

        nodes.add( node );

        //TODO adding one by one to the canvas. We must see the performance
        bayesianNodes.clear();
        bayesianNodes.add( bayesianNode );
        readyEvent.fire( new ReadyEvent( bayesianNodes ) );

    }

    private void setHeader( BayesVariable node,
                            EditableBayesianNode bayesianNode ) {
        bayesianNode.setHeader( new Rectangle( bayesianNode.getRectangle().getWidth(), BayesianUtils.HEIGHT_HEADER ) );
        bayesianNode.getHeader().setFillColor( colors[ 0 ][ 1 ] );
        bayesianNode.getHeader().setX( bayesianNode.getHeader().getX() + 6 );
        bayesianNode.setTextHeader( drawText( node.getName(), BayesianUtils.FONT_SIZE_HEADER_NODE,
                                              BayesianUtils.LABEL_POSITION_X_DEFAULT + 6, BayesianUtils.LABEL_POSITION_Y_DEFAULT ) );
    }

    private void setPorcentualBar( BayesVariable node,
                                   EditableBayesianNode bayesianNode ) {
        String fillColor = colors[ 0 ][ 1 ];
        int widthFill;
        int positionY = 18;
        positionY = ( node.getOutcomes().size() > 3 ) ? positionY - 10 : positionY;
        String borderColor = fillColor;

        List<Rectangle> componentsProgressBar = Lists.newArrayList();
        Text labelPorcentual;
        Map<Text, List<Rectangle>> porcentualsBar = Maps.newHashMap();
        for ( int i = 0; i < node.getOutcomes().size(); i++ ) {
            // Porcentual bar
            positionY += 14;
            labelPorcentual = this.drawText( node.getOutcomes().get( i ), BayesianUtils.FONT_SIZE_PORCENTUAL_BAR,
                                             BayesianUtils.LABEL_POSITION_X_DEFAULT, positionY + 7 );
            componentsProgressBar.add( this.drawComponent( Color.rgbToBrowserHexColor( 255, 255, 255 ),
                                                           BayesianUtils.POSITION_X_PORCENTUAL_BAR, positionY, BayesianUtils.WIDTH_PORCENTUAL_BAR,
                                                           BayesianUtils.HEIGHT_PORCENTUAL_BAR, borderColor, 3 ) );
            // fill bar
            widthFill = calculatePorcentage( node.getProbabilities(), BayesianUtils.WIDTH_PORCENTUAL_BAR, i );
            componentsProgressBar.add( drawComponent( fillColor, BayesianUtils.POSITION_X_PORCENTUAL_BAR, positionY, widthFill,
                                                      BayesianUtils.HEIGHT_PORCENTUAL_BAR, borderColor, 0 ) );
            bayesianNode.getPorcentualsBar().put( labelPorcentual, componentsProgressBar );

            porcentualsBar.put( labelPorcentual, componentsProgressBar );
        }
        bayesianNode.setPorcentualBars( porcentualsBar );
    }

    private int calculatePorcentage( double probabilities[][],
                                     int maxWidthFill,
                                     int position ) {
        double porcentual = 0;
        if ( position == 0 ) {
            porcentual = probabilities[ 0 ][ 0 ];
        } else if ( position == 1 ) {
            porcentual = probabilities[ 0 ][ 1 ];
        }
        porcentual *= 100;
        return (int) ( ( porcentual * maxWidthFill ) / 100 );
    }

    private void clearScreen() {
        probabilityEvent.fire( new ProbabilityEvent() );
    }

}