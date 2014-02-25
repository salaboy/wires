package org.kie.wires.client.bayesian.factory;

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
import org.kie.wires.client.util.BayesianUtils;
import org.kie.wires.client.util.LienzoUtils;

import com.emitrom.lienzo.client.core.event.NodeMouseDownEvent;
import com.emitrom.lienzo.client.core.event.NodeMouseDownHandler;
import com.emitrom.lienzo.client.core.shape.Group;
import com.emitrom.lienzo.client.core.shape.Layer;
import com.emitrom.lienzo.client.core.shape.Rectangle;
import com.emitrom.lienzo.client.widget.LienzoPanel;
import com.google.common.collect.Lists;
import com.google.gwt.user.client.Window;
import com.xstream.bayesian.client.entry.BayesianService;
import com.xstream.bayesian.client.model.BayesNetwork;
import com.xstream.bayesian.client.model.BayesVariable;

public class BayesianFactory extends BaseFactory {

    protected LienzoPanel panel;
    private Caller<BayesianService> bayesianService;
    private Event<LayerEvent> layerEvent;
    private Event<ProbabilityEvent> probabilityEvent;
    private Event<ProgressEvent> progressEvent;
    private Event<ReadyEvent> readyEvent;
    private List<BayesVariable> nodes;

    private List<EditableBayesianNode> bayesianNodes = new ArrayList<EditableBayesianNode>();

    public BayesianFactory(LienzoPanel panel, Caller<BayesianService> bayesianService, String xml03File, Layer layer,
            Event<LayerEvent> layerEvent, Event<ProbabilityEvent> probabilityEvent, Event<ReadyEvent> readyEvent,
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
        }).buildXml03(BayesianUtils.RELATIVE_PATH + xml03File);

    }

    private void drawNode(BayesVariable node, Layer layer) {
        int widthNode = 135;
        double position[][] = node.getPosition();
        int positionX = (int) (BayesianUtils.POSITION_X_BASE + Math.round(position[0][0]));
        int positionY = (int) (BayesianUtils.POSITION_Y_BASE + Math.round(position[0][1]));

        // node
        int width = widthNode;
        int height = 83;

        EditableBayesianNode bayesianNode = new EditableBayesianNode(width, height);

        // bayesianNode.setStrokeColor(borderColor).setStrokeWidth(ShapeFactoryUtil.RGB_STROKE_WIDTH_SHAPE)
        // .setFillColor(colors[0][0]);
        // Shape<?> drawComponent = super.drawComponent(colors[0][0], positionX,
        // positionY, width, height, borderColor, layer, true);

        // header
        // width = widthNode;
        // height = 25;
        // positionY = positionY - height;

        // Shape<?> drawComponent1 = super.drawComponent(colors[0][1],
        // positionX, positionY, width, height, borderColor, layer, true);
        //
        // Text drawText = super.drawText(Color.rgbToBrowserHexColor(104, 104,
        // 104), positionX, positionY, width, height, borderColor,
        // node.getName(), fontSize, layer);
        //

        bayesianNode.init(positionX, positionY, layer, node);

        // bayesianNodes.add(drawText);
        // labels (layers perspective)
        nodes.add(node);

        // draw porcentual bar
        // drawPorcentualbar(node.getOutcomes(), widthNode, position,
        // colors[0][1], node.getProbabilities());

        bayesianNodes.add(bayesianNode);

    }

    private void drawLabelFile(String nameFile, Layer layer) {

        super.drawComponent(BayesianUtils.BG_COLOR_CONTAINER, BayesianUtils.POSITION_X_CONTAINER,
                BayesianUtils.POSITION_Y_CONTAINER, BayesianUtils.WIDTH_CONTAINER, BayesianUtils.HEIGHT_CONTAINER,
                BayesianUtils.BORDER_CONTAINER, true);

        super.drawText(BayesianUtils.COLOR_TEXT_LABEL, BayesianUtils.POSITION_X_TEXT_LABEL,
                BayesianUtils.POSITION_Y_TEXT_LABEL, BayesianUtils.WIDTH_TEXT_LABEL, BayesianUtils.HEIGHT_TEXT_LABEL,
                BayesianUtils.COLOR_TEXT_LABEL, nameFile, BayesianUtils.FONT_SIZE_TEXT_LABEL);
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

    private void clearScreen(Layer layer) {
        layer.draw();
        progressEvent.fire(new ProgressEvent(LienzoUtils.progressShapes));
        layerEvent.fire(new LayerEvent(null));
        probabilityEvent.fire(new ProbabilityEvent(null));
    }

}