/*
 * Copyright 2012 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kie.wires.core.api.factories;

import com.emitrom.lienzo.client.core.shape.Shape;
import org.kie.wires.core.api.categories.Category;
import org.kie.wires.core.api.shapes.WiresBaseGroupShape;

/**
 * Factory for building shapes available for authoring.
 */
public interface ShapeFactory<T extends Shape<T>> {

    /**
     * Get a glyph to represent the Shape. Used by the Palette Screen and Layers Screen
     * @return
     */
    Shape<T> getGlyph();

    /**
     * Get a proxy used during and at the end of a drag operation
     * @param callback
     * @return
     */
    ShapeDragProxy<T> getDragProxy( final ShapeDragProxyCallback callback );

    /**
     * Get a Shape to be created on the Canvas (usually at the end of a drag operation)
     * @return
     */
    WiresBaseGroupShape getShape();

    /**
     * Get X offset. Used to centre the "dropped" Shape at the mouse pointer position. For example a Circle with
     * centre (0, 0) and radius R is automatically centred at the mouse pointer position because its bounding extent
     * is (-R, -R)->(R, R) whereas a Rectangle (0, 0)->(100, 80) needs to be offset (-50, -40) to be centred.
     * @return
     */
    int getShapeOffsetX();

    /**
     * Get Y offset. Used to centre the "dropped" Shape at the mouse pointer position. For example a Circle with
     * centre (0, 0) and radius R is automatically centred at the mouse pointer position because its bounding extent
     * is (-R, -R)->(R, R) whereas a Rectangle (0, 0)->(100, 80) needs to be offset (-50, -40) to be centred.
     * @return
     */
    int getShapeOffsetY();

    /**
     * Get description of Shape
     * @return
     */
    String getShapeDescription();

    /**
     * Get category to which Shape belongs
     * @return
     */
    Category getCategory();

}
