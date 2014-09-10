/*
 * Copyright 2014 JBoss Inc
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
package org.kie.wires.client.factoryShapes;

import javax.enterprise.context.ApplicationScoped;

import com.emitrom.lienzo.client.core.shape.Circle;
import com.emitrom.lienzo.client.core.shape.Shape;
import org.kie.wires.client.factoryShapes.categories.ShapeCategory;
import org.kie.wires.core.api.categories.Category;
import org.kie.wires.core.api.factories.ShapeDragProxy;
import org.kie.wires.core.api.factories.ShapeDragProxyCallback;
import org.kie.wires.core.api.factories.ShapeFactory;
import org.kie.wires.core.api.shapes.WiresBaseGroupShape;
import org.kie.wires.core.client.shapes.WiresCircle;
import org.kie.wires.core.client.util.ShapesUtils;

@ApplicationScoped
public class CircleFactory implements ShapeFactory<Circle> {

    private static final String DESCRIPTION = "Circle";

    @Override
    public Shape<Circle> getGlyph() {
        final Circle circle = new Circle( 15 );
        setAttributes( circle,
                       25,
                       25 );
        circle.setDraggable( false );
        return circle;
    }

    @Override
    public String getShapeDescription() {
        return DESCRIPTION;
    }

    @Override
    public Category getCategory() {
        return ShapeCategory.CATEGORY;
    }

    @Override
    public ShapeDragProxy<Circle> getDragProxy( final ShapeDragProxyCallback callback ) {
        final Circle proxy = new Circle( 20 );
        setAttributes( proxy,
                       25,
                       25 );
        return new ShapeDragProxy<Circle>() {
            @Override
            public Shape<Circle> getDragShape() {
                return proxy;
            }

            @Override
            public void onDragEnd( final int x,
                                   final int y ) {
                callback.callback( DESCRIPTION,
                                   x,
                                   y );
            }

            @Override
            public int getHeight() {
                return 50;
            }

            @Override
            public int getWidth() {
                return 50;
            }

        };
    }

    @Override
    public WiresBaseGroupShape getShape() {
        return new WiresCircle( 0,
                                0,
                                20 );
    }

    @Override
    public int getShapeOffsetX() {
        return 0;
    }

    @Override
    public int getShapeOffsetY() {
        return 0;
    }

    private void setAttributes( final Circle circle,
                                final double x,
                                final double y ) {
        circle.setX( x )
                .setY( y )
                .setStrokeColor( ShapesUtils.RGB_STROKE_SHAPE )
                .setStrokeWidth( ShapesUtils.RGB_STROKE_WIDTH_SHAPE )
                .setFillColor( ShapesUtils.RGB_FILL_SHAPE )
                .setDraggable( false );
    }

}
