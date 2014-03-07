package org.kie.wires.client.events;

import org.kie.wires.client.bayesian.shapes.BayesianProbability;

import com.bayesian.parser.client.model.BayesVariable;

public class ProbabilityEvent {
    
    private BayesVariable variable;
    
    private BayesianProbability bayesianProbabilityGrid;
    
    public ProbabilityEvent(){
        this.variable = null;
        this.bayesianProbabilityGrid = null;
    }
    
    public ProbabilityEvent(BayesVariable var, BayesianProbability bayesianProbabilityGrid){
        this.variable = var;
        this.bayesianProbabilityGrid = bayesianProbabilityGrid;
    }

    public BayesVariable getVariable() {
        return variable;
    }

    public void setVariable(BayesVariable variable) {
        this.variable = variable;
    }

    public BayesianProbability getBayesianProbabilityGrid() {
        return bayesianProbabilityGrid;
    }

    public void setBayesianProbabilityGrid(BayesianProbability bayesianProbabilityGrid) {
        this.bayesianProbabilityGrid = bayesianProbabilityGrid;
    }

}
