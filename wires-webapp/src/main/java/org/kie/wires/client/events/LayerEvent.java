package org.kie.wires.client.events;

import java.util.List;

import com.xstream.bayesian.client.model.BayesVariable;

public class LayerEvent {
    
    private List<BayesVariable> nodes;
    
    public LayerEvent(List<BayesVariable> nodes){
        this.nodes = nodes;
    }

    public List<BayesVariable> getNodes() {
        return nodes;
    }

    public void setNodes(List<BayesVariable> nodes) {
        this.nodes = nodes;
    }
    
   

}
