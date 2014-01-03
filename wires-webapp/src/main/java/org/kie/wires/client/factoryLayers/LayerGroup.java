package org.kie.wires.client.factoryLayers;

import com.emitrom.lienzo.client.core.shape.Picture;
import com.emitrom.lienzo.client.core.shape.Rectangle;
import com.emitrom.lienzo.client.core.shape.Shape;
import com.emitrom.lienzo.client.core.shape.Text;

public class LayerGroup {

    private Rectangle bounding;

    private Picture deleteButton;

    private Picture viewButton;

    private Shape shape;

    private Text description;

    public Rectangle getBounding() {
        return bounding;
    }

    public void setBounding(Rectangle bounding) {
        this.bounding = bounding;
    }

    public Picture getDeleteButton() {
        return deleteButton;
    }

    public void setDeleteButton(Picture deleteButton) {
        this.deleteButton = deleteButton;
    }

    public Picture getViewButton() {
        return viewButton;
    }

    public void setViewButton(Picture viewButton) {
        this.viewButton = viewButton;
    }

    public Shape getShape() {
        return shape;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }

    public Text getDescription() {
        return description;
    }

    public void setDescription(Text description) {
        this.description = description;
    }

}
