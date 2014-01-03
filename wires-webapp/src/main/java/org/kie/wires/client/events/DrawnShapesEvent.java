package org.kie.wires.client.events;

import java.util.Map;

import org.kie.wires.client.factoryLayers.LayerGroup;

public class DrawnShapesEvent {

    private Integer keyDrawnShapes;

    private Map<Integer, LayerGroup> listLayerGroup;

    public DrawnShapesEvent() {

    }

    public DrawnShapesEvent(Integer key) {
        this.keyDrawnShapes = key;
    }

    public Integer getKeyDrawnShapes() {
        return keyDrawnShapes;
    }

    public void setKeyDrawnShapes(Integer keyDrawnShapes) {
        this.keyDrawnShapes = keyDrawnShapes;
    }

    public Map<Integer, LayerGroup> getListLayerGroup() {
        return listLayerGroup;
    }

    public void setListLayerGroup(Map<Integer, LayerGroup> listLayerGroup) {
        this.listLayerGroup = listLayerGroup;
    }

}
