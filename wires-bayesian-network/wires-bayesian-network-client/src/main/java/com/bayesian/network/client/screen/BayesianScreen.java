package com.bayesian.network.client.screen;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.bayesian.network.client.events.BayesianTemplateSelectedEvent;
import com.bayesian.network.client.events.RenderBayesianNetworkEvent;
import com.bayesian.network.client.factory.BayesianFactory;
import com.bayesian.network.client.shapes.EditableBayesianNode;
import com.google.gwt.user.client.ui.IsWidget;
import org.kie.wires.core.client.canvas.WiresCanvas;
import org.uberfire.client.annotations.WorkbenchPartTitle;
import org.uberfire.client.annotations.WorkbenchPartView;
import org.uberfire.client.annotations.WorkbenchScreen;

@Dependent
@WorkbenchScreen(identifier = "BayesianScreen")
public class BayesianScreen extends WiresCanvas {

    @Inject
    private BayesianFactory factory;

    public void onBayesianEvent( @Observes BayesianTemplateSelectedEvent event ) {
        factory.init( event.getTemplate() );
    }

    public void onReadyEvent( @Observes RenderBayesianNetworkEvent event ) {
        clear();
        for ( EditableBayesianNode node : event.getBayesianNodes() ) {
            addShape( node );
        }
    }

    @WorkbenchPartTitle
    @Override
    public String getTitle() {
        return "Bayesian Network";
    }

    @WorkbenchPartView
    public IsWidget getView() {
        return this;
    }

}
