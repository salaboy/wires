package org.kie.wires.client.factoryLayers;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;


public interface ResourcesLayers extends ClientBundle {
    
    ResourcesLayers INSTANCE = GWT.create(ResourcesLayers.class);
    
    @Source("org/kie/wires/public/images/layerPanel/delete.png")
    public ImageResource delete();
    
    @Source("org/kie/wires/public/images/layerPanel/view.png")
    public ImageResource view();

}
