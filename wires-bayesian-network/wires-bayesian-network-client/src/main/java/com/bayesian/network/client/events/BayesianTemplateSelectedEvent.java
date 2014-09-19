package com.bayesian.network.client.events;

import java.io.Serializable;

import org.jboss.errai.common.client.api.annotations.Portable;

@Portable
public class BayesianTemplateSelectedEvent implements Serializable {

    private static final long serialVersionUID = -695192650020463533L;
    private String template;

    public BayesianTemplateSelectedEvent() {

    }

    public BayesianTemplateSelectedEvent( final String template ) {
        this.template = template;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate( final String template ) {
        this.template = template;
    }

}
