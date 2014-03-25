package org.kie.wires.core.client.shapes;

import java.io.Serializable;

import com.emitrom.lienzo.client.core.shape.Group;
import com.emitrom.lienzo.client.core.shape.Rectangle;
import com.emitrom.lienzo.client.core.shape.Shape;
import com.emitrom.lienzo.client.core.shape.Text;

public class PaletteShape extends Group implements Serializable {

    private static final long serialVersionUID = -6555009991474610157L;
    private Rectangle bounding;
    private Shape<?> shape;
    private Text description;

    public PaletteShape() {

    }

    public void build() {
        add(bounding);
        add(shape);
        add(description);
    }

    public Rectangle getBounding() {
        return bounding;
    }

    public void setBounding(Rectangle bounding) {
        this.bounding = bounding;
    }

    public Shape<?> getShape() {
        return shape;
    }

    public void setShape(Shape<?> shape) {
        this.shape = shape;
    }

    public Text getDescription() {
        return description;
    }

    public void setDescription(Text description) {
        this.description = description;
    }

}
