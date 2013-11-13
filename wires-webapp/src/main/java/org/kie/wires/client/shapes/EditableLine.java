/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kie.wires.client.shapes;

import com.emitrom.lienzo.client.core.event.NodeDragEndEvent;
import com.emitrom.lienzo.client.core.event.NodeDragEndHandler;
import com.emitrom.lienzo.client.core.event.NodeDragMoveEvent;
import com.emitrom.lienzo.client.core.event.NodeDragMoveHandler;
import com.emitrom.lienzo.client.core.event.NodeDragStartEvent;
import com.emitrom.lienzo.client.core.event.NodeDragStartHandler;
import com.emitrom.lienzo.client.core.event.NodeMouseClickEvent;
import com.emitrom.lienzo.client.core.event.NodeMouseClickHandler;
import com.emitrom.lienzo.client.core.event.NodeMouseEnterEvent;
import com.emitrom.lienzo.client.core.event.NodeMouseEnterHandler;
import com.emitrom.lienzo.client.core.event.NodeMouseExitEvent;
import com.emitrom.lienzo.client.core.event.NodeMouseExitHandler;
import com.emitrom.lienzo.client.core.shape.Layer;
import com.emitrom.lienzo.client.core.shape.Line;
import com.emitrom.lienzo.client.core.shape.Rectangle;
import com.emitrom.lienzo.client.core.types.Point2DArray;
import com.emitrom.lienzo.shared.core.types.ColorName;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;

/**
 *
 * @author salaboy
 */
public class EditableLine extends Line implements EditableShape {

    private Timer t;

    private Rectangle start;
    private Rectangle end;

    private double dragEventStartX;
    private double dragEventStartY;

    private double dragEventEndX;
    private double dragEventEndY;

    private double initialStartPointX;
    private double initialStartPointY;
    private double initialEndPointX;
    private double initialEndPointY;

    public EditableLine(double x1, double y1, double x2, double y2) {
        super(x1, y1, x2, y2);
    }

    @Override
    public void init(double x, double y) {

        setX(x);
        setY(y);

        addNodeMouseClickHandler(new NodeMouseClickHandler() {
            public void onNodeMouseClick(NodeMouseClickEvent nodeMouseClickEvent) {
                ShapesUtils.nodeMouseClickHandler(EditableLine.this);
                ShapesUtils.deselectAllOtherShapes(getLayer());
            }
        });

        addNodeMouseExitHandler(new NodeMouseExitHandler() {

            public void onNodeMouseExit(NodeMouseExitEvent nodeMouseExitEvent) {
                ShapesUtils.nodeMouseExitHandler(EditableLine.this);
            }

        ;
        } );

        addNodeDragStartHandler(new NodeDragStartHandler() {
            public void onNodeDragStart(NodeDragStartEvent nodeDragStartEvent) {
                if (start != null) {
                    hideDragPoints();
                }
            }
        });

        
    }

   

    @Override
    public void showDragPoints() {
        if(start == null){
            final Layer layer = getLayer();
            start = new Rectangle(10, 10);
            end = new Rectangle(10, 10);
            start.setFillColor(ShapesUtils.LIGHT_BLUE);
            end.setFillColor(ShapesUtils.LIGHT_BLUE);
            createControlPoints(start, end, getLayer());

            layer.draw();
        }
    }

    @Override
    public void hideDragPoints() {
        GWT.log("hide");
        if (start != null) {
            // can be null, afer the main Shape is dragged, and control points are forcibly removed
            Layer layer = getLayer();

            layer.remove(start);
            start = null;

            layer.remove(end);
            end = null;

            layer.draw();
        }
    }

    private void createControlPoints(final Rectangle start, final Rectangle end, final Layer layer) {
        Point2DArray array = getPoints();
        start.setX(getX() - 5);
        start.setY(getY() - 5 );

        start.addNodeDragStartHandler(new NodeDragStartHandler() {
            public void onNodeDragStart(NodeDragStartEvent nodeDragStartEvent) {
                dragEventStartX = nodeDragStartEvent.getX();
                dragEventStartY = nodeDragStartEvent.getY();
                initialStartPointX = getPoints().getPoint(0).getX();
                initialStartPointY = getPoints().getPoint(0).getY();
            }
        });

        start.addNodeDragMoveHandler(new NodeDragMoveHandler() {
            public void onNodeDragMove(NodeDragMoveEvent nodeDragMoveEvent) {
                double deltaX = nodeDragMoveEvent.getX() - dragEventStartX;
                double deltaY = nodeDragMoveEvent.getY() - dragEventStartY;

                Point2DArray array = getPoints();
                array.getPoint(0).setX(initialStartPointX + deltaX);
                array.getPoint(0).setY(initialStartPointY + deltaY);

                layer.draw();
            }
        });

        end.setX(getX() + array.getPoint(1).getX() - 5);
        end.setY(getY() + array.getPoint(1).getY() - 5);

        end.addNodeDragStartHandler(new NodeDragStartHandler() {
            public void onNodeDragStart(NodeDragStartEvent nodeDragStartEvent) {
                dragEventEndX = nodeDragStartEvent.getX();
                dragEventEndY = nodeDragStartEvent.getY();
                initialEndPointX = getPoints().getPoint(1).getX();
                initialEndPointY = getPoints().getPoint(1).getY();
            }
        });

        end.addNodeDragMoveHandler(new NodeDragMoveHandler() {
            public void onNodeDragMove(NodeDragMoveEvent nodeDragMoveEvent) {
                double deltaX = nodeDragMoveEvent.getX() - dragEventEndX;
                double deltaY = nodeDragMoveEvent.getY() - dragEventEndY;

                Point2DArray array = getPoints();
                array.getPoint(1).setX(initialEndPointX + deltaX);
                array.getPoint(1).setY(initialEndPointY + deltaY);

                layer.draw();
            }
        });

        start.setDraggable(true).setStrokeWidth(1)
                .setStrokeColor(ColorName.BLACK);
        end.setDraggable(true)
                .setStrokeWidth(1)
                .setStrokeColor(ColorName.BLACK);
        layer.add(start);
        layer.add(end);

    }

    public void showMagnetsPoints() {
        
    }

}
