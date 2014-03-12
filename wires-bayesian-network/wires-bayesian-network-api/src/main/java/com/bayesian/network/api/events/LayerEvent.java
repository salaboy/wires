package com.bayesian.network.api.events;

import java.io.Serializable;
import java.util.List;

import org.jboss.errai.common.client.api.annotations.Portable;

import com.bayesian.parser.client.model.BayesVariable;

@Portable
public class LayerEvent implements Serializable  {
    
    private static final long serialVersionUID = 7091879717026728559L;
    private List<BayesVariable> nodes;
    
    public LayerEvent(){
        
    }
    
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
