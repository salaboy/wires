package org.kie.wires.client.bayesian.factory;

import com.emitrom.lienzo.client.core.event.NodeMouseDownEvent;
import com.emitrom.lienzo.client.core.event.NodeMouseDownHandler;
import com.emitrom.lienzo.client.core.shape.Group;
import com.emitrom.lienzo.client.core.shape.Layer;
import com.emitrom.lienzo.client.core.shape.Rectangle;
import com.emitrom.lienzo.client.core.shape.Shape;
import com.emitrom.lienzo.client.core.shape.Text;
import com.emitrom.lienzo.client.widget.LienzoPanel;
import com.emitrom.lienzo.shared.core.types.Color;
import com.google.common.collect.Lists;
import com.google.gwt.user.client.Window;
import com.xstream.bayesian.client.entry.BayesianService;
import com.xstream.bayesian.client.model.BayesNetwork;
import com.xstream.bayesian.client.model.BayesVariable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.event.Event;
import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.common.client.api.ErrorCallback;
import org.jboss.errai.common.client.api.RemoteCallback;
import org.kie.wires.client.bayesian.shapes.EditableBayesianNode;
import org.kie.wires.client.events.LayerEvent;
import org.kie.wires.client.events.ProbabilityEvent;
import org.kie.wires.client.events.ProgressEvent;
import org.kie.wires.client.events.ReadyEvent;
import org.kie.wires.client.factoryShapes.ShapeFactoryUtil;
import org.kie.wires.client.util.BayesianUtils;
import org.kie.wires.client.util.LienzoUtils;

public class BayesianFactory extends BaseFactory {

    protected LienzoPanel panel;
    private Caller<BayesianService> bayesianService;
    private Event<LayerEvent> layerEvent;
    private Event<ProbabilityEvent> probabilityEvent;
    private Event<ProgressEvent> progressEvent;
    private Event<ReadyEvent> readyEvent;
    private List<BayesVariable> nodes;
    
    private List<EditableBayesianNode> bayesianNodes = new ArrayList<EditableBayesianNode>();

    public BayesianFactory( LienzoPanel panel, Caller<BayesianService> bayesianService, String xml03File,
            Layer layer, Event<LayerEvent> layerEvent, Event<ProbabilityEvent> probabilityEvent, Event<ReadyEvent> readyEvent,
            Event<ProgressEvent> progressEvent) {
        this.panel = panel;
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
        this.drawLabelFile(xml03File, layer);
        bayesianService.call(new RemoteCallback<BayesNetwork>() {
            @Override
            public void callback(final BayesNetwork response) {
                nodes = Lists.newArrayList();
                for (BayesVariable bay : response.getNodos()) {
                    drawNode(bay, layer);
                }
                LienzoUtils.loadingProgressBar = false;
                layerEvent.fire(new LayerEvent(nodes));
                readyEvent.fire(new ReadyEvent(bayesianNodes));
                layer.draw();
            }
        }, new ErrorCallback() {

            @Override
            public boolean error(Object message, Throwable throwable) {
                Window.alert("Sorry.. the " + xml03File + " could not be read..");
                LienzoUtils.loadingProgressBar = false;
                return false;
            }
        }).buildXml03(BayesianUtils.relativePath + xml03File);
        
        
    }

    private void drawNode(BayesVariable node,  Layer layer) {
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
        
        EditableBayesianNode bayesianNode = new EditableBayesianNode(node.getName(), width, height);
        
        
//        bayesianNode.setStrokeColor(borderColor).setStrokeWidth(ShapeFactoryUtil.RGB_STROKE_WIDTH_SHAPE)
//                .setFillColor(colors[0][0]);
       // Shape<?> drawComponent = super.drawComponent(colors[0][0], positionX, positionY, width, height, borderColor, layer, true);
       
        
        
        // header
//        width = widthNode;
//        height = 25;
//        positionY = positionY - height;
        
//        Shape<?> drawComponent1 = super.drawComponent(colors[0][1], positionX, positionY, width, height, borderColor, layer, true);
//        
//        Text drawText = super.drawText(Color.rgbToBrowserHexColor(104, 104, 104), positionX, positionY, width, height, borderColor,
//                node.getName(), fontSize,  layer);
//        
       
        
        bayesianNode.init(positionX, positionY, layer);
        
       // bayesianNodes.add(drawText);
        // labels (layers perspective)
        nodes.add(node);
        

        // draw porcentual bar
        drawPorcentualbar(node.getOutcomes(), widthNode, position, colors[0][1], node.getProbabilities(), layer);
        
        bayesianNodes.add(bayesianNode);
        
    }

    private void drawLabelFile(String nameFile,  Layer layer) {

        super.drawComponent(BayesianUtils.bgColorContainer, BayesianUtils.positionXContainer, BayesianUtils.positionYContainer,
                BayesianUtils.widthContainer, BayesianUtils.heightContainer, BayesianUtils.borderContainer, layer, true);

        super.drawText(BayesianUtils.colorTextLabel, BayesianUtils.positionXTextLabel, BayesianUtils.positionYTextLabel,
                BayesianUtils.widthTextLabel, BayesianUtils.heightTextLabel, BayesianUtils.colorTextLabel, nameFile,
                BayesianUtils.fontSizeTextLabel, layer);
    }

    private void drawPorcentualbar(List<String> outcomes, int widthNode, double position[][], String fillColor,
            double probabilities[][],  Layer layer) {
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
            super.drawText(Color.rgbToBrowserHexColor(0, 0, 0), positionX - 63, positionY - 8, width, height, borderColor,
                    outcomes.get(i), fontSize,  layer);
            super.drawComponent(Color.rgbToBrowserHexColor(255, 255, 255), positionX, positionY, width, height, borderColor,
                     layer, true);
            // fill bar
            widthFill = calculatePorcentage(probabilities, width, i);
            super.drawComponent(fillColor, positionX, positionY, widthFill, height, borderColor,  layer, false);
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

    private void clearScreen( Layer layer) {
        layer.draw();
        progressEvent.fire(new ProgressEvent(LienzoUtils.progressShapes));
        layerEvent.fire(new LayerEvent(null));
        probabilityEvent.fire(new ProbabilityEvent(null));
    }

    
}