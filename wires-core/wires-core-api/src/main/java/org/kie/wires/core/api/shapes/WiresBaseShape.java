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

import com.emitrom.lienzo.client.core.event.NodeMouseClickEvent;
import com.emitrom.lienzo.client.core.event.NodeMouseClickHandler;
import com.emitrom.lienzo.client.core.shape.Group;
import com.emitrom.lienzo.client.core.shape.Layer;
import org.kie.wires.core.api.selection.RequiresSelectionManager;
import org.kie.wires.core.api.selection.SelectionManager;

/**
 * A Fixed Shape that cannot be re-sized or have connectors attached
 */
public abstract class WiresBaseShape extends Group implements WiresShape,
                                                              RequiresSelectionManager {

    protected String id;
    protected SelectionManager selectionManager;

    public WiresBaseShape() {
        setDraggable( true );
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void setSelectionManager( final SelectionManager manager ) {
        this.selectionManager = manager;
    }

    @Override
    public void init( final double cx,
                      final double cy ) {
        setX( cx );
        setY( cy );

        addNodeMouseClickHandler( new NodeMouseClickHandler() {
            @Override
            public void onNodeMouseClick( final NodeMouseClickEvent nodeMouseClickEvent ) {
                selectionManager.selectShape( WiresBaseShape.this );
            }
        } );
    }

    @Override
    public void destroy() {
        Layer layer = getLayer();
        layer.remove( this );
        layer.draw();
    }

}
