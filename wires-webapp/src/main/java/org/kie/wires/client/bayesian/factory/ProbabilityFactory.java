package org.kie.wires.client.bayesian.factory;

import org.kie.wires.client.util.BayesianUtils;

import com.emitrom.lienzo.client.core.shape.Group;
import com.emitrom.lienzo.client.core.shape.Layer;
import com.emitrom.lienzo.shared.core.types.Color;
import com.xstream.bayesian.client.model.BayesVariable;

public class ProbabilityFactory extends BaseFactory {

    private int positionXPorc;

    public ProbabilityFactory(BayesVariable node, Group group, Layer layer) {
        init(node, group, layer);
    }

    public void init(BayesVariable node, Group group, Layer layer) {
        int positionX = 350;
        int positionY = 75;
        int width = 65;
        int height = 100;

        // draw parent node
        drawNodeSelected(node, positionX, positionY, width, height,  layer);

        // draw porcentual options
        int positionXOptions = positionX + width;
        int positionYOptions = positionY;
        int heightOptions = height / node.getOutcomes().size();
        int widthOptions = width - 10;
        drawPorcentualOptions(node, positionXOptions, positionYOptions, widthOptions, heightOptions,  layer);

        // draw porcentual values
        int positionXValues = positionXOptions + widthOptions;
        int positionYValues = positionY;
        int heightPorcentualValue = height / node.getOutcomes().size();
        int widthPorcentualValue = widthOptions - 5;
        drawPorcentualValues(node, positionXValues, positionYValues, widthPorcentualValue, heightPorcentualValue,  layer,
                positionYValues);

        // draw incoming nodes
        int widthIncoming = width + widthOptions;
        int heightIncoming = 25;
        int positionXIncoming = positionX;
        int positionYIncoming = positionY - heightIncoming;
        drawIncomingNodes(node, positionXIncoming, positionYIncoming, widthIncoming, heightIncoming, layer,
                widthPorcentualValue, widthOptions, heightOptions);
    }

    private void drawNodeSelected(BayesVariable node, int positionX, int positionY, int width, int height, 
            Layer layer) {
        super.drawComponent(Color.rgbToBrowserHexColor(183, 198, 201), positionX, positionY, width, height,
                Color.rgbToBrowserHexColor(183, 198, 201), false);

        super.drawText(BayesianUtils.COLOR_TEXT_LABEL, positionX + 2, positionY + 35, width, height,
                BayesianUtils.COLOR_TEXT_LABEL, node.getName(), BayesianUtils.FONT_SIZE_TEXT_LABEL);
    }

    private void drawPorcentualOptions(BayesVariable node, int positionXOptions, int positionYOptions, int widthOptions,
            int heightOptions, Layer layer) {
        for (String outcome : node.getOutcomes()) {
            super.drawComponent(Color.rgbToBrowserHexColor(200, 216, 203), positionXOptions, positionYOptions, widthOptions,
                    heightOptions, Color.rgbToBrowserHexColor(200, 216, 203), false);

            super.drawText(BayesianUtils.COLOR_TEXT_LABEL, positionXOptions + 2, positionYOptions
                    + (20 / node.getOutcomes().size()), widthOptions, heightOptions, BayesianUtils.COLOR_TEXT_LABEL, outcome,
                    BayesianUtils.FONT_SIZE_TEXT_LABEL);

            positionYOptions += heightOptions;
        }
    }

    private void drawPorcentualValues(BayesVariable node, int positionXValues, int positionYValues, int widthPorcentualValue,
            int heightPorcentualValue,  Layer layer, int positionY) {
        double probabilities[][] = (node.getGiven() != null && node.getGiven().size() > 1) ? orderListValues(node, node
                .getOutcomes().size()) : node.getProbabilities();

        for (int i = 0; i < probabilities.length / node.getOutcomes().size(); i++) {
            for (int j = 0; j < node.getOutcomes().size(); j++) {
                super.drawComponent(Color.rgbToBrowserHexColor(255, 255, 255), positionXValues, positionYValues,
                        widthPorcentualValue, heightPorcentualValue, Color.rgbToBrowserHexColor(183, 198, 201),
                        false);

                super.drawText(BayesianUtils.COLOR_TEXT_LABEL, positionXValues + 2, positionYValues
                        + (20 / node.getOutcomes().size()), widthPorcentualValue, heightPorcentualValue,
                        BayesianUtils.COLOR_TEXT_LABEL, String.valueOf(probabilities[i][j]), BayesianUtils.FONT_SIZE_TEXT_LABEL);

                positionYValues += heightPorcentualValue;
            }
            positionYValues = positionY;
            positionXValues += widthPorcentualValue;
        }
    }

