/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.kie.wires.client.events;

import java.util.List;
import org.kie.wires.client.bayesian.shapes.EditableBayesianNode;

/**
 *
 * @author salaboy
 */
public class ReadyEvent {
    private List<EditableBayesianNode> bayesianNodes;

    public ReadyEvent() {
    }

    
    public ReadyEvent(List<EditableBayesianNode> bayesianNodes) {
        this.bayesianNodes = bayesianNodes;
    }

    public List<EditableBayesianNode> getBayesianNodes() {
        return bayesianNodes;
    }

    
    
    
}
