package org.kie.wires.core.client.canvas;

import com.emitrom.lienzo.client.core.shape.GridLayer;
import com.emitrom.lienzo.client.core.shape.Layer;
import com.emitrom.lienzo.client.core.shape.Line;
import com.emitrom.lienzo.client.widget.LienzoPanel;
import com.emitrom.lienzo.shared.core.types.ColorName;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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
    protected Layer layer;
    public static final List<EditableShape> shapesInCanvas = new ArrayList<EditableShape>();
    public static ProgressBar progressBar;

    @PostConstruct
    public void init() {
        panel = new LienzoPanel(800, 600);
        
        initWidget(panel);
        layer = new Layer();
        panel.getScene().add(layer);

        panel.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                // ShapesUtils.deselectAllShapes(CanvasScreen.shapesInCanvas);
            }
        });

        Line line1 = new Line(0, 0, 0, 0).setStrokeColor(ColorName.BLUE).setAlpha(0.5); // primary
        Line line2 = new Line(0, 0, 0, 0).setStrokeColor(ColorName.GREEN).setAlpha(0.5); // secondary
        line2.setDashArray(2, 2); // the secondary lines are dashed lines

        GridLayer gridLayer = new GridLayer(100, line1, 25, line2);

        panel.setBackgroundLayer(gridLayer);

        gridLayer.moveToBottom();
        gridLayer.setListening(false);
        gridLayer.draw();

        layer.draw();
        panel.setFocus(true);
        
       KeyPressHandler myHandler = new KeyPressHandler(){

            public void onKeyPress(KeyPressEvent event) {
                
                if(event.getCharCode() == 'a' ){
                    EditableShape shapeToRemove =  null;
                    for(EditableShape shape : shapesInCanvas){
                        if(shape.getId().equals(ShapesUtils.selectedShapeId)){
                            shapeToRemove = shape;
                            shape.destroy();
                        }
                    }
                    if(shapeToRemove != null){
                        shapesInCanvas.remove(shapeToRemove);
                    }
                    
                }
                if(event.getCharCode() == 's' ){
                    ShapesUtils.deselectAllShapes(shapesInCanvas);
                    layer.draw();
                }
            }
        };
        
        RootPanel.get().addDomHandler(myHandler, KeyPressEvent.getType());
        

    }

    @Override
    public void onResize() {
        int height = getParent().getOffsetHeight();
        int width = getParent().getOffsetWidth();
        if (width > 0 && height > 0) {
            panel.setPixelSize(width, height);

        } else {
            panel.setPixelSize(800, 600);
        }
        layer.draw();
    }

}
