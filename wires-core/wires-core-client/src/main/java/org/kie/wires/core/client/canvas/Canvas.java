package org.kie.wires.core.client.canvas;

import com.emitrom.lienzo.client.core.event.NodeMouseClickEvent;
import com.emitrom.lienzo.client.core.event.NodeMouseClickHandler;
import com.emitrom.lienzo.client.core.event.NodeMouseOverEvent;
import com.emitrom.lienzo.client.core.event.NodeMouseOverHandler;
import com.emitrom.lienzo.client.core.event.NodeMouseUpEvent;
import com.emitrom.lienzo.client.core.event.NodeMouseUpHandler;
import com.emitrom.lienzo.client.core.shape.GridLayer;
import com.emitrom.lienzo.client.core.shape.Group;
import com.emitrom.lienzo.client.core.shape.Layer;
import com.emitrom.lienzo.client.core.shape.Line;
import com.emitrom.lienzo.client.core.shape.Rectangle;
import com.emitrom.lienzo.client.core.shape.Text;
import com.emitrom.lienzo.client.widget.LienzoPanel;
import com.emitrom.lienzo.shared.core.types.ColorName;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.RootPanel;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.kie.wires.core.api.shapes.EditableShape;
import org.kie.wires.core.client.shapes.ProgressBar;
import org.kie.wires.core.client.util.ShapesUtils;

public class Canvas extends Composite implements RequiresResize {

    protected LienzoPanel panel;
    protected Layer canvasLayer;
    protected Layer menuLayer;
    public static final List<EditableShape> shapesInCanvas = new ArrayList<EditableShape>();
    public static ProgressBar progressBar;

    @PostConstruct
    public void init() {
        panel = new LienzoPanel(1000, 1000);


        initWidget(panel);
        
        canvasLayer = new Layer();
        menuLayer = new Layer();
        

        Line line1 = new Line(0, 0, 0, 0).setStrokeColor(ColorName.BLUE).setAlpha(0.5); // primary
        Line line2 = new Line(0, 0, 0, 0).setStrokeColor(ColorName.GREEN).setAlpha(0.5); // secondary
        line2.setDashArray(2, 2); // the secondary lines are dashed lines

        GridLayer gridLayer = new GridLayer(100, line1, 25, line2);
        panel.setBackgroundLayer(gridLayer);
        

        createMenu(menuLayer, canvasLayer);
        
        panel.getScene().add(menuLayer);
        panel.getScene().add(canvasLayer);
        
        
        
        
        KeyPressHandler myKeyPressedHandler = new KeyPressHandler() {

            public void onKeyPress(KeyPressEvent event) {

                if (event.getCharCode() == 'a') {
                    EditableShape shapeToRemove = null;
                    for (EditableShape shape : shapesInCanvas) {
                        if (shape.getId().equals(ShapesUtils.selectedShapeId)) {
                            shapeToRemove = shape;
                            shape.destroy();
                        }
                    }
                    if (shapeToRemove != null) {
                        shapesInCanvas.remove(shapeToRemove);
                    }

                }
                if (event.getCharCode() == 's') {
                    ShapesUtils.deselectAllShapes(shapesInCanvas);
                    canvasLayer.draw();
                }
            }
        };

        RootPanel.get().addDomHandler(myKeyPressedHandler, KeyPressEvent.getType());
        
        

    }
    
    private void createMenu(final Layer menuLayer, final Layer canvasLayer ){
        Rectangle menuBar = new Rectangle (400, 30, 3 );
        menuBar.setFillColor(ColorName.GRAY);
        menuBar.setX(10);
        menuBar.setY(5);
        
        final Group clearButtonGroup = new Group();
        clearButtonGroup.setX(15);
        clearButtonGroup.setY(10);
        
        final Rectangle clearButtonRectangle = new Rectangle(20, 20, 3);
        clearButtonRectangle.setFillColor(ColorName.AQUAMARINE);
       
        
        Text clearButtonText = new Text("C");
        clearButtonText.setFontSize(12);
        clearButtonText.setX(5);
        clearButtonText.setY(15);
        
        clearButtonGroup.add(clearButtonRectangle);
        clearButtonGroup.add(clearButtonText);
        
        menuLayer.add(menuBar);
        menuLayer.add(clearButtonGroup);
        
        
        clearButtonRectangle.addNodeMouseUpHandler(new NodeMouseUpHandler() {

            public void onNodeMouseUp(NodeMouseUpEvent event) {
                GWT.log("UP! ");
                clearButtonRectangle.setFillColor(ColorName.AQUAMARINE);
                canvasLayer.draw();
                menuLayer.draw();
            }
        });
        
        clearButtonRectangle.addNodeMouseOverHandler(new NodeMouseOverHandler() {

            public void onNodeMouseOver(NodeMouseOverEvent event) {
                GWT.log("Over! ");
                clearButtonRectangle.setFillColor(ColorName.AQUA);
                canvasLayer.draw();
                menuLayer.draw();
            }
        });
        
        clearButtonRectangle.addNodeMouseClickHandler(new NodeMouseClickHandler() {

            public void onNodeMouseClick(NodeMouseClickEvent event) {
                GWT.log("Click! ");
                clearButtonRectangle.setFillColor(ColorName.BLUEVIOLET);
                ShapesUtils.deselectAllShapes(shapesInCanvas);
                canvasLayer.draw();
                menuLayer.draw();
            }
        });
    }

    @Override
    public void onResize() {
//        int width = panel.getViewport().getWidth();
//        int height = panel.getViewport().getHeight();
//        if (width > 0 && height > 0) {
//            panel.setPixelSize(width, height);
//
//        } else {
//            panel.setPixelSize(800, 600);
//        }
        
    }

}
