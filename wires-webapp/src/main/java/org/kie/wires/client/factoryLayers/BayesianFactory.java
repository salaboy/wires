package org.kie.wires.client.factoryLayers;

import java.util.ArrayList;
import java.util.List;

import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.common.client.api.RemoteCallback;
import org.kie.wires.client.factoryShapes.ShapeFactoryUtil;
import org.kie.wires.client.shapes.EditableRectangle;

import com.emitrom.lienzo.client.core.event.NodeMouseDownEvent;
import com.emitrom.lienzo.client.core.event.NodeMouseDownHandler;
import com.emitrom.lienzo.client.core.shape.Group;
import com.emitrom.lienzo.client.core.shape.Layer;
import com.emitrom.lienzo.client.core.shape.Rectangle;
import com.emitrom.lienzo.client.core.shape.Shape;
import com.emitrom.lienzo.client.core.shape.Text;
import com.emitrom.lienzo.client.core.types.LinearGradient;
import com.emitrom.lienzo.client.core.types.Shadow;
import com.emitrom.lienzo.client.widget.LienzoPanel;
import com.emitrom.lienzo.shared.core.types.Color;
import com.emitrom.lienzo.shared.core.types.ColorName;
import com.emitrom.lienzo.shared.core.types.TextAlign;
import com.emitrom.lienzo.shared.core.types.TextBaseLine;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.RootPanel;
import com.hernsys.bayesian.client.entry.BayesianService;
import com.hernsys.bayesian.client.model.BayesNetwork;
import com.hernsys.bayesian.client.model.BayesVariable;

public class BayesianFactory /* extends ShapeFactory<Rectangle> */{

    protected LienzoPanel panel;

    private static String pathaXmlExample = "/media/horacio/C/java/comunidad-jboss/OTHERS/xstream/xstream-backend/src/main/java/com/hernsys/bayesian/client/resources/";

    /* progress bar */
    private Timer timer;
    private boolean infinite;
    private static int substrateWidth = 300;
    private static int substrateHeight = 34;
    private String substrateColor = "#666";
    private LinearGradient substrateGradient;

    private int progressWidth = 15;
    private int progressHeight = 34;
    private LinearGradient progressGradient;

    private Text progressPercentage;

    private Caller<BayesianService> bayesianService;

    private static int positionX_base = 293;
    private static int positionY_base = 50;

    private static List<LienzoPanel> components;
    private static List<LienzoPanel> progressComponents;

    public BayesianFactory(LienzoPanel panel, Caller<BayesianService> bayesianService) {
        GWT.log("construct ");
        this.panel = panel;
        this.bayesianService = bayesianService;
        infinite = true;
        if(components == null){
            components = new ArrayList<LienzoPanel>();
        }
        if(progressComponents==null){
            progressComponents = new ArrayList<LienzoPanel>();
        }
        
    }

    public void init(String xml03File) {
        final int positionX = (int) (positionX_base + 400);
        final int positionY = (int) (positionY_base + 400);
        clearComponents(components);
        clearComponents(progressComponents);
        drawProgressBar(positionX, positionY);
        bayesianService.call(new RemoteCallback<BayesNetwork>() {
            @Override
            public void callback(final BayesNetwork response) {
                for (Object bay : response.getNodos()) {
                    GWT.log("for");
                    drawNode((BayesVariable) bay);
                }
                GWT.log("apago infinite");
                infinite = false;

            }
        }).buildXml03(pathaXmlExample + xml03File);
    }

    private void clearComponents(List<LienzoPanel> comp) {
        for (LienzoPanel entry : comp) {
            entry.removeAll();
            comp = new ArrayList<LienzoPanel>();
        }

    }

    

