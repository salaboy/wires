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

import java.util.List;

import com.emitrom.lienzo.client.core.shape.IPrimitive;
import com.emitrom.lienzo.client.core.shape.Node;

public interface Magnet<T extends Node<T>> extends IPrimitive<T> {

    String getId();

    void setEnabled( final boolean isEnabled );

    boolean isEnabled();

    void attachControlPoint( final ControlPoint controlPoint );

    void detachControlPoint( final ControlPoint controlPoint );

    List<ControlPoint> getAttachedControlPoints();

    void setActive( final boolean isActive );

}
