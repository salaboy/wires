package org.kie.wires.client.factoryLayers;

import java.util.ArrayList;
import java.util.List;

import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.common.client.api.ErrorCallback;
import org.jboss.errai.common.client.api.RemoteCallback;
import org.kie.wires.client.factoryShapes.ShapeFactoryUtil;
import org.kie.wires.client.shapes.EditableRectangle;
import org.kie.wires.client.utils.BayesianUtils;

import com.emitrom.lienzo.client.core.event.NodeMouseDownEvent;
import com.emitrom.lienzo.client.core.event.NodeMouseDownHandler;
import com.emitrom.lienzo.client.core.shape.Group;
import com.emitrom.lienzo.client.core.shape.Layer;
import com.emitrom.lienzo.client.core.shape.Rectangle;
import com.emitrom.lienzo.client.core.shape.Shape;
import com.emitrom.lienzo.client.core.shape.Text;
import com.emitrom.lienzo.client.widget.LienzoPanel;
import com.emitrom.lienzo.shared.core.types.Color;
import com.emitrom.lienzo.shared.core.types.ColorName;
import com.emitrom.lienzo.shared.core.types.TextAlign;
import com.emitrom.lienzo.shared.core.types.TextBaseLine;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.hernsys.bayesian.client.entry.BayesianService;
import com.hernsys.bayesian.client.model.BayesNetwork;
import com.hernsys.bayesian.client.model.BayesVariable;

public class BayesianFactory {

    protected LienzoPanel panel;

    private static String pathaXmlExample = "/Users/salaboy/Projects/xstream/xstream-backend/src/main/java/com/hernsys/bayesian/client/resources/";
    private Timer timer;
    private boolean infinite;
    private int progressWidth = 15;
    private Caller<BayesianService> bayesianService;
    private static List<LienzoPanel> progressComponents;

    public BayesianFactory(Group group, LienzoPanel panel, Caller<BayesianService> bayesianService, String xml03File,
            Layer layer) {
        this.panel = panel;
        this.bayesianService = bayesianService;
        infinite = true;
        if (progressComponents == null) {
            progressComponents = new ArrayList<LienzoPanel>();
        }
        init(xml03File, group, layer);

    }

    public void init(final String xml03File, final Group group, final Layer layer) {
        clearScreen(group, layer);
        drawProgressBar();
        bayesianService.call(new RemoteCallback<BayesNetwork>() {
            @Override
            public void callback(final BayesNetwork response) {
                for (Object bay : response.getNodos()) {
                    drawNode((BayesVariable) bay, group, layer);
                }
                infinite = false;

            }
        }, new ErrorCallback() {

            @Override
            public boolean error(Object message, Throwable throwable) {
                Window.alert("Sorry.. the " + xml03File + " could not be read..");
                infinite = false;
                return false;
            }
        }).buildXml03(pathaXmlExample + xml03File);
    }

    private void clearComponents(List<LienzoPanel> comp) {
        for (LienzoPanel entry : comp) {
            entry.removeAll();
            comp = new ArrayList<LienzoPanel>();
        }

    }

    private void drawProgressBar() {
        final int positionX = (int) (BayesianUtils.positionX_base + 550);
        final int positionY = (int) (BayesianUtils.positionY_base + 300);

        final int substrateWidth = 300;
        final int progressHeight = 34;

        final Text progressPercentage = new Text("Drawing...", BayesianUtils.fontFamilyProgressBar,
                BayesianUtils.fontSizeProgressBar).setFillColor(ColorName.WHITE.getValue())
                .setStrokeColor(BayesianUtils.substrateColor).setTextBaseLine(TextBaseLine.MIDDLE)
                .setTextAlign(TextAlign.CENTER);

        drawProgressBarComponent(Color.rgbToBrowserHexColor(197, 216, 214), positionX, positionY, substrateWidth,
                BayesianUtils.substrateHeight, 200, Color.rgbToBrowserHexColor(197, 216, 214), false);
        timer = new Timer() {
            @Override
            public void run() {
                progressWidth += 4;
                if ((progressWidth > substrateWidth - 4)) {
                    timer.cancel();
                } else if (!infinite) {
                    timer.cancel();
                    clearComponents(progressComponents);
                } else {
                    progressPercentage.setText("Drawing...");
                    progressPercentage.setX(progressPercentage.getX() + 140);
                    progressPercentage.setY(progressPercentage.getY() + 15);

                    Layer floatingLayer = new Layer();
                    LienzoPanel floatingPanel = new LienzoPanel(220, 25);

                    progressComponents.add(floatingPanel);

                    floatingLayer.add(progressPercentage);
                    floatingPanel.add(floatingLayer);
                    floatingLayer.draw();

                    getFloatingStyle(floatingPanel, positionX, positionY, 120);

                    RootPanel.get().add(floatingPanel);

                    drawProgressBarComponent(Color.rgbToBrowserHexColor(102, 183, 176), positionX, positionY, progressWidth,
                            progressHeight, 300, Color.rgbToBrowserHexColor(102, 183, 176), true);
                }

            }
        };
        timer.scheduleRepeating(1);
    }

