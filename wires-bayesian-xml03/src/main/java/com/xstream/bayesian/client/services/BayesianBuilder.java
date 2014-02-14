package com.xstream.bayesian.client.services;

import java.util.List;

import com.google.common.collect.Lists;
import com.xstream.bayesian.client.model.BayesNetwork;
import com.xstream.bayesian.client.model.BayesVariable;
import com.xstream.bayesian.client.parser.model.Bif;
import com.xstream.bayesian.client.parser.model.Definition;
import com.xstream.bayesian.client.parser.model.Network;
import com.xstream.bayesian.client.parser.model.Variable;

public class BayesianBuilder {

    public BayesNetwork<BayesVariable> build(Bif bif) {
        BayesNetwork<BayesVariable> bayesNetwork = new BayesNetwork<BayesVariable>(bif.getNetwork().getName());
        int id = 1;
        for (Definition def : bif.getNetwork().getDefinitions()) {
            BayesVariable nodo = buildVariable(def, bif.getNetwork(), id);
            bayesNetwork.getNodos().add(nodo);
            id += 1;
        }
        return bayesNetwork;
    }

    private BayesVariable buildVariable(Definition def, Network network, int id) {
        List<String> outcomes = Lists.newArrayList();
        double[][] position = new double[2][2];
        this.getOutcomesByVariable(network, def.getName(), outcomes, position);
        return new BayesVariable(def.getName(), id, outcomes, this.getProbabilities(def.getProbabilities()), def.getGiven(),
                position);
    }

    private void getOutcomesByVariable(Network network, String nameDefinition, List<String> outcomes, double[][] position) {
        for (Variable var : network.getVariables()) {
            if (var.getName().equals(nameDefinition)) {
                for (String outcome : var.getOutComes()) {
                    outcomes.add(outcome);
                }
                // get position
                position = getPosition(var.getPosition(), position);
            }
        }
    }

    private double[][] getProbabilities(String table) {
        double probabilities[][] = new double[table.split(" ").length][table.split(" ").length];
        int i = 0;
        int j = 0;
        for (String prob : table.split(" ")) {
            probabilities[i][j] = Double.parseDouble(prob);
            if (i < j) {
                i += 1;
            }
            j += 1;
        }
        return probabilities;
    }

    private double[][] getPosition(String stringPosition, double[][] position) {
        if (stringPosition != null) {
            stringPosition = stringPosition.replace("position", "");
            stringPosition = stringPosition.replace("=", "");
            stringPosition = stringPosition.replace("(", "");
            stringPosition = stringPosition.replace(")", "");
            stringPosition = stringPosition.trim();
            int i = 0;
            int j = 0;
            for (String pos : stringPosition.split(",")) {
                position[i][j] = Double.parseDouble(pos);
                if (i < j) {
                    i += 1;
                }
                j += 1;
            }
        }
        return null;
    }

}
