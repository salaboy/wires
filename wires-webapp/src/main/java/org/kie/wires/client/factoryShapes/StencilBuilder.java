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
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.emitrom.lienzo.client.core.event.NodeMouseDownEvent;
import com.emitrom.lienzo.client.core.event.NodeMouseDownHandler;
import com.emitrom.lienzo.client.core.shape.Layer;
import com.emitrom.lienzo.client.core.shape.Rectangle;
import com.emitrom.lienzo.client.core.shape.Shape;
import com.emitrom.lienzo.client.core.shape.Text;
import com.emitrom.lienzo.client.widget.LienzoPanel;
import com.emitrom.lienzo.shared.core.types.TextBaseLine;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;
import org.kie.wires.core.api.categories.Category;
import org.kie.wires.core.api.factories.ShapeDragProxy;
import org.kie.wires.core.api.factories.ShapeFactory;
import org.kie.wires.core.client.factories.ShapeFactoryCache;
import org.kie.wires.core.client.shapes.PaletteShape;
import org.kie.wires.core.client.util.ShapeFactoryUtil;

@ApplicationScoped
public class StencilBuilder extends Composite {

    private static final int ZINDEX = Integer.MAX_VALUE;

    @Inject
    private ShapeFactoryCache factoriesCache;

    public List<PaletteShape> getShapes( final LienzoPanel dragProxyParentPanel,
                                         final Category shapeCategory ) {
        final List<PaletteShape> shapes = new ArrayList<PaletteShape>();
        for ( ShapeFactory factory : factoriesCache.getShapeFactories() ) {
            if ( factory.getCategory().equals( shapeCategory ) ) {
                shapes.add( build( dragProxyParentPanel,
                                   factory ) );
            }
        }
        return shapes;
    }

    private PaletteShape build( final LienzoPanel dragProxyParentPanel,
                                final ShapeFactory factory ) {
        final PaletteShape paletteShape = new PaletteShape();
        final Rectangle bounding = drawBoundingBox();
        addBoundingHandlers( dragProxyParentPanel,
                             factory.getDragProxy(),
                             bounding );
        paletteShape.setBounding( bounding );

        final Shape shape = factory.getGlyph();
        addShapeHandlers( dragProxyParentPanel,
                          factory.getDragProxy(),
                          shape );
        paletteShape.setShape( shape );

        final Text description = drawDescription( factory.getShapeDescription() );
        paletteShape.setDescription( description );

        return paletteShape;
    }

    private Rectangle drawBoundingBox() {
        final Rectangle boundingBox = new Rectangle( ShapeFactoryUtil.WIDTH_BOUNDING,
                                                     ShapeFactoryUtil.HEIGHT_BOUNDING );
        boundingBox.setStrokeColor( ShapeFactoryUtil.RGB_STROKE_BOUNDING )
                .setStrokeWidth( 1 )
                .setFillColor( ShapeFactoryUtil.RGB_FILL_BOUNDING )
                .setDraggable( false );
        return boundingBox;
    }

    private void addBoundingHandlers( final LienzoPanel dragProxyParentPanel,
                                      final ShapeDragProxy proxy,
                                      final Rectangle boundingBox ) {
        boundingBox.addNodeMouseDownHandler( getShapeDragStartHandler( dragProxyParentPanel,
                                                                       proxy ) );
    }

    private void addShapeHandlers( final LienzoPanel dragProxyParentPanel,
                                   final ShapeDragProxy proxy,
                                   final Shape shape ) {
        shape.addNodeMouseDownHandler( getShapeDragStartHandler( dragProxyParentPanel,
                                                                 proxy ) );
    }

    private Text drawDescription( final String description ) {
        Text text = new Text( description,
                              ShapeFactoryUtil.FONT_FAMILY_DESCRIPTION,
                              ShapeFactoryUtil.FONT_SIZE_DESCRIPTION );
        text.setFillColor( ShapeFactoryUtil.RGB_TEXT_DESCRIPTION );
        text.setTextBaseLine( TextBaseLine.MIDDLE );
        text.setX( 5 );
        text.setY( 60 );
        return text;
    }

    private NodeMouseDownHandler getShapeDragStartHandler( final LienzoPanel dragProxyParentPanel,
                                                           final ShapeDragProxy proxy ) {
        return new NodeMouseDownHandler() {

            @Override
            public void onNodeMouseDown( final NodeMouseDownEvent event ) {
                final LienzoPanel dragProxyPanel = new LienzoPanel( proxy.getWidth(),
                                                                    proxy.getHeight() );
                final Layer dragProxyLayer = new Layer();
                dragProxyLayer.add( proxy.getDragShape() );
                dragProxyPanel.add( dragProxyLayer );
                dragProxyLayer.draw();

                setDragProxyPosition( dragProxyParentPanel,
                                      dragProxyPanel,
                                      event );
                attachDragProxyHandlers( dragProxyPanel,
                                         proxy );

                RootPanel.get().add( dragProxyPanel );
            }
        };
    }

    private void setDragProxyPosition( final LienzoPanel dragProxyParentPanel,
                                       final LienzoPanel dragProxyPanel,
                                       final NodeMouseDownEvent event ) {
        Style style = dragProxyPanel.getElement().getStyle();
        style.setPosition( Style.Position.ABSOLUTE );
        style.setLeft( dragProxyParentPanel.getAbsoluteLeft() + event.getX() - ( dragProxyPanel.getWidth() / 2 ),
                       Style.Unit.PX );
        style.setTop( dragProxyParentPanel.getAbsoluteTop() + event.getY() - ( dragProxyPanel.getHeight() / 2 ),
                      Style.Unit.PX );
        style.setZIndex( ZINDEX );
    }

    private void attachDragProxyHandlers( final LienzoPanel floatingPanel,
                                          final ShapeDragProxy proxy ) {
        final Style style = floatingPanel.getElement().getStyle();
        final HandlerRegistration[] handlerRegs = new HandlerRegistration[ 2 ];

        //MouseMoveEvents
        handlerRegs[ 0 ] = RootPanel.get().addDomHandler( new MouseMoveHandler() {

            @Override
            public void onMouseMove( final MouseMoveEvent mouseMoveEvent ) {
                style.setLeft( mouseMoveEvent.getX() - ( floatingPanel.getWidth() / 2 ),
                               Style.Unit.PX );
                style.setTop( mouseMoveEvent.getY() - ( floatingPanel.getHeight() / 2 ),
                              Style.Unit.PX );
            }
        }, MouseMoveEvent.getType() );

        //MouseUpEvent
        handlerRegs[ 1 ] = RootPanel.get().addDomHandler( new MouseUpHandler() {

            @Override
            public void onMouseUp( final MouseUpEvent mouseUpEvent ) {
                handlerRegs[ 0 ].removeHandler();
                handlerRegs[ 1 ].removeHandler();
                RootPanel.get().remove( floatingPanel );
                proxy.onDragEnd( mouseUpEvent.getX(),
                                 mouseUpEvent.getY() );
            }
        }, MouseUpEvent.getType() );
    }

}