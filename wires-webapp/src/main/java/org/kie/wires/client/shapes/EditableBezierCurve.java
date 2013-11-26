package org.kie.wires.client.shapes;

import com.emitrom.lienzo.client.core.event.NodeDragEndEvent;
import com.emitrom.lienzo.client.core.event.NodeDragEndHandler;
import com.emitrom.lienzo.client.core.event.NodeDragMoveEvent;
import com.emitrom.lienzo.client.core.event.NodeDragMoveHandler;
import com.emitrom.lienzo.client.core.event.NodeDragStartEvent;
import com.emitrom.lienzo.client.core.event.NodeDragStartHandler;
import com.emitrom.lienzo.client.core.event.NodeMouseEnterEvent;
import com.emitrom.lienzo.client.core.event.NodeMouseEnterHandler;
import com.emitrom.lienzo.client.core.event.NodeMouseExitEvent;
import com.emitrom.lienzo.client.core.event.NodeMouseExitHandler;
import com.emitrom.lienzo.client.core.shape.BezierCurve;
import com.emitrom.lienzo.client.core.shape.Layer;
import com.emitrom.lienzo.client.core.shape.Line;
import com.emitrom.lienzo.client.core.shape.Rectangle;
import com.emitrom.lienzo.client.core.shape.Shape;
import com.emitrom.lienzo.client.core.types.Point2D;
import com.emitrom.lienzo.client.core.types.Point2DArray;
import com.emitrom.lienzo.shared.core.types.ColorName;
import java.util.List;
import org.kie.wires.client.util.collision.Projection;
import org.kie.wires.client.util.collision.Vector;

public class EditableBezierCurve extends BezierCurve implements EditableShape {

    private Rectangle groupStart;
    private Rectangle groupC1;
    private Rectangle groupC2;
    private Rectangle groupEnd;

    private Line line1;
    private Line line2;



    // These are used to calculate the drag delta, as applied to the lines and rectangles
    private double controlStartX;
    private double controlStartY;
    private double lineStartX;
    private double lineStartY;
    private double dragEventStartX;
    private double dragEventStartY;

    public EditableBezierCurve(double x,
                               double y,
                               double controlX1,
                               double controlY1,
                               double controlX2,
                               double controlY2,
                               double endX,
                               double endY) {
        super(x, y, controlX1, controlY1, controlX2, controlY2, endX, endY);
    }

    public void init(double x, double y) {
        initControlPoints(x, y);

        addNodeMouseEnterHandler(new NodeMouseEnterHandler() {
            public void onNodeMouseEnter(NodeMouseEnterEvent nodeMouseEnterEvent) {
                ShapesUtils.nodeMouseClickHandler(EditableBezierCurve.this);
            }
        });

        addNodeMouseExitHandler(new NodeMouseExitHandler() {

            public void onNodeMouseExit(NodeMouseExitEvent nodeMouseExitEvent) {
                ShapesUtils.nodeMouseExitHandler(EditableBezierCurve.this);
            }

            ;
        } );

        addNodeDragStartHandler(new NodeDragStartHandler() {
            public void onNodeDragStart(NodeDragStartEvent nodeDragStartEvent) {
              
                if (groupStart != null) {
                    hideDragPoints();
                }

                controlStartX = nodeDragStartEvent.getX();
                controlStartY = nodeDragStartEvent.getY();
            }
        });

        addNodeDragEndHandler(new NodeDragEndHandler() {
            public void onNodeDragEnd(NodeDragEndEvent nodeDragEndEvent) {
                final Point2DArray array = getControlPoints();

                double deltaX = nodeDragEndEvent.getX() - controlStartX;
                double deltaY = nodeDragEndEvent.getY() - controlStartY;

                Point2D p0 = array.getPoint(0);
                double x =  p0.getX();
                double y =  p0.getY();
                initControlPoints(x + deltaX, y + deltaY);
            }
        });
    }

