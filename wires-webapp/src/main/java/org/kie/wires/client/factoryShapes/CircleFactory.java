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
import com.emitrom.lienzo.client.core.shape.Circle;
import com.emitrom.lienzo.client.core.shape.Rectangle;
import com.emitrom.lienzo.client.core.shape.Shape;
import com.emitrom.lienzo.client.widget.LienzoPanel;
import org.kie.wires.core.api.events.ShapeAddEvent;
import org.kie.wires.core.client.util.ShapeCategory;
import org.kie.wires.core.client.util.ShapeType;
import org.kie.wires.core.client.util.ShapesUtils;

public class CircleFactory extends ShapeFactory<Circle> {

    private static String DESCRIPTION = "Circle";

    public CircleFactory( final LienzoPanel panel,
                          final Event<ShapeAddEvent> shapeAddEvent ) {
        super( panel,
               shapeAddEvent );
    }

    @Override
    protected Shape<Circle> drawShape() {
        final Circle circle = new Circle( 15 );
        setAttributes( circle,
                       25,
                       25 );
        circle.setDraggable( false );
        return circle;
    }

    @Override
    protected String getDescription() {
        return DESCRIPTION;
    }

    @Override
    protected void addShapeHandlers( final Shape<Circle> shape ) {
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
                final Circle floatingShape = new Circle( 20 );
                setAttributes( floatingShape,
                               25,
                               25 );
                setFloatingPanel( floatingShape,
                                  "WiresCircle",
                                  50,
                                  50,
                                  event );
            }

        };

        return nodeMouseDownHandler;
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

    @Override
    protected ShapeCategory getCategory() {
        return ShapeType.CIRCLE.getCategory();
    }

}
