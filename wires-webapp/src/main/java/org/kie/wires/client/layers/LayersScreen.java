package org.kie.wires.client.layers;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.bayesian.network.client.events.LayerEvent;
import com.bayesian.parser.client.model.BayesVariable;
import com.emitrom.lienzo.client.core.shape.Circle;
import com.emitrom.lienzo.client.core.shape.Line;
import com.emitrom.lienzo.client.core.shape.Rectangle;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import org.jboss.errai.ioc.client.container.SyncBeanManager;
import org.kie.wires.core.api.events.ClearEvent;
import org.kie.wires.core.api.events.ReadyShape;
import org.uberfire.client.annotations.WorkbenchPartTitle;
import org.uberfire.client.annotations.WorkbenchPartView;
import org.uberfire.client.annotations.WorkbenchScreen;

@Dependent
@WorkbenchScreen(identifier = "WiresLayersScreen")
public class LayersScreen extends Composite implements RequiresResize {

    interface ViewBinder extends UiBinder<Widget, LayersScreen> {

    }

    private static ViewBinder uiBinder = GWT.create( ViewBinder.class );

    @UiField
    public SimplePanel layers;

    @Inject
    private SyncBeanManager iocManager;

    @Inject
    private LayersGroup layersGroup;

    @PostConstruct
    public void init() {
        initWidget( uiBinder.createAndBindUi( this ) );
        layers.add( iocManager.lookupBean( LayersGroup.class ).getInstance() );
    }

    @WorkbenchPartTitle
    @Override
    public String getTitle() {
        return "Layers";
    }

    @WorkbenchPartView
    public IsWidget getView() {
        return this;
    }

    @Override
    public void onResize() {
        int height = getParent().getOffsetHeight();
        int width = getParent().getOffsetWidth();
        super.setPixelSize( width, height );
    }

    /**
     * TODO the events can not be within the SimplePanel because they are re-called
     */
    public void myResponseObserver( @Observes ReadyShape readyShape ) {
        LayersGroup.accountLayers += 1;
        /* refactor this to be generic as well */
        if ( readyShape.getShape().equals( "WiresRectangle" ) ) {
            layersGroup.buildNewLayer( new Rectangle( 40,
                                                      30 ),
                                       null,
                                       LayersGroup.accountLayers );

        } else if ( readyShape.getShape().equals( "WiresCircle" ) ) {
            layersGroup.buildNewLayer( new Circle( 15 ),
                                       null,
                                       LayersGroup.accountLayers );

        } else if ( readyShape.getShape().equals( "WiresLine" ) ) {
            layersGroup.buildNewLayer( new Line( 0,
                                                 0,
                                                 30,
                                                 30 ),
                                       null,
                                       LayersGroup.accountLayers );
        }
        layersGroup.drawLayer();
    }

    public void drawNamesNode( @Observes LayerEvent layerEvent ) {
        if ( LayersGroup.accountNodes == null ) {
            LayersGroup.accountNodes = 0;
            LayersGroup.accountLayers = 0;
        }
        for ( BayesVariable node : layerEvent.getNodes() ) {
            LayersGroup.accountNodes += 1;
            layersGroup.buildNewLayer( null,
                                       node,
                                       LayersGroup.accountNodes );
        }
        layersGroup.drawLayer();
    }

    public void clearPanel( @Observes ClearEvent event ) {
        layersGroup.clearPanel();
    }

}