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
package org.kie.wires.client.factories.dynamic;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import com.emitrom.lienzo.client.core.shape.Rectangle;
import com.emitrom.lienzo.client.core.shape.Shape;
import org.kie.wires.client.factories.categories.ShapeCategory;
import org.kie.wires.core.api.categories.Category;
import org.kie.wires.core.api.events.ShapeDragCompleteEvent;
import org.kie.wires.core.api.factories.ShapeDragProxy;
import org.kie.wires.core.api.factories.ShapeDragProxyCallback;
import org.kie.wires.core.api.factories.ShapeFactory;
import org.kie.wires.core.api.shapes.WiresBaseShape;
import org.kie.wires.core.client.shapes.dynamic.WiresRectangle;
import org.kie.wires.core.client.util.ShapesUtils;

@ApplicationScoped
public class RectangleFactory implements ShapeFactory<Rectangle> {

    private static final String DESCRIPTION = "Box";

    private static final int SHAPE_SIZE_X = 70;
    private static final int SHAPE_SIZE_Y = 40;

    @Inject
    private Event<ShapeDragCompleteEvent> shapeDragCompleteEvent;

    @Override
    public Shape<Rectangle> getGlyph() {
        final Rectangle rectangle = new Rectangle( 40,
                                                   40 );
        setAttributes( rectangle,
                       5,
                       5 );
        return rectangle;
    }

    @Override
    public String getIdentifier() {
        return getClass().getName();
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
    public ShapeDragProxy<Rectangle> getDragProxy( final ShapeDragProxyCallback callback ) {
        final Rectangle proxy = new Rectangle( 90,
                                               40 );
        setAttributes( proxy,
                       5,
                       5 );

        return new ShapeDragProxy<Rectangle>() {
            @Override
            public Shape<Rectangle> getDragShape() {
                return proxy;
            }

            @Override
            public void onDragEnd( final int x,
                                   final int y ) {
                callback.callback( getIdentifier(),
                                   x,
                                   y );
            }

            @Override
            public int getHeight() {
                return 50;
            }

            @Override
            public int getWidth() {
                return 100;
            }

        };
    }

    @Override
    public WiresBaseShape getShape() {
        return new WiresRectangle( SHAPE_SIZE_X,
                                   SHAPE_SIZE_Y );
    }

    @Override
    public int getShapeOffsetX() {
        return SHAPE_SIZE_X / 2;
    }

    @Override
    public int getShapeOffsetY() {
        return SHAPE_SIZE_Y / 2;
    }

    private void setAttributes( final Rectangle floatingShape,
                                final double x,
                                final double y ) {
        floatingShape.setX( x )
                .setY( y )
                .setStrokeColor( ShapesUtils.RGB_STROKE_SHAPE )
                .setStrokeWidth( ShapesUtils.RGB_STROKE_WIDTH_SHAPE )
                .setFillColor( ShapesUtils.RGB_FILL_SHAPE )
                .setDraggable( false );
    }

}