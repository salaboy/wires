package com.bayesian.network.client.templates;

import java.io.Serializable;

import com.emitrom.lienzo.client.core.shape.Group;
import com.emitrom.lienzo.client.core.shape.Rectangle;
import com.emitrom.lienzo.client.core.shape.Shape;
import com.emitrom.lienzo.client.core.shape.Text;

public class TemplateShape extends Group implements Serializable {

    private static final long serialVersionUID = -6555009991474610157L;
    private Rectangle bounding;
    private Shape<?> shape;
    private Text description;

    public TemplateShape() {

    }

    public Rectangle getBounding() {
        return bounding;
    }

    public void setBounding(Rectangle bounding) {
        add(bounding);
        this.bounding = bounding;
    }

    public Shape<?> getShape() {
        return shape;
    }

    public void setShape(Shape<?> shape) {
        add(shape);
        this.shape = shape;
    }

    public Text getDescription() {
        return description;
    }

    public void setDescription(Text description) {
        add(description);
        this.description = description;
    }

}
