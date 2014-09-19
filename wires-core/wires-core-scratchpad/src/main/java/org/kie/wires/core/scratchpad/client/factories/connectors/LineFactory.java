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
package org.kie.wires.core.scratchpad.client.factories.connectors;

import javax.enterprise.context.ApplicationScoped;

import com.emitrom.lienzo.client.core.shape.Line;
import com.emitrom.lienzo.client.core.shape.Shape;
import com.emitrom.lienzo.shared.core.types.LineCap;
import org.kie.wires.core.api.factories.ShapeDragProxy;
import org.kie.wires.core.api.factories.ShapeDragProxyCompleteCallback;
import org.kie.wires.core.api.factories.ShapeDragProxyPreviewCallback;
import org.kie.wires.core.api.factories.ShapeFactory;
import org.kie.wires.core.api.factories.ShapeGlyph;
import org.kie.wires.core.api.factories.categories.Category;
import org.kie.wires.core.api.shapes.WiresBaseShape;
import org.kie.wires.core.client.factories.categories.ConnectorCategory;
import org.kie.wires.core.client.util.ShapesUtils;
import org.kie.wires.core.scratchpad.client.shapes.connectors.WiresLine;

@ApplicationScoped
public class LineFactory implements ShapeFactory<Line> {

    private static final String DESCRIPTION = "Line";

    private static final int SHAPE_SIZE_X = 40;
    private static final int SHAPE_SIZE_Y = 40;

    @Override
    public ShapeGlyph<Line> getGlyph() {
        final Line line = makeLine();

        return new ShapeGlyph<Line>() {
            @Override
            public Shape<Line> getShape() {
                return line;
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
        return ConnectorCategory.CATEGORY;
    }

    @Override
    public ShapeDragProxy<Line> getDragProxy( final ShapeDragProxyPreviewCallback dragPreviewCallback,
                                              final ShapeDragProxyCompleteCallback dragEndCallBack ) {
        final Line line = makeLine();

        return new ShapeDragProxy<Line>() {
            @Override
            public Shape<Line> getDragShape() {
                return line;
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
        return new WiresLine( makeLine() );
    }

    @Override
    public boolean builds( final WiresBaseShape shapeType ) {
        return shapeType instanceof WiresLine;
    }

    private Line makeLine() {
        final Line line = new Line( 0 - ( SHAPE_SIZE_X / 2 ),
                                    0 - ( SHAPE_SIZE_Y / 2 ),
                                    SHAPE_SIZE_X / 2,
                                    SHAPE_SIZE_Y / 2 );
        line.setStrokeColor( ShapesUtils.RGB_STROKE_SHAPE )
                .setStrokeWidth( ShapesUtils.RGB_STROKE_WIDTH_SHAPE )
                .setFillColor( ShapesUtils.RGB_FILL_SHAPE )
                .setLineCap( LineCap.ROUND )
                .setStrokeWidth( 3 )
                .setDraggable( false );
        return line;
    }

}