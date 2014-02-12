package org.kie.wires.client.utils;

import com.emitrom.lienzo.client.core.types.LinearGradient;
import com.emitrom.lienzo.client.core.types.Shadow;
import com.emitrom.lienzo.shared.core.types.Color;

public class BayesianUtils {

    public static int positionX_base = 0;
    public static int positionY_base = 5;

    public static int substrateHeight = 34;

    public static String substrateColor = "#666";

    public static String fontFamilyProgressBar = "Lucida Console";
    public static int fontSizeProgressBar = 12;

    public static String[][] getNodeColors() {
        double rand = Math.random();
        String colors[][] = new String[2][2];
        String headerColor = Color.rgbToBrowserHexColor(255, 0, 0);
        String fillColor = Color.rgbToBrowserHexColor(255, 0, 0);
        if (rand < 0.1) {
            headerColor = Color.rgbToBrowserHexColor(102, 183, 176);
            fillColor = Color.rgbToBrowserHexColor(197, 216, 214);

        } else if (rand > 0.1 && rand < 0.2) {
            headerColor = Color.rgbToBrowserHexColor(179, 99, 150);
            fillColor = Color.rgbToBrowserHexColor(213, 186, 216);

        } else if (rand > 0.2 && rand < 0.3) {
            headerColor = Color.rgbToBrowserHexColor(120, 101, 186);
            fillColor = Color.rgbToBrowserHexColor(210, 204, 229);

        } else if (rand > 0.3 && rand < 0.4) {
            headerColor = Color.rgbToBrowserHexColor(169, 181, 99);
            fillColor = Color.rgbToBrowserHexColor(221, 224, 205);

        } else if (rand > 0.4 && rand < 0.5) {
            headerColor = Color.rgbToBrowserHexColor(89, 177, 140);
            fillColor = Color.rgbToBrowserHexColor(182, 199, 191);

        } else if (rand > 0.5 && rand < 0.6) {
            headerColor = Color.rgbToBrowserHexColor(186, 183, 102);
            fillColor = Color.rgbToBrowserHexColor(222, 219, 202);

        } else if (rand > 0.6 && rand < 0.7) {
            headerColor = Color.rgbToBrowserHexColor(191, 102, 104);
            fillColor = Color.rgbToBrowserHexColor(230, 210, 211);

        } else if (rand > 0.7) {
            headerColor = Color.rgbToBrowserHexColor(108, 156, 218);
            fillColor = Color.rgbToBrowserHexColor(187, 194, 204);

        }
        colors[0][0] = fillColor;
        colors[0][1] = headerColor;
        return colors;

    }

    public static LinearGradient getSubstrateGradient() {
        LinearGradient substrateGradient = new LinearGradient(0, substrateHeight, 0, 0);
        substrateGradient.addColorStop(0.4, "rgba(255,255,255, 0.1)");
        substrateGradient.addColorStop(0.6, "rgba(255,255,255, 0.7)");
        substrateGradient.addColorStop(0.9, "rgba(255,255,255,0.4)");
        substrateGradient.addColorStop(1, "rgba(189,189,189,1)");
        return substrateGradient;
    }

    public static LinearGradient getProgressGradient() {
        LinearGradient progressGradient = new LinearGradient(0, -50, 0, 50);
        progressGradient.addColorStop(0.5, "#4DA4F3");
        progressGradient.addColorStop(0.8, "#ADD9FF");
        progressGradient.addColorStop(1, "#9ED1FF");
        return progressGradient;
    }

    public static Shadow getSubstrateShadow() {
        return new Shadow(BayesianUtils.substrateColor, 5, 3, 3);
    }

}
