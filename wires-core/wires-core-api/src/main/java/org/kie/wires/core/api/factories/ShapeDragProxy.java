/*
 * Copyright 2012 JBoss Inc
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
package org.kie.wires.core.api.factories;

import com.emitrom.lienzo.client.core.shape.Shape;

/**
 * Proxy for Shapes being dragged from the Palette
 */
public interface ShapeDragProxy<T extends Shape<T>> {

    /**
     * Get a Shape that is used as the drag proxy
     * @return
     */
    Shape<T> getDragShape();

    /**
     * Called during the drag operation
     * @param x
     * @param y
     */
    void onDragPreview( final double x,
                        final double y );

    /**
     * Called when the drag operation completes
     * @param x
     * @param y
     */
    void onDragComplete( final double x,
                         final double y );

    /**
     * Height of proxy used to ensure proxy is centered around mouse pointer position
     * @return
     */
    int getHeight();

    /**
     * Width of proxy used to ensure proxy is centered around mouse pointer position
     * @return
     */
    int getWidth();

}
