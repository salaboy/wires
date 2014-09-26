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

public interface WiresShape {

    /**
     * Get UUID for Shape
     * @return
     */
    String getId();

    /**
     * Select the shape. Implementations may choose to change their appearance
     */
    void setSelected( final boolean isSelected );

    /**
     * Destroy the shape and any related components
     */
    void destroy();

    /**
     * Check whether the Shape contains a point
     * @param cx Canvas X coordinate
     * @param cy Canvas Y coordinate
     * @return
     */
    boolean contains( final double cx,
                      final double cy );

}
