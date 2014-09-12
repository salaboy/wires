package org.kie.wires.core.client.properties;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.kie.wires.core.api.events.ShapeSelectedEvent;
import org.kie.wires.core.client.util.PropertyEditorUtil;
import org.uberfire.client.annotations.WorkbenchPartTitle;
import org.uberfire.client.annotations.WorkbenchPartView;
import org.uberfire.client.annotations.WorkbenchScreen;
import org.kie.uberfire.properties.editor.client.PropertyEditorWidget;
import org.kie.uberfire.properties.editor.model.PropertyEditorEvent;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.Widget;

@Dependent
@WorkbenchScreen(identifier = "WiresPropertiesScreen")
public class PropertiesScreen extends Composite implements RequiresResize {

    interface ViewBinder extends UiBinder<Widget, PropertiesScreen> {

    }

    private static ViewBinder uiBinder = GWT.create(ViewBinder.class);

    public static final String MY_ID = "WiresPropertiesScreen";

    @UiField
    FlowPanel panel;

    @Inject
    Event<PropertyEditorEvent> propertyEditorEvent;

    private PropertyEditorWidget propertyEditorWidget;

    @PostConstruct
    public void init() {
        super.initWidget(uiBinder.createAndBindUi(this));

        propertyEditorWidget = GWT.create(PropertyEditorWidget.class);
        panel.add(propertyEditorWidget);

    }

    @WorkbenchPartTitle
    @Override
    public String getTitle() {
        return "Properties Editor";
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

    public void showPropertyEditor(@Observes ShapeSelectedEvent selected) {
        propertyEditorWidget.handle(new PropertyEditorEvent(MY_ID,
                                                            PropertyEditorUtil.createProperties(selected.getShape())));

    }

}
