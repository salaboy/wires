package com.bayesian.network.api.factory;

import java.util.Map;

import javax.enterprise.event.Event;

import com.bayesian.network.api.events.ProbabilityEvent;
import com.bayesian.network.api.shapes.EditableBayesianProbability;
import com.bayesian.network.api.utils.BayesianUtils;
import com.bayesian.parser.client.model.BayesVariable;
import com.emitrom.lienzo.client.core.shape.Rectangle;
import com.emitrom.lienzo.client.core.shape.Text;
import com.emitrom.lienzo.shared.core.types.Color;
import com.google.common.collect.Maps;

public class ProbabilityFactory extends BaseFactory {

    private int positionXPorc;

    private Event<ProbabilityEvent> probabilityEvent;

    public ProbabilityFactory(BayesVariable node, Event<ProbabilityEvent> probabilityEvent) {
        this.probabilityEvent = probabilityEvent;
        init(node);
    }

    public void init(BayesVariable node) {
        int positionX = 350;
        int positionY = 80;
        int width = 65;
        int height = 100;

        EditableBayesianProbability bayesianProbabilityGrid = new EditableBayesianProbability(1200, 300, 0, 0);
        
        // draw parent node
        drawNodeSelected(node, positionX, positionY, width, height, bayesianProbabilityGrid);

        // draw porcentual options
        int positionXOptions = positionX + width;
        int heightOptions = height / node.getOutcomes().size();
        int widthOptions = width - 10;
        drawPorcentualOptions(node, positionXOptions, positionY, widthOptions, heightOptions, bayesianProbabilityGrid);

        // draw porcentual values
        int positionXValues = positionXOptions + widthOptions;
        int positionYValues = positionY;
        int heightPorcentualValue = height / node.getOutcomes().size();
        int widthPorcentualValue = widthOptions - 5;
        drawPorcentualValues(node, positionXValues, positionYValues, widthPorcentualValue, heightPorcentualValue,
                positionYValues, bayesianProbabilityGrid);

        // draw incoming nodes
        int widthIncoming = width + widthOptions;
        int heightIncoming = 25;
        int positionYIncoming = positionY - heightIncoming;
        drawIncomingNodes(node, positionX, positionYIncoming, widthIncoming, heightIncoming, widthPorcentualValue,
                widthOptions, heightOptions, bayesianProbabilityGrid);

        bayesianProbabilityGrid.buildGrid();
        probabilityEvent.fire(new ProbabilityEvent(null, bayesianProbabilityGrid));

    }

    private void drawNodeSelected(BayesVariable node, int positionX, int positionY, int width, int height,
            EditableBayesianProbability bayesianProbabilityGrid) {
        Map<Text, Rectangle> parentNode = Maps.newHashMap();
        Rectangle nodeSelected = super.drawComponent(Color.rgbToBrowserHexColor(183, 198, 201), positionX, positionY, width,
                height, Color.rgbToBrowserHexColor(183, 198, 201), 2);

        Text label = super.drawText(node.getName(), BayesianUtils.FONT_SIZE_TEXT_LABEL, positionX + 7, positionY + 54);
        parentNode.put(label, nodeSelected);
        bayesianProbabilityGrid.setParentNode(parentNode);

    }

    private void drawPorcentualOptions(BayesVariable node, int positionXOptions, int positionYOptions, int widthOptions,
            int heightOptions, EditableBayesianProbability bayesianProbabilityGrid) {
        Map<Text, Rectangle> porcentualOptions = Maps.newHashMap();
        for (String outcome : node.getOutcomes()) {
            Rectangle porcentualOption = super.drawComponent(Color.rgbToBrowserHexColor(200, 216, 203), positionXOptions,
                    positionYOptions, widthOptions, heightOptions, Color.rgbToBrowserHexColor(200, 216, 203), 0);

            Text porcentualLabel = super.drawText(outcome, BayesianUtils.FONT_SIZE_TEXT_LABEL, positionXOptions + 7,
                    positionYOptions + (20 / node.getOutcomes().size()) + 19);

            positionYOptions += heightOptions;
            porcentualOptions.put(porcentualLabel, porcentualOption);
        }
        bayesianProbabilityGrid.setPorcentualOptions(porcentualOptions);
    }

