package org.kie.wires.client.layers;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.bayesian.network.client.events.LayerEvent;
import com.bayesian.parser.client.model.BayesVariable;
import com.emitrom.lienzo.client.core.shape.Shape;
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
import org.kie.wires.core.api.events.ShapeAddedEvent;
import org.kie.wires.core.api.factories.ShapeFactory;
import org.kie.wires.core.client.factories.ShapeFactoryCache;
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

    @Inject
    private ShapeFactoryCache factoriesCache;

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
    public void myResponseObserver( @Observes ShapeAddedEvent shapeAddedEvent ) {
        LayersGroup.accountLayers += 1;

        //Get a concrete Shape
        Shape shape = null;
        final String shapeDescription = shapeAddedEvent.getShape();
        for ( ShapeFactory factory : factoriesCache.getShapeFactories() ) {
            if ( factory.getShapeDescription().equals( shapeDescription ) ) {
                shape = factory.getGlyph();
            }
        }

        if ( shape == null ) {
            return;
        }

        layersGroup.buildNewLayer( shape,
                                   null,
                                   LayersGroup.accountLayers );
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