package com.bayesian.network.client.variables;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import org.jboss.errai.ioc.client.container.SyncBeanManager;
import org.uberfire.client.annotations.WorkbenchPartTitle;
import org.uberfire.client.annotations.WorkbenchPartView;
import org.uberfire.client.annotations.WorkbenchScreen;

@Dependent
@WorkbenchScreen(identifier = "bayesianSouthScreen")
public class BayesianSouthScreen extends Composite implements RequiresResize {

    interface ViewBinder extends UiBinder<Widget, BayesianSouthScreen> {

    }

    private static ViewBinder uiBinder = GWT.create( ViewBinder.class );

    @UiField
    public SimplePanel variables;

    @Inject
    private SyncBeanManager iocManager;

    @PostConstruct
    public void init() {
        initWidget( uiBinder.createAndBindUi( this ) );
        variables.add( iocManager.lookupBean( PorcentualsGroup.class ).getInstance() );
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
        super.setPixelSize( width, height );
    }

}