    private void drawText(String color, int positionX, int positionY, int width, int height, String borderColor,
            String description, int fontSize, Group group, Layer layer) {
        final Text text = new Text(description, "Times", fontSize);
        text.setX(positionX + 8).setY(positionY + 15);
        group.add(text);
        layer.draw();
    }

    private void drawNode(BayesVariable node, Group group, Layer layer) {
        int fontSize = 10;
        String[][] colors = BayesianUtils.getNodeColors();
        int widthNode = 135;
        double position[][] = node.getPosition();
        int positionX = (int) (BayesianUtils.positionX_base + Math.round(position[0][0]));
        int positionY = (int) (BayesianUtils.positionY_base + Math.round(position[0][1]));
        String borderColor = colors[0][0];

        // node
        int width = widthNode;
        int height = 70;
        drawComponent(colors[0][0], positionX, positionY, width, height, borderColor, group, layer);

        // header
        width = widthNode;
        height = 25;
        positionY = positionY - height;
        drawComponent(colors[0][1], positionX, positionY, width, height, borderColor, group, layer);
        drawText(Color.rgbToBrowserHexColor(104, 104, 104), positionX, positionY, width, height, borderColor, node.getName(),
                fontSize, group, layer);

        // porcentual bar and fill bar
        drawPorcentualbar(node.getOutcomes(), widthNode, position, colors[0][1], node.getProbabilities(), group, layer);
    }

    private void drawComponent(String color, int positionX, int positionY, int width, int height, String borderColor,
            Group group, Layer layer) {
        if (borderColor == null) {
            borderColor = Color.rgbToBrowserHexColor(0, 0, 0);
        }
        final EditableRectangle component = new EditableRectangle(width, height);
        setAttributes(component, color, positionX, positionY, borderColor);
        group.add(component);
        layer.draw();
    }

    private void drawProgressBarComponent(String color, int positionX, int positionY, int width, int height, int zindex,
            String borderColor, boolean progress) {
        final EditableRectangle component = new EditableRectangle(width, height);
        if (progress) {
            component.setFillGradient(BayesianUtils.getProgressGradient());
        } else {
            component.setFillGradient(BayesianUtils.getSubstrateGradient()).setShadow(BayesianUtils.getSubstrateShadow())
                    .setStrokeColor(BayesianUtils.substrateColor).setStrokeWidth(1);
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

    private void drawPorcentualbar(List<String> outcomes, int widthNode, double position[][], String fillColor,
            double probabilities[][], Group group, Layer layer) {
        int fontSize = 9;
        int width = widthNode - 75;
        int height = 8;
        int positionX, positionY, widthFill;
        positionX = (int) (BayesianUtils.positionX_base + Math.round(position[0][0])) + 64;
        positionY = (int) (BayesianUtils.positionY_base + Math.round(position[0][1]));
        positionY = (outcomes.size() > 3) ? positionY - 10 : positionY;
        String borderColor = fillColor;
        for (int i = 0; i < outcomes.size(); i++) {
            // Porcentual bar
            positionY += 14;
            drawText(Color.rgbToBrowserHexColor(0, 0, 0), positionX - 63, positionY - 8, width, height, borderColor,
                    outcomes.get(i), fontSize, group, layer);
            drawComponent(Color.rgbToBrowserHexColor(255, 255, 255), positionX, positionY, width, height, borderColor, group,
                    layer);
            // fill bar
            widthFill = calculatePorcentage(probabilities, width, i);
            drawComponent(fillColor, positionX, positionY, widthFill, height, borderColor, group, layer);
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

    private void clearScreen(Group group, Layer layer) {
        group.removeAll();
        layer.draw();
        clearComponents(progressComponents);
    }

    private double getFloatingX() {
        return 0;
    }

    private double getFloatingY() {
        return 0;
    }

}