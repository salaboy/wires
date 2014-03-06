package org.kie.wires.client.bayesian.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.enterprise.event.Event;

import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.common.client.api.ErrorCallback;
import org.jboss.errai.common.client.api.RemoteCallback;
import org.kie.wires.client.bayesian.shapes.EditableBayesianNode;
import org.kie.wires.client.events.LayerEvent;
import org.kie.wires.client.events.ProbabilityEvent;
import org.kie.wires.client.events.ProgressEvent;
import org.kie.wires.client.events.ReadyEvent;
import org.kie.wires.client.util.BayesianUtils;
import org.kie.wires.client.util.LienzoUtils;

import com.emitrom.lienzo.client.core.shape.Layer;
import com.emitrom.lienzo.client.core.shape.Rectangle;
import com.emitrom.lienzo.client.core.shape.Text;
import com.emitrom.lienzo.shared.core.types.Color;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gwt.user.client.Window;
import com.xstream.bayesian.client.entry.BayesianService;
import com.xstream.bayesian.client.model.BayesNetwork;
import com.xstream.bayesian.client.model.BayesVariable;

public class BayesianFactory extends BaseFactory {

    private Caller<BayesianService> bayesianService;
    private Event<LayerEvent> layerEvent;
    private Event<ProbabilityEvent> probabilityEvent;
    private Event<ProgressEvent> progressEvent;
    private Event<ReadyEvent> readyEvent;
    private List<BayesVariable> nodes;
    private String[][] colors;

    private List<EditableBayesianNode> bayesianNodes = new ArrayList<EditableBayesianNode>();

    public BayesianFactory(Caller<BayesianService> bayesianService, String xml03File, Layer layer,
            Event<LayerEvent> layerEvent, Event<ProbabilityEvent> probabilityEvent, Event<ReadyEvent> readyEvent,
            Event<ProgressEvent> progressEvent) {
        this.bayesianService = bayesianService;
        this.layerEvent = layerEvent;
        this.progressEvent = progressEvent;
        this.probabilityEvent = probabilityEvent;
        this.readyEvent = readyEvent;
        LienzoUtils.loadingProgressBar = true;
        init(xml03File, layer);

    }

    public void init(final String xml03File, final Layer layer) {
        clearScreen(layer);
        progressEvent.fire(new ProgressEvent(null));
        this.drawLabelFile(xml03File);
        buildBayesNetworkFromXML(xml03File, layer);

    }

    private void buildBayesNetworkFromXML(final String xml03File, final Layer layer) {
        bayesianService.call(new RemoteCallback<BayesNetwork>() {
            @Override
            public void callback(final BayesNetwork response) {
                nodes = Lists.newArrayList();
                for (BayesVariable bay : response.getNodos()) {
                    drawBayesianNode(bay, layer);
                }
                LienzoUtils.loadingProgressBar = false;
                layerEvent.fire(new LayerEvent(nodes));
                readyEvent.fire(new ReadyEvent(bayesianNodes));
                layer.draw();
            }
        }, new ErrorCallback<Object>() {

            @Override
            public boolean error(Object message, Throwable throwable) {
                Window.alert("Sorry.. the " + xml03File + " could not be read..");
                LienzoUtils.loadingProgressBar = false;
                return false;
            }
        }).buildXml03(BayesianUtils.XML3_RESOURCE_PATH + xml03File);
    }

    private void drawBayesianNode(BayesVariable node, Layer layer) {
        colors = BayesianUtils.getNodeColors();
        double position[][] = node.getPosition();
        int positionX = (int) (BayesianUtils.POSITION_X_BASE + Math.round(position[0][0]));
        int positionY = (int) (BayesianUtils.POSITION_Y_BASE + Math.round(position[0][1]));
        String fillNodeColor = colors[0][0];

        EditableBayesianNode bayesianNode = new EditableBayesianNode(BayesianUtils.WIDTH_NODE, BayesianUtils.HEIGHT_NODE,
                positionX, positionY, fillNodeColor);

        this.setHeader(node, bayesianNode);
        this.setPorcentualBar(node, bayesianNode);

        bayesianNode.buildNode();

        nodes.add(node);
        bayesianNodes.add(bayesianNode);

    }

