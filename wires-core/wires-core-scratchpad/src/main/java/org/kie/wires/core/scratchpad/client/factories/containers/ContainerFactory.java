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
package org.kie.wires.core.scratchpad.client.factories.containers;

import javax.enterprise.context.ApplicationScoped;

import com.emitrom.lienzo.client.core.shape.Rectangle;
import com.emitrom.lienzo.client.core.shape.Shape;
import org.kie.wires.core.api.factories.ShapeDragProxy;
import org.kie.wires.core.api.factories.ShapeDragProxyCallback;
import org.kie.wires.core.api.factories.ShapeFactory;
import org.kie.wires.core.api.factories.ShapeGlyph;
import org.kie.wires.core.api.factories.categories.Category;
import org.kie.wires.core.api.shapes.WiresBaseShape;
import org.kie.wires.core.client.factories.categories.ContainerCategory;
import org.kie.wires.core.client.util.ShapesUtils;
import org.kie.wires.core.scratchpad.client.shapes.containers.WiresContainer;

@ApplicationScoped
public class ContainerFactory implements ShapeFactory<Rectangle> {

    private static final String DESCRIPTION = "Container";

    private static final int SHAPE_SIZE_X = 200;
    private static final int SHAPE_SIZE_Y = 200;

    @Override
    public ShapeGlyph<Rectangle> getGlyph() {
        final Rectangle rectangle = makeRectangle();

        return new ShapeGlyph<Rectangle>() {
            @Override
            public Shape<Rectangle> getShape() {
                return rectangle;
            }

            @Override
            public double getWidth() {
                return SHAPE_SIZE_X;
            }

            @Override
            public double getHeight() {
                return SHAPE_SIZE_Y;
            }
        };
    }

    @Override
    public String getShapeDescription() {
        return DESCRIPTION;
    }

    @Override
    public Category getCategory() {
        return ContainerCategory.CATEGORY;
    }

    @Override
    public ShapeDragProxy<Rectangle> getDragProxy( final ShapeDragProxyCallback callback ) {
        final Rectangle rectangle = makeRectangle();

        return new ShapeDragProxy<Rectangle>() {
            @Override
            public Shape<Rectangle> getDragShape() {
                return rectangle;
            }

            @Override
            public void onDragEnd( final double x,
                                   final double y ) {
                callback.callback( x,
                                   y );
            }

            @Override
            public int getWidth() {
                return SHAPE_SIZE_X;
            }

            @Override
            public int getHeight() {
                return SHAPE_SIZE_Y;
            }

        };
    }

    @Override
    public WiresBaseShape getShape() {
        return new WiresContainer( makeRectangle() );
    }

    @Override
    public boolean builds( final WiresBaseShape shapeType ) {
        return shapeType instanceof WiresContainer;
    }

    private Rectangle makeRectangle() {
        final Rectangle rectangle = new Rectangle( SHAPE_SIZE_X,
                                                   SHAPE_SIZE_Y,
                                                   10 );
        rectangle.setOffset( 0 - ( SHAPE_SIZE_X / 2 ),
                             0 - ( SHAPE_SIZE_Y / 2 ) )
                .setStrokeColor( ShapesUtils.RGB_STROKE_CONTAINER )
                .setStrokeWidth( ShapesUtils.RGB_STROKE_WIDTH_CONTAINER )
                .setFillColor( ShapesUtils.RGB_FILL_CONTAINER )
                .setAlpha( ShapesUtils.RGB_ALPHA_CONTAINER )
                .setDraggable( false );
        return rectangle;
    }

}