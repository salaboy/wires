package org.kie.wires.core.client.canvas;

import javax.annotation.PostConstruct;

import com.emitrom.lienzo.client.core.shape.GridLayer;
import com.emitrom.lienzo.client.core.shape.Layer;
import com.emitrom.lienzo.client.core.shape.Line;
import com.emitrom.lienzo.client.widget.LienzoPanel;
import com.emitrom.lienzo.shared.core.types.ColorName;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RequiresResize;


public class Canvas extends Composite implements RequiresResize {
    
    protected LienzoPanel panel;
    protected Layer layer;
    
    
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
        // line
        Line line2 = new Line(0, 0, 0, 0).setStrokeColor(ColorName.GREEN).setAlpha(0.5); // secondary
        // line
        line2.setDashArray(2, 2); // the secondary lines are dashed lines

        GridLayer gridLayer = new GridLayer(100, line1, 25, line2);

        panel.setBackgroundLayer(gridLayer);

        gridLayer.moveToBottom();
        gridLayer.setListening(false);
        gridLayer.draw();
        
        layer.draw();

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
