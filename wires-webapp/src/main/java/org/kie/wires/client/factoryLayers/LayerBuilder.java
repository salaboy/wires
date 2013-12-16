package org.kie.wires.client.factoryLayers;

import org.kie.wires.client.factoryShapes.ShapeType;

import com.emitrom.lienzo.client.core.shape.Group;
import com.emitrom.lienzo.client.core.shape.Layer;
import com.emitrom.lienzo.client.widget.LienzoPanel;
import com.google.gwt.user.client.ui.Composite;

public class LayerBuilder extends Composite {

	private static final int X_ACCORDION = 0;

	private static final int Y_ACCORDION = 5;
	
	public static int layers;

	public LayerBuilder() {
		layers = 0;
	}

	public LayerBuilder(ShapeType shapeType, Group group, LienzoPanel panel, Layer layer) {
		this.newLayer(group, shapeType, panel);
	}
	
	public void newLayer(Group group, final ShapeType shapeType, LienzoPanel panel) {
		layers += 1;
        switch (shapeType) {
        case LINE:
            new LayerLineFactory(group, panel, layers);
            break;
        case RECTANGLE:
            //TODO
            break;
        case CIRCLE:
        	//TODO
            break;
        default:
            throw new IllegalStateException("Unrecognized layer type '" + shapeType + "'!");
        }

    }

}
