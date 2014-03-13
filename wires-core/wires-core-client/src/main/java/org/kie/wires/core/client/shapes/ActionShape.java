package org.kie.wires.core.client.shapes;

import java.io.Serializable;

import com.emitrom.lienzo.client.core.shape.Group;
import com.emitrom.lienzo.client.core.shape.Picture;
import com.emitrom.lienzo.client.core.shape.Rectangle;

public class ActionShape extends Group implements Serializable {

    private static final long serialVersionUID = -6555009991474610157L;
    private Rectangle bounding;
    private Picture picture;

    public ActionShape() {

    }

    public ActionShape(Picture picture, Rectangle bounding) {
        add(picture);
        add(bounding);
    }

    public Rectangle getBounding() {
        return bounding;
    }

    public void setBounding(Rectangle bounding) {
        this.bounding = bounding;
    }

    public Picture getPicture() {
        return picture;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }

}
