package com.bayesian.network.api.factory;


import org.kie.wires.core.client.resources.AppImages;

import com.bayesian.network.api.utils.ShapeFactoryUtil;
import com.emitrom.lienzo.client.core.shape.Group;
import com.emitrom.lienzo.client.core.shape.Rectangle;
import com.emitrom.lienzo.client.core.shape.Shape;
import com.emitrom.lienzo.client.core.shape.Text;
import com.google.gwt.core.client.GWT;

public abstract class LayerFactory<T extends Shape<T>> {

    private static final int LAYERS_BY_ROW = 1;
    
    private AppImages resource = GWT.create( AppImages.class );

    protected abstract void drawBoundingBox(Group group, String template);

    protected abstract Shape<T> drawLayer();

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
        return text;
    }
    
    protected void createOptions(final int x, final int y){
        /*new Picture(resource.delete(), false).onLoad(new PictureLoadedHandler() {
            @Override  
            public void onPictureLoaded(Picture picture) {
                picture.setX(x);
                picture.setY(y);
                layer.add(picture);
                layer.draw();
                  
            }  
        });  
        
        new Picture(resource.view(), false).onLoad(new PictureLoadedHandler() {
            @Override  
            public void onPictureLoaded(Picture picture) {
                picture.setX(x - 19);
                picture.setY(y);
                layer.add(picture);
                layer.draw();
                  
            }  
        });  */
    }

}
