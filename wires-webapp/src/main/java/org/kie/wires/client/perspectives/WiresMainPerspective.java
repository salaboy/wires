package org.kie.wires.client.perspectives;

import static org.uberfire.workbench.model.PanelType.ROOT_STATIC;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

import org.kie.wires.client.events.BayesianEvent;
import org.uberfire.client.annotations.Perspective;
import org.uberfire.client.annotations.WorkbenchPerspective;
import org.uberfire.mvp.impl.DefaultPlaceRequest;
import org.uberfire.workbench.model.PanelDefinition;
import org.uberfire.workbench.model.PanelType;
import org.uberfire.workbench.model.PerspectiveDefinition;
import org.uberfire.workbench.model.Position;
import org.uberfire.workbench.model.impl.PanelDefinitionImpl;
import org.uberfire.workbench.model.impl.PartDefinitionImpl;
import org.uberfire.workbench.model.impl.PerspectiveDefinitionImpl;

import com.google.gwt.core.shared.GWT;

/**
 * A Perspective to show File Explorer
 */
@ApplicationScoped
@WorkbenchPerspective(identifier = "WiresMainPerspective", isDefault = true)
public class WiresMainPerspective {

    private static final String WIRES = "Wires";

    private static final String WIRES_LAYERS_SCREEN = "WiresLayersScreen";
    private static final String WIRES_PALETTE_SCREEN = "WiresPaletteScreen";
    private static final String WIRES_CANVAS_SCREEN = "WiresCanvasScreen";
    private static final String WIRES_TEMPLATE_SCREEN = "WiresTemplateScreen";
    private static final String BAYESIAN_SOUTH_SCREEN = "bayesianSouthScreen";
    

    private static final int MIN_WIDTH_PANEL = 200;
    private static final int WIDTH_PANEL = 300;
    
    private PerspectiveDefinition perspective;

    @Perspective
    public PerspectiveDefinition buildPerspective() {
        this.perspective = new PerspectiveDefinitionImpl(ROOT_STATIC);
        perspective.setName(WIRES);

        perspective.getRoot().addPart(new PartDefinitionImpl(new DefaultPlaceRequest(WIRES_CANVAS_SCREEN)));

        this.createPanelWithChild(perspective, Position.EAST);
        //this.createPanel1(perspective, Position.EAST, WIRES_TEMPLATE_SCREEN);
        this.drawPanel(perspective, Position.WEST, WIRES_PALETTE_SCREEN);

        perspective.setTransient(true);

        pepe();
        
        return perspective;
    }

    private void drawPanel(PerspectiveDefinition p, Position position, String identifierPanel) {
        p.getRoot().insertChild(position, newPanel(p, position, identifierPanel));
    }
    
    private void createPanelWithChild(PerspectiveDefinition p, Position position) {
        final PanelDefinition childPanel = newPanel(p, position, WIRES_TEMPLATE_SCREEN);
        childPanel.setHeight(380);
        childPanel.setMinHeight(250);
        
        final PanelDefinition parentPanel = newPanel(p, position, WIRES_LAYERS_SCREEN);
        parentPanel.setHeight(180);
        parentPanel.setMinHeight(150);
        parentPanel.appendChild(Position.SOUTH, childPanel);
        p.getRoot().insertChild(position, parentPanel);
        
    }
    
    private PanelDefinition newPanel(PerspectiveDefinition p, Position position, String identifierPanel) {
        final PanelDefinition panel = new PanelDefinitionImpl(PanelType.MULTI_LIST);
        panel.setWidth(WIDTH_PANEL);
        panel.setMinWidth(MIN_WIDTH_PANEL);
        panel.addPart(new PartDefinitionImpl(new DefaultPlaceRequest(identifierPanel)));
        return panel;
    }
    
    public void pepe(){
        this.drawPanel(perspective, Position.SOUTH, BAYESIAN_SOUTH_SCREEN);
    }
    
    

}