    private void drawProgressBar(final int positionX, final int positionY) {
        progressPercentage = new Text("Loading...", "Lucida Console", 12).setFillColor(ColorName.WHITE.getValue())
                .setStrokeColor(substrateColor).setTextBaseLine(TextBaseLine.MIDDLE).setTextAlign(TextAlign.CENTER);
        
        
        drawProgressBarComponent(Color.rgbToBrowserHexColor(197, 216, 214), positionX, positionY, substrateWidth, substrateHeight, 200, Color.rgbToBrowserHexColor(197, 216, 214), false);
        timer = new Timer() {
            @Override
            public void run() {
                progressWidth++;
                GWT.log("progressWidth "  + progressWidth);
                if((progressWidth > substrateWidth - 4)){
                    timer.cancel();
                }else if (!infinite) {
                    timer.cancel();
                    clearComponents(progressComponents);
                }else{
                    GWT.log("***entro else");
                    progressPercentage.setText("Loading...");
                    progressPercentage.setX(progressPercentage.getX() + 140);
                    progressPercentage.setY(progressPercentage.getY() + 15);
                    final Layer floatingLayer = new Layer();
                    final LienzoPanel floatingPanel = new LienzoPanel(250, 20);
                    progressComponents.add(floatingPanel);
                    floatingLayer.add(progressPercentage);
                    floatingPanel.add(floatingLayer);
                    floatingLayer.draw();
                    getFloatingStyle(floatingPanel, positionX, positionY, 120);
                    RootPanel.get().add(floatingPanel);
                    
                    
                    
                    drawProgressBarComponent(Color.rgbToBrowserHexColor(102, 183, 176), positionX, positionY, progressWidth, progressHeight, 300, Color.rgbToBrowserHexColor(102, 183, 176), true);
                }    
                
            }
        };
        timer.scheduleRepeating(1);
    }
    
    private void drawText(String color, int positionX, int positionY, int width, int height, int zindex, String borderColor,
            String description, int fontSize) {
        final Text text = new Text(description, "Times", fontSize);
        text.setX(text.getX() + 8);
        text.setY(text.getY() + 15);
        final Layer floatingLayer = new Layer();
        final LienzoPanel floatingPanel = new LienzoPanel(width, 20);
        components.add(floatingPanel);
        floatingLayer.add(text);
        floatingPanel.add(floatingLayer);
        floatingLayer.draw();
        getFloatingStyle(floatingPanel, positionX, positionY, 120);
        RootPanel.get().add(floatingPanel);
    }

    private void drawNode(BayesVariable node) {
        int fontSize = 10;
        String[][] colors = getNodeColors();
        int widthNode = 110;
        double position[][] = node.getPosition();
        int positionX = (int) (positionX_base + Math.round(position[0][0]));
        int positionY = (int) (positionY_base + Math.round(position[0][1]));
        String borderColor = colors[0][0];

        // header
        int width = widthNode;
        int height = 25;
        int zindex = 100;
        positionY = positionY - height;
        drawComponent(colors[0][1], positionX, positionY, width, height, zindex, borderColor);
        drawText(Color.rgbToBrowserHexColor(104, 104, 104), positionX, positionY, width, height, zindex, borderColor,
                node.getName(), fontSize);

        // node
        width = widthNode;
        height = 80;
        zindex = 90;
        drawComponent(colors[0][0], positionX, positionY, width, height, zindex, borderColor);

        // porcentual bar and fill bar
        drawPorcentualbar(node.getOutcomes(), widthNode, position, colors[0][1], node.getProbabilities());
    }

    private void drawComponent(String color, int positionX, int positionY, int width, int height, int zindex, String borderColor) {
        if (borderColor == null) {
            borderColor = Color.rgbToBrowserHexColor(0, 0, 0);
        }
        final EditableRectangle component = new EditableRectangle(width, height);
        setAttributes(component, color, getFloatingX(), getFloatingY(), borderColor);
        final Layer floatingLayer = new Layer();
        final LienzoPanel floatingPanel = new LienzoPanel(width, height);

        components.add(floatingPanel);

        floatingLayer.add(component);
        floatingPanel.add(floatingLayer);
        floatingLayer.draw();
        
        getFloatingStyle(floatingPanel, positionX, positionY, zindex);
        RootPanel.get().add(floatingPanel);
    }
    
