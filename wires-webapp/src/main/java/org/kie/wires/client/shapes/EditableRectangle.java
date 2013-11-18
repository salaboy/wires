package org.kie.wires.client.shapes;

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
import com.emitrom.lienzo.client.core.event.NodeMouseOverEvent;
import com.emitrom.lienzo.client.core.event.NodeMouseOverHandler;
import com.emitrom.lienzo.client.core.shape.Circle;
import com.emitrom.lienzo.client.core.shape.Layer;
import com.emitrom.lienzo.client.core.shape.Rectangle;
import com.emitrom.lienzo.shared.core.types.ColorName;
import com.google.gwt.core.client.GWT;



public class EditableRectangle extends Rectangle implements EditableShape {
    private static final int CONTROL_TOP_LEFT = 0;
    private static final int CONTROL_BOTTOM_LEFT = 1;
    private static final int CONTROL_TOP_RIGHT = 2;
    private static final int CONTROL_BOTTOM_RIGHT = 3;

    private Rectangle topLeft;
    private Rectangle topRight;
    private Rectangle bottomLeft;
    private Rectangle bottomRight;
    
    private static final int MAGNET_TOP = 0;
    private static final int MAGNET_BOTTOM = 1;
    private static final int MAGNET_RIGHT = 2;
    private static final int MAGNET_LEFT = 3;

    private Circle topMagnet;
    private Circle rightMagnet;
    private Circle bottomMagnet;
    private Circle leftMagnet;
    
    private double dragEventStartX;
    private double dragEventStartY;
    
    
    private double startX;
    private double startY;
    private double startWidth;
    private double startHeight;

    public EditableRectangle(double width, double height) {
        super(width, height);
    }

    public EditableRectangle(double width, double height, double cornerRadius) {
        super(width, height, cornerRadius);
    }

    public void init(double x, double y) {
        setX( x );
        setY( y );

        addNodeMouseClickHandler(new NodeMouseClickHandler() {
            public void onNodeMouseClick(NodeMouseClickEvent nodeMouseClickEvent) {
                ShapesUtils.nodeMouseClickHandler(EditableRectangle.this);
                ShapesUtils.deselectAllOtherShapes(getLayer());
            }
        });
        
        addNodeMouseEnterHandler(new NodeMouseEnterHandler() {
            public void onNodeMouseEnter(NodeMouseEnterEvent nodeMouseEnterEvent) {
                ShapesUtils.nodeMouseEnterHandler(EditableRectangle.this);
                
            }
        });
        
         
         addNodeMouseOverHandler(new NodeMouseOverHandler() {
            public void onNodeMouseOver(NodeMouseOverEvent nodeMouseOverEvent) {
                ShapesUtils.nodeMouseOverHandler(EditableRectangle.this);
                
            }
        });
       
        addNodeMouseExitHandler(new NodeMouseExitHandler() {
            public void onNodeMouseExit(NodeMouseExitEvent nodeMouseExitEvent) {
                ShapesUtils.nodeMouseExitHandler(EditableRectangle.this);

            }
        });

        addNodeDragStartHandler(new NodeDragStartHandler() {
            public void onNodeDragStart(NodeDragStartEvent nodeDragStartEvent) {
                
                if (topLeft != null) {
                    hideDragPoints();
                }
                if( topMagnet != null){
                    hideMagnetPoints();
                }
            }
        });
    }

    public void showDragPoints() {
        if ( topLeft == null ) {
            // Can be null, if we enter, after an exit but the timer has not removed the points yet
            final Layer layer = getLayer();

            topLeft = new Rectangle(10, 10);
            topLeft.setFillColor(ShapesUtils.LIGHT_BLUE);
            createControlPoints(topLeft, layer, CONTROL_TOP_LEFT);

            bottomLeft = new Rectangle(10, 10);
            bottomLeft.setFillColor(ShapesUtils.LIGHT_BLUE);
            createControlPoints(bottomLeft, layer, CONTROL_BOTTOM_LEFT);

            topRight = new Rectangle(10, 10);
            topRight.setFillColor(ShapesUtils.LIGHT_BLUE);
            createControlPoints(topRight, layer, CONTROL_TOP_RIGHT);

            bottomRight = new Rectangle(10, 10);
            bottomRight.setFillColor(ShapesUtils.LIGHT_BLUE);
            createControlPoints(bottomRight, layer, CONTROL_BOTTOM_RIGHT);

            layer.draw();
        }
    }

    public void hideDragPoints() {
        GWT.log("hide");
        if ( topLeft != null ) {
            // can be null, afer the main Shape is dragged, and control points are forcibly removed
            Layer layer = getLayer();

            layer.remove( topLeft );
            topLeft = null;

            layer.remove( bottomLeft );
            bottomLeft = null;

            layer.remove( topRight );
            topRight = null;

            layer.remove( bottomRight );
            bottomRight = null;

            layer.draw();
        }

    }

