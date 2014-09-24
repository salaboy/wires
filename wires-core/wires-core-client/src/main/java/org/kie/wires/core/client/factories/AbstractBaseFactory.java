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
package org.kie.wires.core.client.factories;

import com.emitrom.lienzo.client.core.shape.Shape;
import org.kie.wires.core.api.factories.ShapeDragProxy;
import org.kie.wires.core.api.factories.ShapeDragProxyCompleteCallback;
import org.kie.wires.core.api.factories.ShapeDragProxyPreviewCallback;
import org.kie.wires.core.api.factories.ShapeFactory;
import org.kie.wires.core.api.factories.ShapeGlyph;

/**
 * Base implementation of a ShapeFactory to avoid unnecessary boiler-plate code
 */
public abstract class AbstractBaseFactory<T extends Shape<T>> implements ShapeFactory<T> {

    @Override
    public ShapeGlyph<T> getGlyph() {
        final T shape = makeShape();

        return new ShapeGlyph<T>() {
            @Override
            public Shape<T> getShape() {
                return shape;
            }

            @Override
            public double getWidth() {
                return AbstractBaseFactory.this.getWidth();
            }

            @Override
            public double getHeight() {
                return AbstractBaseFactory.this.getHeight();
            }
        };
    }

    @Override
    public ShapeDragProxy<T> getDragProxy( final ShapeDragProxyPreviewCallback dragPreviewCallback,
                                           final ShapeDragProxyCompleteCallback dragEndCallBack ) {
        final T shape = makeShape();

        return new ShapeDragProxy<T>() {
            @Override
            public Shape<T> getDragShape() {
                return shape;
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
                return AbstractBaseFactory.this.getWidth();
            }

            @Override
            public int getHeight() {
                return AbstractBaseFactory.this.getHeight();
            }

        };
    }

    protected abstract T makeShape();

    protected abstract int getWidth();

    protected abstract int getHeight();

}
