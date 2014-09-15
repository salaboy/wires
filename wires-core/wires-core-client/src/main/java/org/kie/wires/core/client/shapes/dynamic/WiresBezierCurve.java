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
package org.kie.wires.core.client.shapes.dynamic;

import com.emitrom.lienzo.client.core.event.NodeDragStartEvent;
import com.emitrom.lienzo.client.core.event.NodeDragStartHandler;
import com.emitrom.lienzo.client.core.event.NodeMouseClickEvent;
import com.emitrom.lienzo.client.core.event.NodeMouseClickHandler;
import com.emitrom.lienzo.client.core.shape.BezierCurve;
import org.kie.wires.core.api.shapes.WiresBaseDynamicShape;
import org.kie.wires.core.client.util.UUID;

public class WiresBezierCurve extends WiresBaseDynamicShape {

    private static final int BOUNDARY_SIZE = 10;

    private BezierCurve curve;
    private BezierCurve bounding;

    public WiresBezierCurve( final double x,
                             final double y,
                             final double controlX1,
                             final double controlY1,
                             final double controlX2,
                             final double controlY2,
                             final double endX,
                             final double endY ) {
        id = UUID.uuid();
        curve = new BezierCurve( x,
                                 y,
                                 controlX1,
                                 controlY1,
                                 controlX2,
                                 controlY2,
                                 endX,
                                 endY );
        curve.setX( x );
        curve.setY( y );

        bounding = new BezierCurve( x,
                                    y,
                                    controlX1,
                                    controlY1,
                                    controlX2,
                                    controlY2,
                                    endX,
                                    endY );
        bounding.setStrokeWidth( BOUNDARY_SIZE );
        bounding.setVisible( false );
        bounding.setAlpha( 0.1 );

        add( curve );
        add( bounding );

        magnets.clear();
        controlPoints.clear();
    }

    @Override
    public void setSelected( final boolean isSelected ) {
        bounding.setVisible( isSelected );
    }

    @Override
    public void init( final double cx,
                      final double cy ) {
        setX( cx );
        setY( cy );

        addNodeMouseClickHandler( new NodeMouseClickHandler() {
            public void onNodeMouseClick( final NodeMouseClickEvent nodeMouseClickEvent ) {
                selectionManager.selectShape( WiresBezierCurve.this );
            }
        } );

        addNodeDragStartHandler( new NodeDragStartHandler() {
            public void onNodeDragStart( NodeDragStartEvent nodeDragStartEvent ) {
                selectionManager.deselectShape( WiresBezierCurve.this );
            }
        } );
    }

    @Override
    public boolean contains( final double cx,
                             final double cy ) {
        return false;
    }

}
