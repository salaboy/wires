/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bayesian.network.client.events;

import java.io.Serializable;
import java.util.List;

import com.bayesian.network.client.shapes.EditableBayesianNode;
import org.jboss.errai.common.client.api.annotations.Portable;

@Portable
public class ReadyEvent implements Serializable {

    private static final long serialVersionUID = 60336200253522640L;
    private List<EditableBayesianNode> bayesianNodes;

    public ReadyEvent() {
    }

    public ReadyEvent( final List<EditableBayesianNode> bayesianNodes ) {
        this.bayesianNodes = bayesianNodes;
    }

    public List<EditableBayesianNode> getBayesianNodes() {
        return bayesianNodes;
    }

}
