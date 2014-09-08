package com.bayesian.network.client.events;

import java.io.Serializable;

import com.bayesian.network.client.shapes.EditableBayesianProbability;
import com.bayesian.parser.client.model.BayesVariable;

public class ProbabilityEvent implements Serializable {

    private static final long serialVersionUID = 7823462946356291601L;

    private BayesVariable variable;

    private EditableBayesianProbability bayesianProbabilityGrid;

    public ProbabilityEvent() {
        this.variable = null;
        this.bayesianProbabilityGrid = null;
    }

    public ProbabilityEvent( final BayesVariable var,
                             final EditableBayesianProbability bayesianProbabilityGrid ) {
        this.variable = var;
        this.bayesianProbabilityGrid = bayesianProbabilityGrid;
    }

    public BayesVariable getVariable() {
        return variable;
    }

    public void setVariable( final BayesVariable variable ) {
        this.variable = variable;
    }

    public EditableBayesianProbability getBayesianProbabilityGrid() {
        return bayesianProbabilityGrid;
    }

    public void setBayesianProbabilityGrid( final EditableBayesianProbability bayesianProbabilityGrid ) {
        this.bayesianProbabilityGrid = bayesianProbabilityGrid;
    }

}
