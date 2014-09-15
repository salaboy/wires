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
package org.kie.wires.core.client.palette;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.emitrom.lienzo.client.core.shape.Layer;
import com.emitrom.lienzo.client.widget.LienzoPanel;
import com.google.gwt.user.client.ui.Composite;
import org.kie.wires.core.client.factories.categories.ShapeCategory;
import org.kie.wires.core.api.factories.ShapeFactory;
import org.kie.wires.core.client.factories.ShapeFactoryCache;
import org.kie.wires.core.client.util.ShapeFactoryUtil;
import org.kie.wires.core.client.util.ShapesUtils;

@Dependent
public class ShapesGroup extends Composite {

    private Layer layer;
    private LienzoPanel panel;

    @Inject
    private ShapeFactoryCache factoriesCache;

    @Inject
    private StencilPaletteBuilder stencilBuilder;

    @PostConstruct
    public void init() {
        panel = new LienzoPanel( ShapeFactoryUtil.WIDTH_PANEL,
                                 ShapesUtils.calculateHeight( ShapesUtils.getNumberOfShapesInCategory( ShapeCategory.CATEGORY,
                                                                                                       factoriesCache.getShapeFactories() ) ) );
        layer = new Layer();
        panel.getScene().add( layer );
        initWidget( panel );

        drawShapes();
    }

    private void drawShapes() {
        //Get PaletteShape for each Shape Factory
        final List<PaletteShape> shapes = new ArrayList<PaletteShape>();
        for ( ShapeFactory factory : factoriesCache.getShapeFactories() ) {
            if ( factory.getCategory().equals( ShapeCategory.CATEGORY ) ) {
                shapes.add( stencilBuilder.build( panel,
                                                  factory ) );
            }
        }

        //Add PaletteShapes to the UI
        int shapeCount = 1;
        for ( PaletteShape shape : shapes ) {
            shape.setX( PaletteLayoutUtilities.getX( shapeCount ) );
            shape.setY( PaletteLayoutUtilities.getY( shapeCount ) );
            layer.add( shape );
            shapeCount++;
        }

        layer.draw();
    }

}