    @Override
    public void hideMagnetPoints() {
        GWT.log("hide");
        if (topMagnet != null) {
            // can be null, afer the main Shape is dragged, and control points are forcibly removed
            Layer layer = getLayer();
            layer.remove(topMagnet);
            topMagnet = null;

            layer.remove(leftMagnet);
            leftMagnet = null;

            layer.remove(rightMagnet);
            rightMagnet = null;

            layer.remove(bottomMagnet);
            bottomMagnet = null;

            layer.draw();
        }

    }

    public void recordStartData(NodeDragStartEvent nodeDragStartEvent) {
        dragEventStartX = nodeDragStartEvent.getX();
        dragEventStartY = nodeDragStartEvent.getY();

        startX = getX();
        startY = getY();
        startHeight = getHeight();
        startWidth = getWidth();
    }

    private void createControlPoints(Rectangle rect, Layer layer, final int control) {
        switch( control ) {
            case CONTROL_TOP_LEFT:
                rect.setX( getX() - 5 )
                     .setY( getY() - 5 );
                break;
            case CONTROL_BOTTOM_LEFT:
                rect.setX( getX() - 5)
                     .setY( getY() + getHeight() - 5);
                break;
            case CONTROL_TOP_RIGHT:
                rect.setX( getX() + getWidth() - 5 )
                     .setY( getY() - 5);
                break;
            case CONTROL_BOTTOM_RIGHT:
                rect.setX( getX() + getWidth() - 5 )
                     .setY( getY() + getHeight()- 5 );
                break;
        }

        rect.setDraggable(true)
                .setStrokeWidth(1)
                .setStrokeColor(ColorName.BLACK);

        rect.addNodeDragStartHandler(new NodeDragStartHandler() {
            public void onNodeDragStart(NodeDragStartEvent nodeDragStartEvent) {
                GWT.log("drag start");
                recordStartData(nodeDragStartEvent);
            }
        } );

        rect.addNodeDragMoveHandler(new NodeDragMoveHandler() {
            public void onNodeDragMove(NodeDragMoveEvent nodeDragMoveEvent) {
                Layer layer = getLayer();
                nodeDragMove(EditableRectangle.this, control, nodeDragMoveEvent, layer);
                if (topMagnet != null) {
                    placeMagnetPoints(topMagnet, layer, MAGNET_TOP);
                    placeMagnetPoints(leftMagnet, layer, MAGNET_LEFT);
                    placeMagnetPoints(rightMagnet, layer, MAGNET_RIGHT);
                    placeMagnetPoints(bottomMagnet, layer, MAGNET_BOTTOM);
                }
                layer.draw();

            }
            
        } );
        layer.add( rect );
    }

    public static void nodeDragMove(EditableRectangle rect, int control, NodeDragMoveEvent nodeDragMoveEvent, Layer layer) {


        double deltaX = nodeDragMoveEvent.getX() - rect.getDragEventStartX();
        double deltaY = nodeDragMoveEvent.getY() - rect.getDragEventStartY();
        
        switch( control ) {
            case CONTROL_TOP_LEFT:
                rect.setX( rect.getStartX() + deltaX );
                rect.setY( rect.getStartY() + deltaY );

                rect.setWidth( rect.getStartWidth() - deltaX );
                rect.setHeight( rect.getStartHeight() - deltaY );
                break;
            case CONTROL_BOTTOM_LEFT:
                rect.setX( rect.getStartX() + deltaX );

                rect.setWidth( rect.getStartWidth() - deltaX );
                rect.setHeight( rect.getStartHeight() + deltaY );
                break;
            case CONTROL_TOP_RIGHT:
                rect.setY( rect.getStartY() + deltaY );

                rect.setWidth( rect.getStartWidth() + deltaX );
                rect.setHeight( rect.getStartHeight() - deltaY );
                break;
            case CONTROL_BOTTOM_RIGHT:
                rect.setWidth( rect.getStartWidth() + deltaX );
                rect.setHeight( rect.getStartHeight() + deltaY );
                break;
        }

        
        
        switch( control ) {
            case CONTROL_TOP_LEFT:
                rect.getBottomLeft().setX( rect.getX() - 5 );
                rect.getTopRight().setY( rect.getY() - 5 );
                break;
            case CONTROL_BOTTOM_LEFT:
                rect.getTopLeft().setX( rect.getX() -5 );
                rect.getBottomRight().setY( rect.getY() + rect.getHeight() - 5 );
                break;
            case CONTROL_TOP_RIGHT:
                rect.getTopLeft().setY( rect.getY() - 5 );
                rect.getBottomRight().setX( rect.getX() + rect.getWidth() - 5);
                break;
            case CONTROL_BOTTOM_RIGHT:
                rect.getBottomLeft().setY( rect.getY() + rect.getHeight() - 5 );
                rect.getTopRight().setX( rect.getX() + rect.getWidth() - 5 );
                break;
        }

    }

