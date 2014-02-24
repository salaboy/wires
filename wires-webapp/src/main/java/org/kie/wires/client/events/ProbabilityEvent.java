package org.kie.wires.client.events;

import com.xstream.bayesian.client.model.BayesVariable;

public class ProbabilityEvent {
    
    BayesVariable variable;
    
    public ProbabilityEvent(BayesVariable var){
        this.variable = var;
    }

    public BayesVariable getVariable() {
        return variable;
    }

    public void setVariable(BayesVariable variable) {
        this.variable = variable;
    }

}
