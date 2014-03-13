package org.kie.wires.client.actions;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.kie.wires.client.events.ImageReadyEvent;
import org.kie.wires.client.factoryShapes.ActionFactory;
import org.kie.wires.core.api.events.ClearEvent;
import org.kie.wires.core.client.shapes.ActionShape;
import org.uberfire.client.annotations.WorkbenchPartTitle;
import org.uberfire.client.annotations.WorkbenchPartView;
import org.uberfire.client.annotations.WorkbenchScreen;

import com.bayesian.network.api.utils.ShapeFactoryUtil;
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
@WorkbenchScreen(identifier = "WiresActionsScreen")
public class ActionsScreen extends Composite implements RequiresResize {

    interface ViewBinder extends UiBinder<Widget, ActionsScreen> {

    }

    private static ViewBinder uiBinder = GWT.create(ViewBinder.class);

    @UiField
    public SimplePanel actions;

    private LienzoPanel panel;

    private Layer layer;

    public static int accountLayers;

    @Inject
    private Event<ClearEvent> clearEvent;

    @Inject
    private Event<ImageReadyEvent> imageReadyEvent;

    @PostConstruct
    public void init() {
        panel = new LienzoPanel(ShapeFactoryUtil.WIDTH_PANEL, ShapeFactoryUtil.HEIGHT_PANEL);
        initWidget(panel);
        layer = new Layer();
        panel.getScene().add(layer);

        this.drawActions();
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

    private void drawActions() {
        new ActionFactory(clearEvent, imageReadyEvent);
    }

    public void readyEvent(@Observes ImageReadyEvent event) {
        for (ActionShape action : event.getActionsShape()) {
            layer.add(action);
        }
        layer.draw();
    }
}