    public void showMagnetsPoints() {
        if(topMagnet == null){
            final Layer layer = getLayer();
            
            topMagnet = new Circle(5);
            topMagnet.addNodeMouseEnterHandler(new NodeMouseEnterHandler() {
                public void onNodeMouseEnter(NodeMouseEnterEvent nodeMouseEnterEvent) {
                    GWT.log("on a top magnet!!!");
                }
            });
            topMagnet.setFillColor(ColorName.YELLOW);
            placeMagnetPoints(topMagnet, layer, MAGNET_TOP);
            
            bottomMagnet = new Circle(5);
            bottomMagnet.addNodeMouseEnterHandler(new NodeMouseEnterHandler() {
                public void onNodeMouseEnter(NodeMouseEnterEvent nodeMouseEnterEvent) {
                    GWT.log("on a bottom magnet!!!");
                }
            });
            bottomMagnet.setFillColor(ColorName.YELLOW);
            placeMagnetPoints(bottomMagnet, layer, MAGNET_BOTTOM);
            
            leftMagnet = new Circle(5);
            leftMagnet.addNodeMouseEnterHandler(new NodeMouseEnterHandler() {
                public void onNodeMouseEnter(NodeMouseEnterEvent nodeMouseEnterEvent) {
                    GWT.log("on a left magnet!!!");
                }
            });
            leftMagnet.setFillColor(ColorName.YELLOW);
            placeMagnetPoints(leftMagnet, layer, MAGNET_LEFT);
            
            rightMagnet = new Circle(5);
            rightMagnet.addNodeMouseEnterHandler(new NodeMouseEnterHandler() {
                public void onNodeMouseEnter(NodeMouseEnterEvent nodeMouseEnterEvent) {
                    GWT.log("on a right magnet!!!");
                }
            });
            rightMagnet.setFillColor(ColorName.YELLOW);
            placeMagnetPoints(rightMagnet, layer, MAGNET_RIGHT);
            
            layer.draw();
        }
    }

    private void placeMagnetPoints(Circle magnet, Layer layer, int control){
        switch( control ) {
            case MAGNET_TOP: 
                magnet.setX(getX() + (getWidth() / 2) );
                magnet.setY(getY()  );
                break;
            case MAGNET_BOTTOM: 
                magnet.setX(getX() + (getWidth() / 2) );
                magnet.setY(getY() + getHeight() );
                break;
           case MAGNET_RIGHT: 
                magnet.setX(getX() + getWidth() );
                magnet.setY(getY() + (getHeight() / 2 ) );
                break;
           case MAGNET_LEFT: 
                magnet.setX(getX() );
                magnet.setY(getY() + (getHeight() / 2 ) );
                break;
        }
        layer.add(magnet);
    }
    
    public Rectangle getTopLeft() {
        return topLeft;
    }

    public void setTopLeft(Rectangle topLeft) {
        this.topLeft = topLeft;
    }

    public Rectangle getTopRight() {
        return topRight;
    }

    public void setTopRight(Rectangle topRight) {
        this.topRight = topRight;
    }

    public Rectangle getBottomLeft() {
        return bottomLeft;
    }

    public void setBottomLeft(Rectangle bottomLeft) {
        this.bottomLeft = bottomLeft;
    }

    public Rectangle getBottomRight() {
        return bottomRight;
    }

    public void setBottomRight(Rectangle bottomRight) {
        this.bottomRight = bottomRight;
    }

    public double getDragEventStartX() {
        return dragEventStartX;
    }

    public void setDragEventStartX(double dragEventStartX) {
        this.dragEventStartX = dragEventStartX;
    }

    public double getDragEventStartY() {
        return dragEventStartY;
    }

    public void setDragEventStartY(double dragEventStartY) {
        this.dragEventStartY = dragEventStartY;
    }

    public double getStartX() {
        return startX;
    }

    public void setStartX(double startX) {
        this.startX = startX;
    }

    public double getStartY() {
        return startY;
    }

    public void setStartY(double startY) {
        this.startY = startY;
    }

    public double getStartWidth() {
        return startWidth;
    }

    public void setStartWidth(double startWidth) {
        this.startWidth = startWidth;
    }

    public double getStartHeight() {
        return startHeight;
    }

    public void setStartHeight(double startHeight) {
        this.startHeight = startHeight;
    }

}
