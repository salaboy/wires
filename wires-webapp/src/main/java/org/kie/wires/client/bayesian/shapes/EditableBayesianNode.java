package org.kie.wires.client.bayesian.shapes;

import java.util.List;
import java.util.Map;

import org.kie.wires.client.shapes.WiresRectangle;

import com.emitrom.lienzo.client.core.shape.Rectangle;
import com.emitrom.lienzo.client.core.shape.Text;
import com.google.common.collect.Maps;

/**
 * 
 * @author salaboy
 */
public class EditableBayesianNode extends WiresRectangle {

    private Rectangle header;
    private Text textHeader;
    private Map<Text, List<Rectangle>> porcentualBars;

    public EditableBayesianNode(double width, double height, double positionXNode, double positionYNode,
            String fillColor) {
        super(width, height);
        super.init(positionXNode, positionYNode);
        this.porcentualBars = Maps.newHashMap();
        super.getRectangle().setFillColor(fillColor);
    }

    public void buildNode() {
        add(this.header);
        add(this.textHeader);
        for (Map.Entry<Text, List<Rectangle>> porcenualBar : this.porcentualBars.entrySet()) {
            for (Rectangle rec : porcenualBar.getValue()) {
                add(rec);
            }
            add(porcenualBar.getKey());
        }
    }

    public Rectangle getParentNode() {
        return super.getRectangle();
    }

    public Rectangle getHeader() {
        return header;
    }

    public void setHeader(Rectangle header) {
        this.header = header;
        // add(header);
    }

    public Text getTextHeader() {
        return textHeader;
    }

    public void setTextHeader(Text textHeader) {
        this.textHeader = textHeader;
        // add(textHeader);
    }

    public Map<Text, List<Rectangle>> getPorcentualsBar() {
        return porcentualBars;
    }

    public void setPorcentualBars(Map<Text, List<Rectangle>> porcentualBars) {
        this.porcentualBars = porcentualBars;
        // for (Map.Entry<Text, List<Rectangle>> porcenualBar :
        // porcentualBars.entrySet()) {
        // for (Rectangle rec : porcenualBar.getValue()) {
        // add(rec);
        // }
        // add(porcenualBar.getKey());
        // }
    }

}
