package org.kie.wires.client.factoryLayers;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.event.Event;

import org.kie.wires.client.events.DrawnShapesEvent;
import org.kie.wires.client.factoryShapes.ShapeFactoryUtil;

import com.emitrom.lienzo.client.core.event.NodeMouseClickEvent;
import com.emitrom.lienzo.client.core.event.NodeMouseClickHandler;
import com.emitrom.lienzo.client.core.event.NodeMouseDownHandler;
import com.emitrom.lienzo.client.core.image.PictureLoadedHandler;
import com.emitrom.lienzo.client.core.shape.Group;
import com.emitrom.lienzo.client.core.shape.Layer;
import com.emitrom.lienzo.client.core.shape.Picture;
import com.emitrom.lienzo.client.core.shape.Rectangle;
import com.emitrom.lienzo.client.core.shape.Shape;
import com.emitrom.lienzo.client.core.shape.Text;
import com.google.gwt.core.client.GWT;

public abstract class LayerFactory<T extends Shape<T>> {

    private static final int LAYERS_BY_ROW = 1;

    private ResourcesLayers resource = GWT.create(ResourcesLayers.class);

    protected abstract void drawBoundingBox(Group group, Layer layer, Event<DrawnShapesEvent> drawnShapesEvent,
            int positionLayer);

    protected abstract Shape<T> drawLayer();

    protected abstract void addShapeHandlers(Shape<T> shape, Group group);

    protected abstract NodeMouseDownHandler getNodeMouseDownEvent(final Group group);

    protected abstract void addBoundingHandlers(Rectangle boundingBox, Group group);

    protected static Map<Integer, LayerGroup> listLayerGroup;

    protected static Layer layer;

    protected LayerGroup layerGroup;

    public LayerFactory() {
    }

    public LayerFactory(Layer l) {
        if (listLayerGroup == null) {
            listLayerGroup = new HashMap<Integer, LayerGroup>();
        }
        layer = l;
    }

    protected Rectangle createBoundingBox(Group group, int layers) {
        final Rectangle boundingBox = new Rectangle(ShapeFactoryUtil.WIDTH_BOUNDING_LAYER,
                ShapeFactoryUtil.HEIGHT_BOUNDING_LAYER);
        boundingBox.setX(getXBoundingBox()).setY(this.getYBoundingBox(layers))
                .setStrokeColor(ShapeFactoryUtil.RGB_STROKE_BOUNDING).setStrokeWidth(1)
                .setFillColor(ShapeFactoryUtil.RGB_FILL_BOUNDING).setDraggable(false);
        group.add(boundingBox);
        return boundingBox;
    }

    private double getXBoundingBox() {
        return 0;
    }

    private double getYBoundingBox(int layers) {
        return calculateY(layers);
    }

    protected double getYText(int layers) {
        return 20 + calculateY(layers);
    }

    protected int calculateX(int layers) {
        int x = layers > 1 ? (this.getPositionInRow(layers) - 1) : 0;
        return x > 0 ? (ShapeFactoryUtil.HEIGHT_BOUNDING_LAYER * x) : 0;
    }

    protected int calculateY(int layers) {
        int y = layers > 1 ? layers - 1 : 0;
        return y * ShapeFactoryUtil.HEIGHT_BOUNDING_LAYER;
    }

    private int getPositionInRow(int shapes) {
        return (shapes - this.layersByRow()) >= 1 ? (shapes - (this.layersByRow() * getRow(shapes))) : shapes;
    }

    private int getRow(int layers) {
        return Math.round((layers * ShapeFactoryUtil.WIDTH_BOUNDING_LAYER) / ShapeFactoryUtil.WIDTH_STENCIL);
    }

    private int layersByRow() {
        return LAYERS_BY_ROW;
    }

    protected Text createDescription(String description, int shapes) {
        Text text = new Text(description, ShapeFactoryUtil.FONT_FAMILY_DESCRIPTION, ShapeFactoryUtil.FONT_SIZE_DESCRIPTION);
        text.setX(45).setY(this.getYText(shapes)).setFillColor(ShapeFactoryUtil.RGB_TEXT_DESCRIPTION);
        layerGroup.setDescription(text);
        return text;
    }

    protected void createOptions(final Layer layer, final int x, final int y, final Event<DrawnShapesEvent> drawnShapesEvent,
            final int positionLayer, final Group group, final LayerGroup layerGroup) {
        new Picture(resource.delete(), true).onLoad(new PictureLoadedHandler() {
            @Override
            public void onPictureLoaded(Picture deletePicture) {
                deletePicture.setX(x);
                deletePicture.setY(y);
                layer.add(deletePicture);

                deletePicture.addNodeMouseClickHandler(new NodeMouseClickHandler() {
                    @Override
                    public void onNodeMouseClick(NodeMouseClickEvent event) {
                        DrawnShapesEvent ev = new DrawnShapesEvent(positionLayer);
                        ev.setListLayerGroup(listLayerGroup);
                        drawnShapesEvent.fire(ev);
                    }
                });
                layerGroup.setDeleteButton(deletePicture);

            }
        });

        new Picture(resource.view(), true).onLoad(new PictureLoadedHandler() {
            @Override
            public void onPictureLoaded(Picture picture) {
                picture.setX(x - 19);
                picture.setY(y);
                layer.add(picture);

                // picture.addNodeMouseClickHandler(new NodeMouseClickHandler()
                // {
                // @Override
                // public void onNodeMouseClick(NodeMouseClickEvent event) {
                // GWT.log("view ");
                // }
                // });
                layerGroup.setViewButton(picture);

                layer.draw();
                // group.add(picture);

            }
        });

    }

}
