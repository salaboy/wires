package org.kie.wires.client.perspectives;

import static org.uberfire.workbench.model.PanelType.ROOT_STATIC;

import javax.enterprise.context.ApplicationScoped;

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
    

    private static final int MIN_WIDTH_PANEL = 200;
    private static final int WIDTH_PANEL = 300;

    @Perspective
    public PerspectiveDefinition buildPerspective() {
        final PerspectiveDefinition perspective = new PerspectiveDefinitionImpl(ROOT_STATIC);
        perspective.setName(WIRES);

        perspective.getRoot().addPart(new PartDefinitionImpl(new DefaultPlaceRequest(WIRES_CANVAS_SCREEN)));

        this.createPanel1(perspective, Position.EAST);
        //this.createPanel1(perspective, Position.EAST, WIRES_TEMPLATE_SCREEN);
        this.createPanel(perspective, Position.WEST, WIRES_PALETTE_SCREEN);

        perspective.setTransient(true);

        return perspective;
    }

    private void createPanel(PerspectiveDefinition p, Position position, String identifierPanel) {
        final PanelDefinition panel = new PanelDefinitionImpl(PanelType.MULTI_LIST);
        panel.setWidth(WIDTH_PANEL);
        panel.setMinWidth(MIN_WIDTH_PANEL);
        panel.addPart(new PartDefinitionImpl(new DefaultPlaceRequest(identifierPanel)));
        p.getRoot().insertChild(position, panel);
    }
    
    private void createPanel1(PerspectiveDefinition p, Position position) {
        final PanelDefinition panel = new PanelDefinitionImpl(PanelType.MULTI_LIST);
        panel.setWidth(WIDTH_PANEL);
        panel.setMinWidth(MIN_WIDTH_PANEL);
        panel.setHeight(380);
        panel.setMinHeight(250);
        panel.addPart(new PartDefinitionImpl(new DefaultPlaceRequest(WIRES_TEMPLATE_SCREEN)));
        
        
        final PanelDefinition panel1 = new PanelDefinitionImpl(PanelType.MULTI_LIST);
        panel1.setWidth(WIDTH_PANEL);
        panel1.setMinWidth(MIN_WIDTH_PANEL);
        panel1.setHeight(180);
        panel1.setMinHeight(150);
        panel1.appendChild(Position.SOUTH, panel);
        panel1.addPart(new PartDefinitionImpl(new DefaultPlaceRequest(WIRES_LAYERS_SCREEN)));
        p.getRoot().insertChild(position, panel1);
        
        
        
        
        
        
        
    }

}
