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
package com.bayesian.network.client.factory;

import javax.enterprise.context.ApplicationScoped;

import com.bayesian.network.client.shapes.EditableBayesianNode;
import com.emitrom.lienzo.client.core.shape.Rectangle;
import com.emitrom.lienzo.client.core.shape.Shape;
import org.kie.wires.core.api.factories.ShapeDragProxy;
import org.kie.wires.core.api.factories.ShapeDragProxyCompleteCallback;
import org.kie.wires.core.api.factories.ShapeDragProxyPreviewCallback;
import org.kie.wires.core.api.factories.ShapeFactory;
import org.kie.wires.core.api.factories.ShapeGlyph;
import org.kie.wires.core.api.factories.categories.Category;
import org.kie.wires.core.api.shapes.WiresBaseShape;
import org.kie.wires.core.client.util.ShapesUtils;

@ApplicationScoped
public class BayesianNodePaletteFactory implements ShapeFactory<Rectangle> {

    private static final String DESCRIPTION = "Bayesian Node";

    private static final int SHAPE_SIZE_X = 40;
    private static final int SHAPE_SIZE_Y = 40;

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
        return BayesianNodeCategory.CATEGORY;
    }

    @Override
    public ShapeDragProxy<Rectangle> getDragProxy( final ShapeDragProxyPreviewCallback dragPreviewCallback,
                                                   final ShapeDragProxyCompleteCallback dragEndCallBack ) {
        final Rectangle rectangle = makeRectangle();

        return new ShapeDragProxy<Rectangle>() {
            @Override
            public Shape<Rectangle> getDragShape() {
                return rectangle;
            }

            @Override
            public void onDragPreview( final double x,
                                       final double y ) {
                dragPreviewCallback.callback( x,
                                              y );
            }

            @Override
            public void onDragComplete( final double x,
                                        final double y ) {
                dragEndCallBack.callback( x,
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
        return new EditableBayesianNode( makeRectangle() );
    }

    @Override
    public boolean builds( final WiresBaseShape shapeType ) {
        return shapeType instanceof EditableBayesianNode;
    }

    private Rectangle makeRectangle() {
        final Rectangle rectangle = new Rectangle( SHAPE_SIZE_X,
                                                   SHAPE_SIZE_Y,
                                                   5 );
        rectangle.setOffset( 0 - ( SHAPE_SIZE_X / 2 ),
                             0 - ( SHAPE_SIZE_Y / 2 ) )
                .setStrokeColor( ShapesUtils.RGB_STROKE_SHAPE )
                .setStrokeWidth( ShapesUtils.RGB_STROKE_WIDTH_SHAPE )
                .setFillColor( ShapesUtils.RGB_FILL_SHAPE )
                .setDraggable( false );
        return rectangle;
    }

}