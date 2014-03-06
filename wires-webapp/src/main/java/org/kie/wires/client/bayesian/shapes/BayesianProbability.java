package org.kie.wires.client.bayesian.shapes;

import java.util.Map;

import org.kie.wires.client.shapes.EditableRectangle;

import com.emitrom.lienzo.client.core.shape.Rectangle;
import com.emitrom.lienzo.client.core.shape.Text;
import com.google.common.collect.Maps;

public class BayesianProbability extends EditableRectangle {

    private Map<Text, Rectangle> parentNode;
    private Map<Text, Rectangle> porcentualOptions;
    private Map<Text, Rectangle> porcentualValues;
    private Map<Map<Text, Rectangle>, Map<Text, Rectangle>> incomingNodes;

    public BayesianProbability(double width, double height, double positionXNode, double positionYNode) {
        super(width, height);
        super.init(positionXNode, positionYNode);
        this.parentNode = Maps.newHashMap();
        this.porcentualOptions = Maps.newHashMap();
        this.porcentualValues = Maps.newHashMap();
        this.incomingNodes = Maps.newHashMap();
    }

    public void buildGrid() {
        drawComponents(this.parentNode);
        drawComponents(this.porcentualOptions);
        drawComponents(this.porcentualValues);
        if (this.incomingNodes != null && !this.incomingNodes.isEmpty()) {
            for (Map.Entry<Map<Text, Rectangle>, Map<Text, Rectangle>> porc : incomingNodes.entrySet()) {
                drawComponents(porc.getValue());
                drawComponents(porc.getKey());
            }

        }
    }

    private void drawComponents(Map<Text, Rectangle> hash) {
        for (Map.Entry<Text, Rectangle> parent : hash.entrySet()) {
            add(parent.getValue());
            add(parent.getKey());
        }
    }

    public Map<Text, Rectangle> getParentNode() {
        return parentNode;
    }

    public void setParentNode(Map<Text, Rectangle> parentNode) {
        this.parentNode = parentNode;
    }

    public Map<Text, Rectangle> getPorcentualOptions() {
        return porcentualOptions;
    }

    public void setPorcentualOptions(Map<Text, Rectangle> porcentualOptions) {
        this.porcentualOptions = porcentualOptions;
    }

    public Map<Text, Rectangle> getPorcentualValues() {
        return porcentualValues;
    }

    public void setPorcentualValues(Map<Text, Rectangle> porcentualValues) {
        this.porcentualValues = porcentualValues;
    }

    public Map<Map<Text, Rectangle>, Map<Text, Rectangle>> getIncomingNodes() {
        return incomingNodes;
    }

    public void setIncomingNodes(Map<Map<Text, Rectangle>, Map<Text, Rectangle>> incomingNodes) {
        this.incomingNodes = incomingNodes;
    }

    

}