    public void initControlPoints(double x, double y) {
        final Point2DArray array = getControlPoints();
        Point2D p0 = array.getPoint(0);
        Point2D p1 = array.getPoint(1);
        Point2D p2 = array.getPoint(2);
        Point2D p3 = array.getPoint(3);

        p0.setX( p0.getX() + x);
        p0.setY( p0.getY() + y);

        p1.setX( p1.getX() + x);
        p1.setY( p1.getY() + y);

        p2.setX( p2.getX() + x);
        p2.setY( p2.getY() + y);

        p3.setX( p3.getX() + x);
        p3.setY( p3.getY() + y);
    }

   
    public void showDragPoints() {
        if (groupStart == null) {
            final Point2DArray array = getControlPoints();

            final Layer layer = getLayer();

            line1 = new Line( array.getPoint( 0 ).getX(), array.getPoint( 0 ).getY(), array.getPoint( 1 ).getX(), array.getPoint( 1 ).getY() )
                    .setStrokeWidth(1)
                    .setStrokeColor(ColorName.BLACK);  // primary line
            layer.add( line1 );

            groupStart = new Rectangle(6, 6);
            createControlPoints( groupStart, array.getPoint( 0 ), line1.getPoints().getPoint( 0 ), layer );

            groupC1 = new Rectangle(6, 6);
            createControlPoints( groupC1, array.getPoint( 1 ), line1.getPoints().getPoint( 1 ), layer );

            line2 = new Line( array.getPoint( 3 ).getX(), array.getPoint( 3 ).getY(), array.getPoint( 2 ).getX(), array.getPoint( 2 ).getY() )
                    .setStrokeWidth(1)
                    .setStrokeColor(ColorName.BLACK);  // primary line
            layer.add( line2 );

            groupC2 = new Rectangle(6, 6);
            createControlPoints( groupC2, array.getPoint( 2 ), line2.getPoints().getPoint( 1 ), layer );

            groupEnd = new Rectangle(6, 6);
            createControlPoints( groupEnd, array.getPoint( 3 ), line2.getPoints().getPoint( 0 ), layer );

            layer.draw();
        }
    }

    public void hideDragPoints() {
        if ( groupStart != null ) {
            Layer layer = getLayer();

            layer.remove( groupStart );
            groupStart = null;

            layer.remove( groupEnd );
            groupStart = null;

            layer.remove( groupC1 );
            groupC1 = null;

            layer.remove( groupC2 );
            groupC2 = null;

            layer.remove( line1 );
            line1 = null;

            layer.remove( line2 );
            line2 = null;

            layer.draw();
        }
    }

    private void createControlPoints( final Rectangle rect,
                                      final Point2D controlPoint,
                                      final Point2D linePoint,
                                      final Layer layer ) {
        rect.setStrokeWidth(1)
            .setStrokeColor(ColorName.BLACK)
            .setX(controlPoint.getX())
            .setY(controlPoint.getY())
            .setDraggable(true);

        rect.addNodeMouseEnterHandler(new NodeMouseEnterHandler() {
            public void onNodeMouseEnter(NodeMouseEnterEvent nodeMouseEnterEvent) {
                ShapesUtils.nodeMouseClickHandler(EditableBezierCurve.this);
            }
        });

        rect.addNodeMouseExitHandler(new NodeMouseExitHandler() {
            public void onNodeMouseExit(NodeMouseExitEvent nodeMouseExitEvent) {
                ShapesUtils.nodeMouseExitHandler(EditableBezierCurve.this);
            }
        });

        rect.addNodeDragStartHandler( new NodeDragStartHandler() {
            public void onNodeDragStart( NodeDragStartEvent nodeDragStartEvent ) {
                dragEventStartX = nodeDragStartEvent.getX();
                dragEventStartY = nodeDragStartEvent.getY();

                controlStartX = controlPoint.getX();
                controlStartY = controlPoint.getY();
                lineStartX = linePoint.getX();
                lineStartY = linePoint.getY();
            }
        } );

        rect.addNodeDragMoveHandler( new NodeDragMoveHandler() {
            public void onNodeDragMove( NodeDragMoveEvent nodeDragMoveEvent ) {
                double deltaX = nodeDragMoveEvent.getX() - dragEventStartX;
                double deltaY = nodeDragMoveEvent.getY() - dragEventStartY;

                controlPoint.setX( controlStartX + deltaX );
                controlPoint.setY( controlStartY + deltaY );

                linePoint.setX( lineStartX + deltaX );
                linePoint.setY( lineStartY + deltaY );

                layer.draw();
            }
        } );

        layer.add( rect );
    }

    public void showMagnetsPoints() {
        
    }

    public void hideMagnetPoints() {
        
    }

    public boolean collidesWith(EditableShape shape) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean separationOnAxes(List<Vector> axes, EditableShape shape) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<Vector> getAxes() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Projection project(Vector axis) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean isBeingDragged() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getId() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public double getCurrentDragX() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public double getCurrentDragY() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean isBeingResized() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<Shape> getMagnets() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
