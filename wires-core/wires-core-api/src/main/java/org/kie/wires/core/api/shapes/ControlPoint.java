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
package org.kie.wires.core.api.shapes;

import com.emitrom.lienzo.client.core.shape.Layer;

/**
 * @author salaboy
 */
public interface ControlPoint {

    static final int CONTROL_START = 0;
    static final int CONTROL_END = 1;

    static final int CONTROL_TOP_LEFT = 2;
    static final int CONTROL_BOTTOM_LEFT = 3;
    static final int CONTROL_TOP_RIGHT = 4;
    static final int CONTROL_BOTTOM_RIGHT = 5;

    String getId();

    void placeControlPoint( final Layer layer );

    void setControlPointX( final double x );

    void setControlPointY( final double y );

    void setControlPointVisible( final boolean visible );

    void moveControlPoint();

    HasControlPoints getShape();

    void updateShape( final Layer layer,
                      final double x,
                      final double y );

    boolean isAttached();

    double getControlPointX();

    double getControlPointY();

}
