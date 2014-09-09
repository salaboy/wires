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

import javax.enterprise.event.Event;

import com.emitrom.lienzo.client.core.event.NodeMouseDownEvent;
import com.emitrom.lienzo.client.core.event.NodeMouseDownHandler;
import com.emitrom.lienzo.client.core.shape.Layer;
import com.emitrom.lienzo.client.core.shape.Rectangle;
import com.emitrom.lienzo.client.core.shape.Shape;
import com.emitrom.lienzo.client.core.shape.Text;
import com.emitrom.lienzo.client.widget.LienzoPanel;
import com.emitrom.lienzo.shared.core.types.TextBaseLine;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.RootPanel;
import org.kie.wires.core.api.events.ShapeAddEvent;
import org.kie.wires.core.client.shapes.PaletteShape;
import org.kie.wires.core.client.util.ShapeCategory;
import org.kie.wires.core.client.util.ShapeFactoryUtil;

public abstract class ShapeFactory<T extends Shape<T>> {

    protected LienzoPanel panel;

    protected Event<ShapeAddEvent> shapeAddEvent;

    protected static final int ZINDEX = 100;

    protected PaletteShape paletteShape = new PaletteShape();

    protected ShapeFactory( final LienzoPanel lienzoPanel,
                            final Event<ShapeAddEvent> shapeEvent ) {
        panel = lienzoPanel;
        shapeAddEvent = shapeEvent;
    }

    protected abstract ShapeCategory getCategory();

    public PaletteShape build() {
        final Rectangle bounding = drawBoundingBox();
        addBoundingHandlers( bounding );
        paletteShape.setBounding( bounding );

        final Shape<T> shape = drawShape();
        addShapeHandlers( shape );
        paletteShape.setShape( shape );

        final Text description = drawDescription( getDescription() );
        paletteShape.setDescription( description );

        return paletteShape;
    }

    protected Rectangle drawBoundingBox() {
        final Rectangle boundingBox = new Rectangle( ShapeFactoryUtil.WIDTH_BOUNDING,
                                                     ShapeFactoryUtil.HEIGHT_BOUNDING );
        boundingBox.setStrokeColor( ShapeFactoryUtil.RGB_STROKE_BOUNDING )
                .setStrokeWidth( 1 )
                .setFillColor( ShapeFactoryUtil.RGB_FILL_BOUNDING )
                .setDraggable( false );
        return boundingBox;
    }

    protected abstract void addBoundingHandlers( final Rectangle boundingBox );

    /**
     * Draw a shape representing whatever the concrete Factory represents
     * @return The shape
     */
    protected abstract Shape<T> drawShape();

    protected abstract void addShapeHandlers( final Shape<T> shape );

    protected Text drawDescription( final String description ) {
        Text text = new Text( description,
                              ShapeFactoryUtil.FONT_FAMILY_DESCRIPTION,
                              ShapeFactoryUtil.FONT_SIZE_DESCRIPTION );
        text.setFillColor( ShapeFactoryUtil.RGB_TEXT_DESCRIPTION );
        text.setTextBaseLine( TextBaseLine.MIDDLE );
        text.setX( 5 );
        text.setY( 60 );
        return text;
    }

    protected abstract String getDescription();

    protected abstract NodeMouseDownHandler getNodeMouseDownEvent();

    protected void setFloatingPanel( final Shape floatingShape,
                                     final String shapeName,
                                     final int height,
                                     final int width,
                                     final NodeMouseDownEvent event ) {
        final Layer floatingLayer = new Layer();
        final LienzoPanel floatingPanel = new LienzoPanel( width,
                                                           height );
        floatingLayer.add( floatingShape );
        floatingPanel.add( floatingLayer );
        floatingLayer.draw();

        final Style style = getFloatingStyle( floatingPanel,
                                              event );
        RootPanel.get().add( floatingPanel );
        setFloatingHandlers( style,
                             floatingPanel,
                             shapeName );
    }

    protected void setFloatingHandlers( final Style style,
                                        final LienzoPanel floatingPanel,
                                        final String shapeName ) {
        final HandlerRegistration[] handlerRegs = new HandlerRegistration[ 2 ];
        handlerRegs[ 0 ] = RootPanel.get().addDomHandler( new MouseMoveHandler() {
            public void onMouseMove( MouseMoveEvent mouseMoveEvent ) {
                style.setLeft( mouseMoveEvent.getX() - ( floatingPanel.getWidth() / 2 ),
                               Unit.PX );
                style.setTop( mouseMoveEvent.getY() - ( floatingPanel.getHeight() / 2 ),
                              Unit.PX );
            }
        }, MouseMoveEvent.getType() );

        handlerRegs[ 1 ] = RootPanel.get().addDomHandler( new MouseUpHandler() {
            public void onMouseUp( MouseUpEvent mouseUpEvent ) {
                handlerRegs[ 0 ].removeHandler();
                handlerRegs[ 1 ].removeHandler();
                RootPanel.get().remove( floatingPanel );
                shapeAddEvent.fire( new ShapeAddEvent( shapeName,
                                                       mouseUpEvent.getX(),
                                                       mouseUpEvent.getY() ) );
            }
        }, MouseUpEvent.getType() );
    }

    protected Style getFloatingStyle( final LienzoPanel floatingPanel,
                                      final NodeMouseDownEvent event ) {
        Style style = floatingPanel.getElement().getStyle();
        style.setPosition( Position.ABSOLUTE );
        style.setLeft( panel.getAbsoluteLeft() + event.getX() - ( floatingPanel.getWidth() / 2 ),
                       Unit.PX );
        style.setTop( panel.getAbsoluteTop() + event.getY() - ( floatingPanel.getHeight() / 2 ),
                      Unit.PX );
        style.setZIndex( ZINDEX );
        return style;
    }

}