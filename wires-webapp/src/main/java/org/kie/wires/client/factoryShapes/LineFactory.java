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
import javax.enterprise.event.Event;
import javax.inject.Inject;

import com.emitrom.lienzo.client.core.shape.Line;
import com.emitrom.lienzo.client.core.shape.Shape;
import org.kie.wires.client.factoryShapes.categories.ConnectorCategory;
import org.kie.wires.core.api.categories.Category;
import org.kie.wires.core.api.events.ShapeDragCompleteEvent;
import org.kie.wires.core.api.factories.ShapeDragProxy;
import org.kie.wires.core.api.factories.ShapeFactory;
import org.kie.wires.core.api.shapes.WiresBaseGroupShape;
import org.kie.wires.core.client.shapes.WiresLine;
import org.kie.wires.core.client.util.ShapesUtils;

@ApplicationScoped
public class LineFactory implements ShapeFactory<Line> {

    private static final String DESCRIPTION = "Line";

    @Inject
    private Event<ShapeDragCompleteEvent> shapeDragCompleteEvent;

    @Override
    public Shape<Line> getGlyph() {
        Line line = new Line( 5,
                              5,
                              45,
                              45 );
        setAttributes( line );
        return line;
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
    public ShapeDragProxy<Line> getDragProxy() {
        final Line proxy = new Line( 5,
                                     5,
                                     45,
                                     45 );
        setAttributes( proxy );

        return new ShapeDragProxy<Line>() {
            @Override
            public Shape<Line> getDragShape() {
                return proxy;
            }

            @Override
            public String getShapeDescription() {
                return DESCRIPTION;
            }

            @Override
            public void onDragEnd( final int x,
                                   final int y ) {
                shapeDragCompleteEvent.fire( new ShapeDragCompleteEvent( DESCRIPTION,
                                                                         x,
                                                                         y ) );
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
        return new WiresLine( 0,
                              0,
                              30,
                              30 );
    }

    private void setAttributes( final Line line ) {
        line.setStrokeColor( ShapesUtils.RGB_STROKE_SHAPE )
                .setStrokeWidth( ShapesUtils.RGB_STROKE_WIDTH_SHAPE )
                .setFillColor( ShapesUtils.RGB_FILL_SHAPE )
                .setDraggable( false );
    }

}