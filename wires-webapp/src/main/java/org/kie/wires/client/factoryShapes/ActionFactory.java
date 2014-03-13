package org.kie.wires.client.factoryShapes;

import java.util.List;

import javax.enterprise.event.Event;

import org.kie.wires.client.events.ImageReadyEvent;
import org.kie.wires.core.api.events.ClearEvent;
import org.kie.wires.core.client.resources.AppResource;
import org.kie.wires.core.client.shapes.ActionShape;

import com.emitrom.lienzo.client.core.event.NodeMouseClickEvent;
import com.emitrom.lienzo.client.core.event.NodeMouseClickHandler;
import com.emitrom.lienzo.client.core.image.PictureLoadedHandler;
import com.emitrom.lienzo.client.core.shape.Picture;
import com.emitrom.lienzo.client.core.shape.Rectangle;
import com.emitrom.lienzo.shared.core.types.ColorName;
import com.google.common.collect.Lists;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Window;

public class ActionFactory {

    private static final int HEIGHT_BOUNDING = 20;
    private static final int WIDTH_BOUNDING = 20;
    private static final int HEIGHT_PICTURE = 16;
    private static final int WIDTH_PICTURE = 16;

    private List<ActionShape> actionsShape;

    private Event<ImageReadyEvent> imageReadyEvent;
    private Event<ClearEvent> clearEvent;

    private static final int totalActions = 1;

    public ActionFactory(Event<ClearEvent> clearEvent, Event<ImageReadyEvent> imageReadyEvent) {
        this.actionsShape = Lists.newArrayList();
        this.imageReadyEvent = imageReadyEvent;
        this.clearEvent = clearEvent;
        init();
    }

    public void init() {
        // TODO to do dynamic("x" and "y")
        addAction(10, 10, clearEvent(), AppResource.INSTANCE.images().clear());
        // put here the invocation to others actions and change totalActions
    }

    private void addAction(final int x, final int y, NodeMouseClickHandler clickEvent, ImageResource img) {
        final Rectangle boundingAction = this.getBoundingImage(clickEvent).setX(x).setY(y);

        new Picture(img, WIDTH_PICTURE, HEIGHT_PICTURE, false, null).setX(x).setY(y).onLoad(new PictureLoadedHandler() {
            @Override
            public void onPictureLoaded(Picture pic) {
                actionsShape.add(new ActionShape(pic, boundingAction));
                invokeEvent();
            }
        });
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

    private Rectangle getBoundingImage(NodeMouseClickHandler clickEvent) {
        Rectangle bounding = new Rectangle(WIDTH_BOUNDING, HEIGHT_BOUNDING).setX(0).setY(0)
                .setStrokeColor(ColorName.WHITE.getValue());
        bounding.addNodeMouseClickHandler(clickEvent);
        return bounding;
    }

    private void invokeEvent() {
        if (actionsShape.size() == totalActions) {
            imageReadyEvent.fire(new ImageReadyEvent(actionsShape));
        }
    }

}