    private void drawProgressBarComponent(String color, int positionX, int positionY, int width, int height, int zindex, String borderColor, boolean progress) {
        final EditableRectangle component = new EditableRectangle(width, height);
        if(progress){
            progressGradient = new LinearGradient(0, -50, 0, 50);
            progressGradient.addColorStop(0.5, "#4DA4F3");
            progressGradient.addColorStop(0.8, "#ADD9FF");
            progressGradient.addColorStop(1, "#9ED1FF");
            component.setFillGradient(progressGradient);
        }else{
            substrateGradient = new LinearGradient(0, substrateHeight, 0, 0);
            substrateGradient.addColorStop(0.4, "rgba(255,255,255, 0.1)");
            substrateGradient.addColorStop(0.6, "rgba(255,255,255, 0.7)");
            substrateGradient.addColorStop(0.9, "rgba(255,255,255,0.4)");
            substrateGradient.addColorStop(1, "rgba(189,189,189,1)");
            component.setFillGradient(substrateGradient).setShadow(new Shadow(substrateColor, 5, 3, 3)).setStrokeColor(substrateColor).setStrokeWidth(1);
        }
        component.setX(getFloatingX()).setY(getFloatingY()).setDraggable(false);
        final Layer floatingLayer = new Layer();
        final LienzoPanel floatingPanel = new LienzoPanel(width, height);

        progressComponents.add(floatingPanel);

        floatingLayer.add(component);
        floatingPanel.add(floatingLayer);
        floatingLayer.draw();
        
        getFloatingStyle(floatingPanel, positionX, positionY, zindex);
        RootPanel.get().add(floatingPanel);
    }

    private void drawPorcentualbar(String[] outcomes, int widthNode, double position[][], String fillColor,
            double probabilities[][]) {
        int fontSize = 9;
        int width = widthNode - 50;
        int height = 8;
        int zindex = 120;
        int positionX, positionY, widthFill;
        positionX = (int) (positionX_base + Math.round(position[0][0])) + 45;
        positionY = (int) (positionY_base + Math.round(position[0][1])) + 20;
        String borderColor = fillColor;
        for (int i = 0; i < outcomes.length; i++) {
            // Porcentual bar
            positionY += i * 14;
            drawText(Color.rgbToBrowserHexColor(0, 0, 0), positionX - 48, positionY - 8, width, height, zindex, borderColor,
                    outcomes[i], fontSize);
            drawComponent(Color.rgbToBrowserHexColor(255, 255, 255), positionX, positionY, width, height, zindex, borderColor);
            // fill bar
            widthFill = calculatePorcentage(probabilities, width, i);
            drawComponent(fillColor, positionX, positionY, widthFill, height, zindex, borderColor);
        }
    }

    private int calculatePorcentage(double probabilities[][], int maxWidthFill, int position) {
        double porcentual = 0;
        if (position == 0) {
            porcentual = probabilities[0][0];
        } else if (position == 1) {
            porcentual = probabilities[0][1];
        }
        porcentual *= 100;
        int result = (int) ((porcentual * maxWidthFill) / 100);
        // minus 1 minus 1 - border width
        result -= 1;
        return result;
    }

    protected void addBoundingHandlers(Rectangle boundingBox, Group group) {
        boundingBox.addNodeMouseDownHandler(getNodeMouseDownEvent(group));

    }

    protected NodeMouseDownHandler getNodeMouseDownEvent(final Group group) {
        NodeMouseDownHandler nodeMouseDownHandler = new NodeMouseDownHandler() {
            public void onNodeMouseDown(NodeMouseDownEvent event) {
            }
        };

        return nodeMouseDownHandler;
    }

    protected Style getFloatingStyle(LienzoPanel floatingPanel, int positionX, int positionY, int zIndex) {
        Style style = floatingPanel.getElement().getStyle();
        style.setPosition(Position.ABSOLUTE);
        style.setLeft(positionX, Unit.PX);
        style.setTop(positionY, Unit.PX);
        style.setZIndex(zIndex);
        return style;
    }

    private void setAttributes(Shape<?> floatingShape, String fillColor, double x, double y, String borderColor) {
        floatingShape.setX(x).setY(y).setStrokeColor(borderColor).setStrokeWidth(ShapeFactoryUtil.RGB_STROKE_WIDTH_SHAPE)
                .setFillColor(fillColor).setDraggable(false);

    }

    private String[][] getNodeColors() {
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
        // String colors[][] = {headerColor, fillColor};
        colors[0][0] = fillColor;
        colors[0][1] = headerColor;
        return colors;

    }


    private double getFloatingX() {
        return 0;
    }

    private double getFloatingY() {
        return 0;
    }

}