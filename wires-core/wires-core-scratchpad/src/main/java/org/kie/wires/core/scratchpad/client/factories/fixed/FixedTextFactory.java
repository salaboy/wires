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
package org.kie.wires.core.scratchpad.client.factories.fixed;

import javax.enterprise.context.ApplicationScoped;

import com.emitrom.lienzo.client.core.shape.Layer;
import com.emitrom.lienzo.client.core.shape.Shape;
import com.emitrom.lienzo.client.core.shape.Text;
import com.emitrom.lienzo.client.core.types.TextMetrics;
import com.emitrom.lienzo.client.widget.LienzoPanel;
import com.emitrom.lienzo.shared.core.types.TextAlign;
import com.emitrom.lienzo.shared.core.types.TextBaseLine;
import org.kie.wires.core.api.factories.ShapeDragProxy;
import org.kie.wires.core.api.factories.ShapeDragProxyCompleteCallback;
import org.kie.wires.core.api.factories.ShapeDragProxyPreviewCallback;
import org.kie.wires.core.api.factories.ShapeFactory;
import org.kie.wires.core.api.factories.ShapeGlyph;
import org.kie.wires.core.api.factories.categories.Category;
import org.kie.wires.core.api.shapes.WiresBaseShape;
import org.kie.wires.core.client.factories.categories.FixedShapeCategory;
import org.kie.wires.core.client.util.ShapesUtils;
import org.kie.wires.core.scratchpad.client.shapes.fixed.WiresFixedText;

@ApplicationScoped
public class FixedTextFactory implements ShapeFactory<Text> {

    private static final String DESCRIPTION = "Text";

    private static final int FONT_POINT = 32;

    private final double TEXT_WIDTH;
    private final double TEXT_HEIGHT;

    public FixedTextFactory() {
        final Text text = makeText();
        final LienzoPanel panel = new LienzoPanel( 100,
                                                   100 );
        final Layer layer = new Layer();
        panel.add( layer );
        final TextMetrics tm = text.measure( layer.getContext() );
        TEXT_WIDTH = tm.getWidth();
        TEXT_HEIGHT = tm.getHeight();
    }

    @Override
    public ShapeGlyph<Text> getGlyph() {
        final Text text = makeText();

        return new ShapeGlyph<Text>() {
            @Override
            public Shape<Text> getShape() {
                return text;
            }

            @Override
            public double getWidth() {
                return TEXT_WIDTH;
            }

            @Override
            public double getHeight() {
                return TEXT_HEIGHT;
            }
        };
    }

    @Override
    public String getShapeDescription() {
        return DESCRIPTION;
    }

    @Override
    public Category getCategory() {
        return FixedShapeCategory.CATEGORY;
    }

    @Override
    public ShapeDragProxy<Text> getDragProxy( final ShapeDragProxyPreviewCallback dragPreviewCallback,
                                              final ShapeDragProxyCompleteCallback dragEndCallBack ) {
        final Text text = makeText();

        return new ShapeDragProxy<Text>() {
            @Override
            public Shape<Text> getDragShape() {
                return text;
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
                return (int) Math.round( TEXT_WIDTH );
            }

            @Override
            public int getHeight() {
                return (int) Math.round( TEXT_HEIGHT );
            }

        };
    }

    @Override
    public WiresBaseShape getShape() {
        return new WiresFixedText( makeText() );
    }

    @Override
    public boolean builds( final WiresBaseShape shapeType ) {
        return shapeType instanceof WiresFixedText;
    }

    private Text makeText() {
        final Text text = new Text( "T",
                                    "normal",
                                    FONT_POINT );
        text.setStrokeColor( ShapesUtils.RGB_STROKE_TEXT )
                .setFillColor( ShapesUtils.RGB_FILL_TEXT )
                .setTextBaseLine( TextBaseLine.MIDDLE )
                .setTextAlign( TextAlign.CENTER )
                .setDraggable( false );
        return text;
    }

}
