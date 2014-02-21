package org.kie.wires.client.util;

import java.util.Map;

import com.emitrom.lienzo.client.core.types.LinearGradient;
import com.emitrom.lienzo.client.core.types.Shadow;
import com.emitrom.lienzo.shared.core.types.Color;
import com.google.common.collect.ImmutableMap;

public class BayesianUtils {

    public static int positionX_base = 0;
    public static int positionY_base = 25;

    public static int substrateHeight = 34;

    public static String substrateColor = "#666";

    public static String fontFamilyProgressBar = "Lucida Console";
    public static int fontSizeProgressBar = 12;

    public static String relativePath = "/src/main/java/org/kie/wires/client/bayesian/resources/";
    
    
    //label
    public static String bgColorContainer = Color.rgbToBrowserHexColor(236, 236, 236);
    public static String borderContainer = Color.rgbToBrowserHexColor(236, 236, 236);
    public static int positionXContainer = 0;
    public static int positionYContainer = -4;
    public static int widthContainer = 250;
    public static int heightContainer = 23;
    public static int fontSizeTextLabel = 9;
    public static String colorTextLabel = Color.rgbToBrowserHexColor(141, 147, 144);
    public static int positionXTextLabel = 40;
    public static int positionYTextLabel = 0;
    public static int widthTextLabel = 150;
    public static int heightTextLabel = 18;
    
    

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
            if(rand < entry.getKey() && rand > (entry.getKey() - 0.1)){
                colors[0][0] = entry.getValue()[0][1];
                colors[0][1] = entry.getValue()[0][0];
            }
        }
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
