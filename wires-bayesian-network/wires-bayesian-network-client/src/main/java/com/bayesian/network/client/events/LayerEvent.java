package com.bayesian.network.client.events;

import java.io.Serializable;
import java.util.List;

import com.bayesian.parser.client.model.BayesVariable;
import org.jboss.errai.common.client.api.annotations.Portable;

@Portable
public class LayerEvent implements Serializable {

    private static final long serialVersionUID = 7091879717026728559L;
    private List<BayesVariable> nodes;

    public LayerEvent() {

    }

    public LayerEvent( final List<BayesVariable> nodes ) {
        this.nodes = nodes;
    }

    public List<BayesVariable> getNodes() {
        return nodes;
    }

    public void setNodes( final List<BayesVariable> nodes ) {
        this.nodes = nodes;
    }

}
