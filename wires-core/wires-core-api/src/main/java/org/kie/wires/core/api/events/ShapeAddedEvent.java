package org.kie.wires.core.api.events;

public class ShapeAddedEvent {

    private String identifier;

    public ShapeAddedEvent( final String identifier ) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

}
