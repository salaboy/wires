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

import java.util.ArrayList;
import java.util.List;
import javax.enterprise.event.Event;

import com.emitrom.lienzo.client.widget.LienzoPanel;
import com.google.gwt.user.client.ui.Composite;
import org.kie.wires.core.api.events.ShapeAddEvent;
import org.kie.wires.core.client.shapes.PaletteShape;
import org.kie.wires.core.client.util.ShapeCategory;
import org.kie.wires.core.client.util.ShapeType;

public class StencilBuilder extends Composite {

    private List<PaletteShape> shapes = new ArrayList<PaletteShape>();

    public StencilBuilder( final Event<ShapeAddEvent> shapeAddEvent,
                           final ShapeCategory shapeCategory,
                           final LienzoPanel panel ) {
        for ( ShapeType shapeType : ShapeType.values() ) {
            if ( shapeType.getCategory().equals( shapeCategory ) ) {
                shapes.add( newShape( shapeType,
                                      panel,
                                      shapeAddEvent ) );
            }
        }
    }

    public List<PaletteShape> getShapes() {
        return shapes;
    }

    private PaletteShape newShape( final ShapeType shapeType,
                                   final LienzoPanel panel,
                                   final Event<ShapeAddEvent> shapeAddEvent ) {
        PaletteShape shape = null;

        switch ( shapeType ) {
            case LINE:
                shape = new LineFactory( panel,
                                         shapeAddEvent ).build();
                break;
            case RECTANGLE:
                shape = new RectangleFactory( panel,
                                              shapeAddEvent ).build();
                break;
            case CIRCLE:
                shape = new CircleFactory( panel,
                                           shapeAddEvent ).build();
                break;
            default:
                throw new IllegalStateException( "Unrecognized shape type '" + shapeType + "'!" );
        }
        return shape;
    }

}