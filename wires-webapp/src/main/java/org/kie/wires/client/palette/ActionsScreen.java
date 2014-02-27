package org.kie.wires.client.palette;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.kie.wires.client.events.ClearEvent;
import org.kie.wires.client.factoryShapes.ShapeFactoryUtil;
import org.kie.wires.client.resources.AppResource;
import org.uberfire.client.annotations.WorkbenchPartTitle;
import org.uberfire.client.annotations.WorkbenchPartView;
import org.uberfire.client.annotations.WorkbenchScreen;

import com.emitrom.lienzo.client.core.event.NodeMouseClickEvent;
import com.emitrom.lienzo.client.core.event.NodeMouseClickHandler;
import com.emitrom.lienzo.client.core.image.PictureLoadedHandler;
import com.emitrom.lienzo.client.core.shape.Group;
import com.emitrom.lienzo.client.core.shape.Layer;
import com.emitrom.lienzo.client.core.shape.Picture;
import com.emitrom.lienzo.client.core.shape.Rectangle;
import com.emitrom.lienzo.client.widget.LienzoPanel;
import com.emitrom.lienzo.shared.core.types.ColorName;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
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

    private Group group;

    private Layer layer;

    private static final int X = 0;

    private static final int Y = 5;

    public static int accountLayers;

    private Picture clearAction;

    @Inject
    private Event<ClearEvent> clearEvent;

    @PostConstruct
    public void init() {
        accountLayers = 0;
        super.initWidget(uiBinder.createAndBindUi(this));
        panel = new LienzoPanel(ShapeFactoryUtil.WIDTH_PANEL, ShapeFactoryUtil.HEIGHT_PANEL);
        layer = new Layer();
        panel.add(layer);
        group = new Group();
        group.setX(X).setY(Y);
        layer.add(group);
        actions.add(panel);
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
        PictureLoadedHandler onLoad = new PictureLoadedHandler() {
            public void onPictureLoaded(Picture picture) {
                group.add(picture);
                layer.draw();
            }
        };
        clearOption(onLoad);

    }

    private void clearOption(PictureLoadedHandler onLoad) {
        Rectangle boundingAction = new Rectangle(20, 20).setX(0).setY(0).setStrokeColor(ColorName.WHITE.getValue());
        group.add(boundingAction);
        clearAction = new Picture(AppResource.INSTANCE.images().clear(), 16, 16, true, null);
        clearAction.setDraggable(true).setX(1).setY(1).onLoad(onLoad);
        clearAction.addNodeMouseClickHandler(clearEvent());
        boundingAction.addNodeMouseClickHandler(clearEvent());
    }

    private NodeMouseClickHandler clearEvent() {
        return new NodeMouseClickHandler() {
            @Override
            public void onNodeMouseClick(NodeMouseClickEvent event) {
                if (Window.confirm("Are you sure to clean the canvas?")) {
                    clearEvent.fire(new ClearEvent());
                }
            }
        };
    }

}
