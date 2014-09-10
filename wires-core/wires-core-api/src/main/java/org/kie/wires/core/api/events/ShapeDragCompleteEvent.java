package org.kie.wires.core.api.events;

public class ShapeDragCompleteEvent {

    private String identifier;

    private int x;
    private int y;

    public ShapeDragCompleteEvent( final String identifier,
                                   final int x,
                                   final int y ) {
        this.identifier = identifier;
        this.x = x;
        this.y = y;
    }

    public String getIdentifier() {
        return identifier;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
