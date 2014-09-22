package org.kie.wires.core.trees.client.perspectives;

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

import static org.uberfire.workbench.model.PanelType.*;

/**
 * A Perspective for Wires Scratch Pad
 */
@ApplicationScoped
@WorkbenchPerspective(identifier = "WiresTreesPerspective")
public class WiresTreesPerspective {

    private static final String WIRES = "Wires Trees";

    private static final String WIRES_LAYERS_SCREEN = "WiresLayersScreen";
    private static final String WIRES_PALETTE_SCREEN = "WiresTreesPaletteScreen";
    private static final String WIRES_CANVAS_SCREEN = "WiresTreesScreen";
    private static final String WIRES_PROPERTIES_SCREEN = "WiresPropertiesScreen";

    private static final int MIN_WIDTH_PANEL = 200;
    private static final int WIDTH_PANEL = 300;

    private PerspectiveDefinition perspective;

    @Perspective
    public PerspectiveDefinition buildPerspective() {
        this.perspective = new PerspectiveDefinitionImpl( ROOT_SIMPLE );
        perspective.setName( WIRES );

        perspective.getRoot().addPart( new PartDefinitionImpl( new DefaultPlaceRequest( WIRES_CANVAS_SCREEN ) ) );

        final PanelDefinition palettePanel = new PanelDefinitionImpl( PanelType.MULTI_LIST );
        palettePanel.setMinWidth( MIN_WIDTH_PANEL );
        palettePanel.setWidth( WIDTH_PANEL );
        palettePanel.addPart( new PartDefinitionImpl( new DefaultPlaceRequest( WIRES_PALETTE_SCREEN ) ) );

        final PanelDefinition propertiesPanel = new PanelDefinitionImpl( PanelType.MULTI_LIST );
        propertiesPanel.setMinWidth( MIN_WIDTH_PANEL );
        propertiesPanel.setWidth( WIDTH_PANEL );
        propertiesPanel.addPart( new PartDefinitionImpl( new DefaultPlaceRequest( WIRES_PROPERTIES_SCREEN ) ) );
        palettePanel.appendChild( Position.SOUTH,
                                  propertiesPanel );

        perspective.getRoot().insertChild( Position.WEST,
                                           palettePanel );

        final PanelDefinition layersPanel = new PanelDefinitionImpl( PanelType.MULTI_LIST );
        layersPanel.setMinWidth( MIN_WIDTH_PANEL );
        layersPanel.setWidth( WIDTH_PANEL );
        layersPanel.addPart( new PartDefinitionImpl( new DefaultPlaceRequest( WIRES_LAYERS_SCREEN ) ) );

        perspective.getRoot().insertChild( Position.EAST,
                                           layersPanel );

        perspective.setTransient( true );

        return perspective;
    }

}
