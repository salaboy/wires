/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bayesian.network.api.events;

import java.io.Serializable;
import java.util.List;

import org.jboss.errai.common.client.api.annotations.Portable;

import com.bayesian.network.api.shapes.EditableBayesianNode;

@Portable
public class ReadyEvent implements Serializable {
    
    private static final long serialVersionUID = 60336200253522640L;
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
