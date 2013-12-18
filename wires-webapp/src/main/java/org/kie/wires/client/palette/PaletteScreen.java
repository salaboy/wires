package org.kie.wires.client.palette;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.kie.wires.client.events.ShapeAddEvent;
import org.kie.wires.client.factoryShapes.ShapeCategory;
import org.kie.wires.client.factoryShapes.StencilBuilder;
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
@WorkbenchScreen(identifier = "WiresPaletteScreen")
public class PaletteScreen extends Composite implements RequiresResize {

    interface ViewBinder extends UiBinder<Widget, PaletteScreen> {

    }

    private static ViewBinder uiBinder = GWT.create(ViewBinder.class);

    @UiField
    public SimplePanel shapes;

    @UiField
    public SimplePanel connectors;

    @Inject
    private Event<ShapeAddEvent> shapeAddEvent;


    @PostConstruct
    public void init() {
        super.initWidget(uiBinder.createAndBindUi(this));
        this.drawStencil();
    }

    @WorkbenchPartTitle
    @Override
    public String getTitle() {
        return "Palette";
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

    private void drawStencil() {
        newAccordion(shapes, ShapeCategory.SHAPES);
        newAccordion(connectors, ShapeCategory.CONNECTORS);
    }

    private void newAccordion(SimplePanel panel, ShapeCategory category) {
        panel.add(new StencilBuilder(shapeAddEvent, category));
    }

}