/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.kie.wires.client.bayesian.shapes;

import com.emitrom.lienzo.client.core.shape.Layer;
import com.emitrom.lienzo.client.core.shape.Rectangle;
import com.emitrom.lienzo.client.core.shape.Text;
import org.kie.wires.client.shapes.EditableRectangle;

/**
 *
 * @author salaboy
 */
public class EditableBayesianNode extends EditableRectangle {

    private Rectangle header;
    private Text text;
    private String nodeName;
    
    
    public EditableBayesianNode(String nodeName, double width, double height) {
        super(width, height);
        this.nodeName = nodeName;
    }
    
    public EditableBayesianNode(double width, double height) {
        super(width, height);
    }


    @Override
    public void init(double x, double y, Layer layer) {
        super.init(x, y, layer);
        int fontSize = 10;
        double height = 25;
        header = new Rectangle(getRectangle().getWidth(), height);
      
        text =  new Text(nodeName, "Times", fontSize);
        text.setX( 8 );
        text.setY( 15);
        add(header);
        add(text);
        
        
        
       
    }
    
    
    
}
