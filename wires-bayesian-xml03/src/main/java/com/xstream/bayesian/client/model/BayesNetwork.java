package com.xstream.bayesian.client.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jboss.errai.common.client.api.annotations.Portable;

@Portable
public class BayesNetwork<T> implements Serializable {

    private static final long serialVersionUID = 6231201134802600033L;

    private String name;
    private List<T> nodos;

    public BayesNetwork() {

    }

    public BayesNetwork(String name) {
        // TODO implement Guava
        this.nodos = new ArrayList<T>();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<T> getNodos() {
        return nodos;
    }

    public void setNodos(List<T> nodos) {
        this.nodos = nodos;
    }

}
