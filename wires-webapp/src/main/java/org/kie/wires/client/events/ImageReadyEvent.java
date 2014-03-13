package org.kie.wires.client.events;

import java.io.Serializable;
import java.util.List;

import org.kie.wires.core.client.shapes.ActionShape;

public class ImageReadyEvent implements Serializable {
    
    private static final long serialVersionUID = 8509466865934252823L;
    
    private List<ActionShape> actionsShape;
    
    public ImageReadyEvent(){
        
    }
    
    public ImageReadyEvent(List<ActionShape> actionsShape){
        this.actionsShape = actionsShape;
    }

    public List<ActionShape> getActionsShape() {
        return actionsShape;
    }

    public void setActionsShape(List<ActionShape> actionsShape) {
        this.actionsShape = actionsShape;
    }


}
