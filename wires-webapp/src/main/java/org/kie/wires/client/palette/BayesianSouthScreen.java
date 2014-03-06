package org.kie.wires.client.palette;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.kie.wires.client.bayesian.factory.ProbabilityFactory;
import org.kie.wires.client.events.ClearEvent;
import org.kie.wires.client.events.ProbabilityEvent;
import org.uberfire.client.annotations.WorkbenchPartTitle;
import org.uberfire.client.annotations.WorkbenchPartView;
import org.uberfire.client.annotations.WorkbenchScreen;

import com.emitrom.lienzo.client.core.shape.Layer;
import com.emitrom.lienzo.client.widget.LienzoPanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

@Dependent
@WorkbenchScreen(identifier = "bayesianSouthScreen")
public class BayesianSouthScreen extends Composite implements RequiresResize {

    interface ViewBinder extends UiBinder<Widget, BayesianSouthScreen> {

    }

    private static ViewBinder uiBinder = GWT.create(ViewBinder.class);

    @UiField
    public SimplePanel variables;

    private LienzoPanel panel;

    private Layer layer;

    @Inject
    private Event<ProbabilityEvent> probabilityEvent;
    
    

    @PostConstruct
    public void init() {
        panel = new LienzoPanel(1200, 600);
        initWidget(panel);
        layer = new Layer();
        panel.getScene().add(layer);
    }

    @WorkbenchPartTitle
    @Override
    public String getTitle() {
        return "Template variables";
    }

    @WorkbenchPartView
    public IsWidget getView() {
        return this;
    }

    @Override
    public void onResize() {
        int height = getParent().getOffsetHeight();
        int width = getParent().getOffsetWidth();
        super.setPixelSize(width, height);
    }
    
    public void myResponseObserver(@Observes ProbabilityEvent event) {
        layer.removeAll();
        if(event.getBayesianProbabilityGrid() != null){
            layer.add(event.getBayesianProbabilityGrid());
        }else if(event.getVariable() != null){
    	    new ProbabilityFactory(event.getVariable(), probabilityEvent);
    	}
    	layer.draw();
    }
    
    public void clearPanel(@Observes ClearEvent event) {
        layer.removeAll();
        layer.draw();
    }


}