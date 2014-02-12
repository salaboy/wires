package org.kie.wires.client.events;

public class BayesianEvent {

    private String template;

    public BayesianEvent(String template) {
        this.template = template;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

}
