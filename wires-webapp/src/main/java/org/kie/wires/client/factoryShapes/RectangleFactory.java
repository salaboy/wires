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

import javax.enterprise.event.Event;

import com.emitrom.lienzo.client.core.event.NodeMouseDownEvent;
import com.emitrom.lienzo.client.core.event.NodeMouseDownHandler;
import com.emitrom.lienzo.client.core.shape.Rectangle;
import com.emitrom.lienzo.client.core.shape.Shape;
import com.emitrom.lienzo.client.widget.LienzoPanel;
import org.kie.wires.core.api.events.ShapeAddEvent;
import org.kie.wires.core.client.util.ShapeCategory;
import org.kie.wires.core.client.util.ShapeType;
import org.kie.wires.core.client.util.ShapesUtils;

public class RectangleFactory extends ShapeFactory<Rectangle> {

    private static String DESCRIPTION = "Box";

    public RectangleFactory( final LienzoPanel panel,
                             final Event<ShapeAddEvent> shapeAddEvent ) {
        super( panel,
               shapeAddEvent );
    }

    @Override
    protected Shape<Rectangle> drawShape() {
        final Rectangle rectangle = new Rectangle( 40,
                                                   40 );
        setAttributes( rectangle,
                       5,
                       5 );
        return rectangle;
    }

    @Override
    protected String getDescription() {
        return DESCRIPTION;
    }

    @Override
    protected void addShapeHandlers( final Shape<Rectangle> shape ) {
        shape.addNodeMouseDownHandler( getNodeMouseDownEvent() );
    }

    @Override
    protected void addBoundingHandlers( final Rectangle boundingBox ) {
        boundingBox.addNodeMouseDownHandler( getNodeMouseDownEvent() );

    }

    @Override
    protected NodeMouseDownHandler getNodeMouseDownEvent() {
        NodeMouseDownHandler nodeMouseDownHandler = new NodeMouseDownHandler() {
            public void onNodeMouseDown( NodeMouseDownEvent event ) {
                final Rectangle floatingShape = new Rectangle( 70,
                                                               40 );
                setAttributes( floatingShape,
                               0,
                               0 );
                setFloatingPanel( floatingShape,
                                  "WiresRectangle",
                                  40,
                                  70,
                                  event );
            }
        };

        return nodeMouseDownHandler;
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

    @Override
    protected ShapeCategory getCategory() {
        return ShapeType.RECTANGLE.getCategory();
    }

}