    private void drawPorcentualValues(BayesVariable node, int positionXValues, int positionYValues, int widthPorcentualValue,
            int heightPorcentualValue, int positionY, EditableBayesianProbability bayesianProbabilityGrid) {
        double probabilities[][] = (node.getGiven() != null && node.getGiven().size() > 1) ? BayesianUtils.orderListValues(
                node, node.getOutcomes().size()) : node.getProbabilities();
        Map<Text, Rectangle> porcentualValues = Maps.newHashMap();
        for (int i = 0; i < probabilities.length / node.getOutcomes().size(); i++) {
            for (int j = 0; j < node.getOutcomes().size(); j++) {
                Rectangle porcentual = super.drawComponent(Color.rgbToBrowserHexColor(255, 255, 255), positionXValues,
                        positionYValues, widthPorcentualValue, heightPorcentualValue,
                        Color.rgbToBrowserHexColor(183, 198, 201), 0);

                Text porcentualLabel = super.drawText(String.valueOf(probabilities[i][j]), BayesianUtils.FONT_SIZE_TEXT_LABEL,
                        positionXValues + 7, positionYValues + (20 / node.getOutcomes().size()) + 19);

                positionYValues += heightPorcentualValue;
                porcentualValues.put(porcentualLabel, porcentual);
            }
            positionYValues = positionY;
            positionXValues += widthPorcentualValue;
        }
        bayesianProbabilityGrid.setPorcentualValues(porcentualValues);
    }

    private void drawIncomingNodes(BayesVariable node, int positionXIncoming, int positionYIncoming, int widthIncoming,
            int heightIncoming, int widthPorcentualValue, int widthOptions, int heightOptions,
            EditableBayesianProbability bayesianProbabilityGrid) {
        String color = Color.rgbToBrowserHexColor(182, 199, 191);
        int incomingPosition = 0;
        Map<Map<Text, Rectangle>, Map<Text, Rectangle>> porcentualIncoming = Maps.newHashMap();
        int acountIterations = 0;
        int widthNode = 0;
        if (node.getIncomingNodes() != null && !node.getIncomingNodes().isEmpty()) {
            for (BayesVariable nod : node.getIncomingNodes()) {
                Map<Text, Rectangle> incomingNodes = Maps.newHashMap();
                Map<Text, Rectangle> porcentualValues = Maps.newHashMap();

                // draw label
                Rectangle incomingNode = super.drawComponent(color, positionXIncoming, positionYIncoming, widthIncoming,
                        heightIncoming, color, 0);
                Text incomingLabel = super.drawText(nod.getName(), BayesianUtils.FONT_SIZE_TEXT_LABEL, positionXIncoming + 10,
                        positionYIncoming + 19);

                incomingNodes.put(incomingLabel, incomingNode);

                // draw porcentual options
                positionXPorc = positionXIncoming + widthIncoming;

                if (incomingPosition == 0) {
                    for (int i = 0; i < (node.getProbabilities().length / node.getOutcomes().size()) / nod.getOutcomes().size(); i++) {
                        drawPorcentualIncoming(nod, positionYIncoming, heightIncoming, widthPorcentualValue, heightOptions,
                                bayesianProbabilityGrid, porcentualValues);
                        acountIterations += 1;
                    }
                } else {
                    int sizeOutcomesPrevIncomingNode = node.getIncomingNodes().get(incomingPosition - 1).getOutcomes().size();
                    int iter = acountIterations / nod.getOutcomes().size();
                    acountIterations = 0;

                    widthNode = (widthNode == 0) ? widthPorcentualValue * sizeOutcomesPrevIncomingNode : widthNode
                            * sizeOutcomesPrevIncomingNode;

                    for (int i = 0; i < iter; i++) {
                        drawPorcentualIncoming(nod, positionYIncoming, heightIncoming, widthNode, heightOptions,
                                bayesianProbabilityGrid, porcentualValues);
                        acountIterations += 1;
                    }
                }
                incomingPosition += 1;

                positionYIncoming -= heightIncoming;
                color = Color.rgbToBrowserHexColor(210, 204, 229);
                porcentualIncoming.put(incomingNodes, porcentualValues);
            }
            bayesianProbabilityGrid.setIncomingNodes(porcentualIncoming);
        }

    }

    private void drawPorcentualIncoming(BayesVariable nod, int positionYIncoming, int heightIncoming, int widthPorcentualValue,
            int heightOptions, EditableBayesianProbability bayesianProbabilityGrid, Map<Text, Rectangle> porcentualValues) {
        for (String out : nod.getOutcomes()) {
            Rectangle porcentual = super.drawComponent(Color.rgbToBrowserHexColor(200, 216, 203), positionXPorc,
                    positionYIncoming, widthPorcentualValue, heightIncoming, Color.rgbToBrowserHexColor(200, 216, 203), 0);

            Text porcentualLabel = super.drawText(out, BayesianUtils.FONT_SIZE_TEXT_LABEL, positionXPorc + 7,
                    positionYIncoming + 19);

            positionXPorc += widthPorcentualValue;
            porcentualValues.put(porcentualLabel, porcentual);
        }
    }

}
