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
package org.kie.wires.core.client.shapes.fixed;

import com.emitrom.lienzo.client.core.shape.Circle;
import org.kie.wires.core.api.shapes.WiresBaseShape;
import org.kie.wires.core.client.util.ShapesUtils;
import org.kie.wires.core.client.util.UUID;

public class WiresFixedCircle extends WiresBaseShape {

    private Circle circle;

    public WiresFixedCircle( final double x,
                             final double y,
                             final double radius ) {
        id = UUID.uuid();
        circle = new Circle( radius );
        circle.setStrokeColor( ShapesUtils.RGB_STROKE_SHAPE );
        circle.setStrokeWidth( ShapesUtils.RGB_STROKE_WIDTH_SHAPE );
        circle.setFillColor( "#ff0000" );
        circle.setAlpha( 0.75 );
        circle.setX( x );
        circle.setY( y );

        add( circle );
    }

    @Override
    public void init( final double cx,
                      final double cy ) {
        setX( cx );
        setY( cy );
    }

    @Override
    public boolean contains( final double cx,
                             final double cy ) {
        return false;
    }
}
