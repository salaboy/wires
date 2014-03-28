package org.kie.wires.client.actions;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.jboss.errai.ioc.client.container.SyncBeanManager;
import org.uberfire.client.annotations.WorkbenchPartTitle;
import org.uberfire.client.annotations.WorkbenchPartView;
import org.uberfire.client.annotations.WorkbenchScreen;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

@Dependent
@WorkbenchScreen(identifier = "WiresActionsScreen")
public class ActionsScreen extends Composite implements RequiresResize {

    interface ViewBinder extends UiBinder<Widget, ActionsScreen> {

    }

    private static ViewBinder uiBinder = GWT.create(ViewBinder.class);

    @UiField
    public SimplePanel actions;

    public static int accountLayers;

    @Inject
    private SyncBeanManager iocManager;

    @PostConstruct
    public void init() {
        initWidget(uiBinder.createAndBindUi(this));
        actions.add(iocManager.lookupBean(ActionsGroup.class).getInstance());
    }

    @WorkbenchPartTitle
    @Override
    public String getTitle() {
        return "Actions";
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

}
