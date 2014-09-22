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
package org.kie.wires.core.trees.client.factories;

import javax.enterprise.context.ApplicationScoped;

import com.emitrom.lienzo.client.core.shape.Circle;
import com.emitrom.lienzo.client.core.shape.Shape;
import org.kie.wires.core.api.factories.ShapeDragProxy;
import org.kie.wires.core.api.factories.ShapeDragProxyCompleteCallback;
import org.kie.wires.core.api.factories.ShapeDragProxyPreviewCallback;
import org.kie.wires.core.api.factories.ShapeFactory;
import org.kie.wires.core.api.factories.ShapeGlyph;
import org.kie.wires.core.api.factories.categories.Category;
import org.kie.wires.core.api.shapes.WiresBaseShape;
import org.kie.wires.core.client.util.ShapesUtils;
import org.kie.wires.core.trees.client.factories.categories.TreeNodesCategory;
import org.kie.wires.core.trees.client.shapes.WiresTreeNode;

@ApplicationScoped
public class TreeNodeFactory implements ShapeFactory<Circle> {

    private static final String DESCRIPTION = "Node";

    private static final int SHAPE_RADIUS = 25;

    @Override
    public ShapeGlyph<Circle> getGlyph() {
        final Circle circle = makeCircle();

        return new ShapeGlyph<Circle>() {
            @Override
            public Shape<Circle> getShape() {
                return circle;
            }

            @Override
            public double getWidth() {
                return ( SHAPE_RADIUS + ShapesUtils.RGB_STROKE_WIDTH_SHAPE ) * 2;
            }

            @Override
            public double getHeight() {
                return ( SHAPE_RADIUS + ShapesUtils.RGB_STROKE_WIDTH_SHAPE ) * 2;
            }
        };
    }

    @Override
    public String getShapeDescription() {
        return DESCRIPTION;
    }

    @Override
    public Category getCategory() {
        return TreeNodesCategory.CATEGORY;
    }

    @Override
    public ShapeDragProxy<Circle> getDragProxy( final ShapeDragProxyPreviewCallback dragPreviewCallback,
                                                final ShapeDragProxyCompleteCallback dragEndCallBack ) {
        final Circle circle = makeCircle();

        return new ShapeDragProxy<Circle>() {
            @Override
            public Shape<Circle> getDragShape() {
                return circle;
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
                return ( SHAPE_RADIUS + ShapesUtils.RGB_STROKE_WIDTH_SHAPE ) * 2;
            }

            @Override
            public int getHeight() {
                return ( SHAPE_RADIUS + ShapesUtils.RGB_STROKE_WIDTH_SHAPE ) * 2;
            }

        };
    }

    @Override
    public WiresBaseShape getShape() {
        return new WiresTreeNode( makeCircle() );
    }

    @Override
    public boolean builds( final WiresBaseShape shapeType ) {
        return shapeType instanceof WiresTreeNode;
    }

    private Circle makeCircle() {
        final Circle circle = new Circle( SHAPE_RADIUS );
        circle.setStrokeColor( "#000000" )
                .setStrokeWidth( ShapesUtils.RGB_STROKE_WIDTH_SHAPE )
                .setFillColor( "#ff0000" )
                .setDraggable( false );
        return circle;
    }

}