    private double[][] orderListValues(BayesVariable node, int outcomesSize) {
        double[][] probabilities = node.getProbabilities();
        double[][] valuesSorted = new double[probabilities.length][probabilities.length];
        int middle = probabilities.length / outcomesSize;
        int sizeFirstIncoming = node.getIncomingNodes().get(0).getOutcomes().size();
        int secondPart = middle / sizeFirstIncoming;
        int k = 0;
        for (int i = 0; i < middle / 2; i++) {
            for (int j = 0; j < outcomesSize; j++) {
                valuesSorted[k][j] = probabilities[i][j];
                valuesSorted[k + 1][j] = probabilities[secondPart + i][j];
                // TODO refactor this part
                if (sizeFirstIncoming == 3) {
                    valuesSorted[k + 2][j] = probabilities[secondPart * 2 + i][j];
                } else if (sizeFirstIncoming == 4) {
                    valuesSorted[k + 2][j] = probabilities[secondPart * 2 + i][j];
                    valuesSorted[k + 3][j] = probabilities[secondPart * 3 + i][j];
                }
            }
            k += sizeFirstIncoming;
        }
        return valuesSorted;
    }

    private void drawIncomingNodes(BayesVariable node, int positionXIncoming, int positionYIncoming, int widthIncoming,
            int heightIncoming, Layer layer, int widthPorcentualValue, int widthOptions, int heightOptions) {
        String color = Color.rgbToBrowserHexColor(182, 199, 191);
        int incomingPosition = 0;
        if (node.getIncomingNodes() != null && !node.getIncomingNodes().isEmpty()) {
            for (BayesVariable nod : node.getIncomingNodes()) {

                // draw label
                super.drawComponent(color, positionXIncoming, positionYIncoming, widthIncoming, heightIncoming, color, false);

                super.drawText(BayesianUtils.COLOR_TEXT_LABEL, positionXIncoming + 5, positionYIncoming, widthIncoming,
                        heightIncoming, BayesianUtils.COLOR_TEXT_LABEL, nod.getName(), BayesianUtils.FONT_SIZE_TEXT_LABEL);

                // draw porcentual options
                positionXPorc = positionXIncoming + widthIncoming;

                if (incomingPosition == 0) {
                    for (int i = 0; i < (node.getProbabilities().length / node.getOutcomes().size()) / nod.getOutcomes().size(); i++) {
                        drawPorcentualIncoming(nod, positionYIncoming, heightIncoming, layer, widthPorcentualValue,
                                widthOptions, heightOptions);
                    }
                } else {
                    int sizeOutcomesPrevIncomingNode = node.getIncomingNodes().get(incomingPosition - 1).getOutcomes().size();
                    drawPorcentualIncoming(nod, positionYIncoming, heightIncoming, layer, widthPorcentualValue
                            * sizeOutcomesPrevIncomingNode, widthOptions, heightOptions);
                }
                incomingPosition += 1;

                positionYIncoming -= heightIncoming;
                color = Color.rgbToBrowserHexColor(210, 204, 229);

            }
        }
    }

    private void drawPorcentualIncoming(BayesVariable nod, int positionYIncoming, int heightIncoming,  Layer layer,
            int widthPorcentualValue, int widthOptions, int heightOptions) {
        for (String out : nod.getOutcomes()) {
            super.drawComponent(Color.rgbToBrowserHexColor(200, 216, 203), positionXPorc, positionYIncoming,
                    widthPorcentualValue, heightIncoming, Color.rgbToBrowserHexColor(200, 216, 203), false);

            super.drawText(BayesianUtils.COLOR_TEXT_LABEL, positionXPorc + 2, positionYIncoming, widthOptions, heightOptions,
                    BayesianUtils.COLOR_TEXT_LABEL, out, BayesianUtils.FONT_SIZE_TEXT_LABEL);

            positionXPorc += widthPorcentualValue;

        }
    }

}
