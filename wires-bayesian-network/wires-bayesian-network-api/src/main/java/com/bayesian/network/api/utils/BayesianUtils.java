package com.bayesian.network.api.utils;

import java.util.Map;

import org.kie.wires.core.client.util.ShapesUtils;

import com.bayesian.parser.client.model.BayesVariable;
import com.emitrom.lienzo.client.core.types.LinearGradient;
import com.emitrom.lienzo.client.core.types.Shadow;
import com.emitrom.lienzo.shared.core.types.Color;
import com.google.common.collect.ImmutableMap;

public class BayesianUtils {

    // node
    public static int WIDTH_NODE = 135;
    public static int HEIGHT_NODE = 83;
    public static int HEIGHT_HEADER = 25;

    // header node
    public static int FONT_SIZE_HEADER_NODE = 10;
    public static int LABEL_POSITION_X_DEFAULT = 8;
    public static int LABEL_POSITION_Y_DEFAULT = 15;

    // porcentual bar
    public static final String DEFAULT_PORCENTUAL_FILL_COLOR = ShapesUtils.RGB_FILL_SHAPE;
    public static final String DEFAULT_PORCENTUAL_BORDER_COLOR = ShapesUtils.RGB_STROKE_SHAPE;
    public static final int HEIGHT_PORCENTUAL_BAR = 8;
    public static final int FONT_SIZE_PORCENTUAL_BAR = 9;
    public static final int WIDTH_PORCENTUAL_BAR = BayesianUtils.WIDTH_NODE - 75;
    public static final int POSITION_X_PORCENTUAL_BAR = 64;

    public static int POSITION_X_BASE = 0;
    public static int POSITION_Y_BASE = 25;

    public static int SUBSTRATE_HEIGHT = 34;

    public static String SUBSTRATE_COLOR = "#666";

    public static String FONT_FAMILY_PROGRESS_BAR = "Lucida Console";
    public static int FONT_SIZE_PROGRESS_BAR = 12;

    public static String XML3_RESOURCE_PATH = "xml03/";

    // label
    public static String BG_COLOR_CONTAINER = Color.rgbToBrowserHexColor(236, 236, 236);
    public static String BORDER_CONTAINER = Color.rgbToBrowserHexColor(236, 236, 236);
    public static int POSITION_X_CONTAINER = 0;
    public static int POSITION_Y_CONTAINER = -4;
    public static int WIDTH_CONTAINER = 250;
    public static int HEIGHT_CONTAINER = 23;
    public static int FONT_SIZE_TEXT_LABEL = 9;
    public static String COLOR_TEXT_LABEL = Color.rgbToBrowserHexColor(141, 147, 144);
    public static int POSITION_X_TEXT_LABEL = 40;
    public static int POSITION_Y_TEXT_LABEL = 0;
    public static int WIDTH_TEXT_LABEL = 150;
    public static int HEIGHT_TEXT_LABEL = 18;

    private static ImmutableMap<Double, String[][]> nodeColors = ImmutableMap
            .<Double, String[][]> builder()
            .put(0.1,
                    new String[][] { { Color.rgbToBrowserHexColor(102, 183, 176), Color.rgbToBrowserHexColor(197, 216, 214) } })
            .put(0.2,
                    new String[][] { { Color.rgbToBrowserHexColor(179, 99, 150), Color.rgbToBrowserHexColor(213, 186, 216) } })
            .put(0.3,
                    new String[][] { { Color.rgbToBrowserHexColor(120, 101, 186), Color.rgbToBrowserHexColor(210, 204, 229) } })
            .put(0.4,
                    new String[][] { { Color.rgbToBrowserHexColor(169, 181, 99), Color.rgbToBrowserHexColor(221, 224, 205) } })
            .put(0.5,
                    new String[][] { { Color.rgbToBrowserHexColor(89, 177, 140), Color.rgbToBrowserHexColor(182, 199, 191) } })
            .put(0.6,
                    new String[][] { { Color.rgbToBrowserHexColor(186, 183, 102), Color.rgbToBrowserHexColor(222, 219, 202) } })
            .put(0.7,
                    new String[][] { { Color.rgbToBrowserHexColor(191, 102, 104), Color.rgbToBrowserHexColor(230, 210, 211) } })
            .put(0.8,
                    new String[][] { { Color.rgbToBrowserHexColor(108, 156, 218), Color.rgbToBrowserHexColor(187, 194, 204) } })
            .put(0.9,
                    new String[][] { { Color.rgbToBrowserHexColor(108, 156, 218), Color.rgbToBrowserHexColor(187, 194, 204) } })
            .put(1.0,
                    new String[][] { { Color.rgbToBrowserHexColor(108, 156, 218), Color.rgbToBrowserHexColor(187, 194, 204) } })
            .build();

    public static String[][] getNodeColors() {
        double rand = Math.random();
        String colors[][] = new String[2][2];
        for (Map.Entry<Double, String[][]> entry : nodeColors.entrySet()) {
            if (rand < entry.getKey() && rand > (entry.getKey() - 0.1)) {
                colors[0][0] = entry.getValue()[0][1];
                colors[0][1] = entry.getValue()[0][0];
            }
        }
        return colors;

    }
    
    public static double[][] orderListValues(BayesVariable node, int outcomesSize) {
        double[][] probabilities = node.getProbabilities();
        double[][] valuesSorted = new double[probabilities.length][probabilities.length];
        int middle = probabilities.length / outcomesSize;
        int sizeFirstIncoming = node.getIncomingNodes().get(0).getOutcomes().size();
        int secondPart = middle / sizeFirstIncoming;
        int k = 0;
        for (int i = 0; i < middle / 2; i++) {
            for (int j = 0; j < outcomesSize; j++) {
                orderValues(valuesSorted, secondPart, k, j, sizeFirstIncoming, probabilities);
            }
            k += sizeFirstIncoming;
        }
        return valuesSorted;
    }
    
    private static void orderValues(double[][] valuesSorted, int secondPart, int k, int j, int sizeFirstIncoming, double[][] probabilities){
        for(int i=0; i<sizeFirstIncoming; i++ ){
            valuesSorted[k + i][j] = probabilities[secondPart * i + i][j];
        }     
    }

}