    private void setHeader(BayesVariable node, EditableBayesianNode bayesianNode) {
        bayesianNode.setHeader(new Rectangle(bayesianNode.getRectangle().getWidth(), BayesianUtils.HEIGHT_HEADER));
        bayesianNode.getHeader().setFillColor(colors[0][1]);
        bayesianNode.setTextHeader(drawText(node.getName(), BayesianUtils.FONT_SIZE_HEADER_NODE,
                BayesianUtils.LABEL_POSITION_X_DEFAULT, BayesianUtils.LABEL_POSITION_Y_DEFAULT));
    }

    private void setPorcentualBar(BayesVariable node, EditableBayesianNode bayesianNode) {
        String fillColor = colors[0][1];
        int widthFill;
        int positionY = 18;
        positionY = (node.getOutcomes().size() > 3) ? positionY - 10 : positionY;
        String borderColor = fillColor;

        List<Rectangle> componentsProgressBar = Lists.newArrayList();
        Text labelPorcentual;
        Map<Text, List<Rectangle>> porcentualsBar = Maps.newHashMap();
        for (int i = 0; i < node.getOutcomes().size(); i++) {
            // Porcentual bar
            positionY += 14;
            labelPorcentual = this.drawText(node.getOutcomes().get(i), BayesianUtils.FONT_SIZE_PORCENTUAL_BAR,
                    BayesianUtils.LABEL_POSITION_X_DEFAULT, positionY + 7);
            componentsProgressBar.add(this.drawComponent(Color.rgbToBrowserHexColor(255, 255, 255),
                    BayesianUtils.POSITION_X_PORCENTUAL_BAR, positionY, BayesianUtils.WIDTH_PORCENTUAL_BAR,
                    BayesianUtils.HEIGHT_PORCENTUAL_BAR, borderColor, 3));
            // fill bar
            widthFill = calculatePorcentage(node.getProbabilities(), BayesianUtils.WIDTH_PORCENTUAL_BAR, i);
            componentsProgressBar.add(drawComponent(fillColor, BayesianUtils.POSITION_X_PORCENTUAL_BAR, positionY, widthFill,
                    BayesianUtils.HEIGHT_PORCENTUAL_BAR, borderColor, 0));
            bayesianNode.getPorcentualsBar().put(labelPorcentual, componentsProgressBar);

            porcentualsBar.put(labelPorcentual, componentsProgressBar);
        }
        bayesianNode.setPorcentualBars(porcentualsBar);
    }

    

    private int calculatePorcentage(double probabilities[][], int maxWidthFill, int position) {
        double porcentual = 0;
        if (position == 0) {
            porcentual = probabilities[0][0];
        } else if (position == 1) {
            porcentual = probabilities[0][1];
        }
        porcentual *= 100;
        return (int) ((porcentual * maxWidthFill) / 100);
    }

    

    private void drawLabelFile(String nameFile) {

        /*super.drawComponent(BayesianUtils.BG_COLOR_CONTAINER, BayesianUtils.POSITION_X_CONTAINER,
                BayesianUtils.POSITION_Y_CONTAINER, BayesianUtils.WIDTH_CONTAINER, BayesianUtils.HEIGHT_CONTAINER,
                BayesianUtils.BORDER_CONTAINER, true);

        super.drawText(BayesianUtils.COLOR_TEXT_LABEL, BayesianUtils.POSITION_X_TEXT_LABEL,
                BayesianUtils.POSITION_Y_TEXT_LABEL, BayesianUtils.WIDTH_TEXT_LABEL, BayesianUtils.HEIGHT_TEXT_LABEL,
                BayesianUtils.COLOR_TEXT_LABEL, nameFile, BayesianUtils.FONT_SIZE_TEXT_LABEL);*/
    }

    private void clearScreen(Layer layer) {
        layer.draw();
        progressEvent.fire(new ProgressEvent(LienzoUtils.progressShapes));
        probabilityEvent.fire(new ProbabilityEvent());
    }

}