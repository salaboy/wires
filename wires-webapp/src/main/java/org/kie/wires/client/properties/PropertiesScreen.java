package org.kie.wires.client.properties;

import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.client.ui.WellForm;
import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import org.uberfire.client.annotations.WorkbenchPartTitle;
import org.uberfire.client.annotations.WorkbenchPartView;
import org.uberfire.client.annotations.WorkbenchScreen;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.Widget;
import javax.enterprise.event.Observes;
import org.kie.wires.client.events.ShapeSelectedEvent;

@Dependent
@WorkbenchScreen(identifier = "WiresPropertiesScreen")
public class PropertiesScreen extends Composite implements RequiresResize {

    interface ViewBinder extends UiBinder<Widget, PropertiesScreen> {

    }

    private static ViewBinder uiBinder = GWT.create(ViewBinder.class);

    @UiField
    public WellForm properties;

    @PostConstruct
    public void init() {
        super.initWidget(uiBinder.createAndBindUi(this));

    }

    @WorkbenchPartTitle
    @Override
    public String getTitle() {
        return "Properties";
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

    public void showProperties(@Observes ShapeSelectedEvent selected) {
        TextBox textBoxName = new TextBox();
        textBoxName.setText("Component = "+selected.getShape());
        double x = selected.getShape().getX();
        double y = selected.getShape().getY();
        properties.clear();
        TextBox textBoxX = new TextBox();
        TextBox textBoxY = new TextBox();
        textBoxX.setText("X = " + x);
        textBoxY.setText("Y = " + y);
        properties.add(textBoxName);
        properties.add(textBoxX);
        properties.add(textBoxY);
        
    }
}
