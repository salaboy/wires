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
package org.kie.wires.core.scratchpad.client.factories.dynamic;

import javax.enterprise.context.ApplicationScoped;

import com.emitrom.lienzo.client.core.shape.Arrow;
import com.emitrom.lienzo.client.core.shape.Shape;
import com.emitrom.lienzo.client.core.types.Point2D;
import com.emitrom.lienzo.shared.core.types.ArrowType;
import org.kie.wires.core.api.factories.ShapeDragProxy;
import org.kie.wires.core.api.factories.ShapeDragProxyCompleteCallback;
import org.kie.wires.core.api.factories.ShapeDragProxyPreviewCallback;
import org.kie.wires.core.api.factories.ShapeFactory;
import org.kie.wires.core.api.factories.ShapeGlyph;
import org.kie.wires.core.api.factories.categories.Category;
import org.kie.wires.core.api.shapes.WiresBaseShape;
import org.kie.wires.core.client.factories.categories.ConnectorCategory;
import org.kie.wires.core.client.util.ShapesUtils;
import org.kie.wires.core.scratchpad.client.shapes.dynamic.WiresArrow;

@ApplicationScoped
public class ArrowFactory implements ShapeFactory<Arrow> {

    private static final String DESCRIPTION = "Arrow";

    private static final int BASE_WIDTH = 10;
    private static final int HEAD_WIDTH = 20;
    private static final int ARROW_ANGLE = 45;
    private static final int BASE_ANGLE = 30;

    private static final int SHAPE_SIZE_X = 50;
    private static final int SHAPE_SIZE_Y = 50;

    @Override
    public ShapeGlyph<Arrow> getGlyph() {
        final Arrow arrow = makeArrow();

        return new ShapeGlyph<Arrow>() {
            @Override
            public Shape<Arrow> getShape() {
                return arrow;
            }

            @Override
            public double getWidth() {
                return SHAPE_SIZE_X + 10;
            }

            @Override
            public double getHeight() {
                return SHAPE_SIZE_Y + 10;
            }
        };
    }

    @Override
    public String getShapeDescription() {
        return DESCRIPTION;
    }

    @Override
    public Category getCategory() {
        return ConnectorCategory.CATEGORY;
    }

    @Override
    public ShapeDragProxy<Arrow> getDragProxy( final ShapeDragProxyPreviewCallback dragPreviewCallback,
                                               final ShapeDragProxyCompleteCallback dragEndCallBack ) {
        final Arrow arrow = makeArrow();

        return new ShapeDragProxy<Arrow>() {
            @Override
            public Shape<Arrow> getDragShape() {
                return arrow;
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
                return SHAPE_SIZE_X + 10;
            }

            @Override
            public int getHeight() {
                return SHAPE_SIZE_Y + 10;
            }

        };
    }

    @Override
    public WiresBaseShape getShape() {
        return new WiresArrow( makeArrow() );
    }

    @Override
    public boolean builds( final WiresBaseShape shapeType ) {
        return shapeType instanceof WiresArrow;
    }

    private Arrow makeArrow() {
        final Arrow arrow = new Arrow( new Point2D( 0 - ( SHAPE_SIZE_X / 2 ),
                                                    0 - ( SHAPE_SIZE_Y / 2 ) ),
                                       new Point2D( SHAPE_SIZE_X / 2,
                                                    SHAPE_SIZE_Y / 2 ),
                                       BASE_WIDTH,
                                       HEAD_WIDTH,
                                       ARROW_ANGLE,
                                       BASE_ANGLE,
                                       ArrowType.AT_END );
        arrow.setStrokeColor( ShapesUtils.RGB_STROKE_SHAPE )
                .setStrokeWidth( 1 )
                .setFillColor( "#ffff00" )
                .setDraggable( false );
        return arrow;
    }

}