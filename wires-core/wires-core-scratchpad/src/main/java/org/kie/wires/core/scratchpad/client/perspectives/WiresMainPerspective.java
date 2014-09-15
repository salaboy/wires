package org.kie.wires.core.scratchpad.client.perspectives;

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
@WorkbenchPerspective(identifier = "WiresScratchPadPerspective", isDefault = true)
public class WiresMainPerspective {

    private static final String WIRES = "Wires";

    private static final String WIRES_LAYERS_SCREEN = "WiresLayersScreen";
    private static final String WIRES_PALETTE_SCREEN = "WiresPaletteScreen";
    private static final String WIRES_CANVAS_SCREEN = "WiresCanvasScreen";
    private static final String WIRES_ACTIONS_SCREEN = "WiresActionsScreen";
    private static final String WIRES_PROPERTIES_SCREEN = "WiresPropertiesScreen";

    private static final int MIN_WIDTH_PANEL = 200;
    private static final int WIDTH_PANEL = 300;

    private PerspectiveDefinition perspective;

    @Perspective
    public PerspectiveDefinition buildPerspective() {
        this.perspective = new PerspectiveDefinitionImpl( ROOT_SIMPLE );
        perspective.setName( WIRES );

        perspective.getRoot().addPart( new PartDefinitionImpl( new DefaultPlaceRequest( WIRES_CANVAS_SCREEN ) ) );

        createPanelWithChild( perspective,
                              Position.EAST );

        final PanelDefinition panel = new PanelDefinitionImpl( PanelType.MULTI_LIST );
        panel.setMinWidth( MIN_WIDTH_PANEL );
        panel.setWidth( WIDTH_PANEL );
        panel.addPart( new PartDefinitionImpl( new DefaultPlaceRequest( WIRES_PALETTE_SCREEN ) ) );
        PanelDefinition propertiesPanel = new PanelDefinitionImpl( PanelType.MULTI_LIST );
        propertiesPanel.setMinWidth( MIN_WIDTH_PANEL );
        propertiesPanel.setWidth( WIDTH_PANEL );
        propertiesPanel.addPart( new PartDefinitionImpl( new DefaultPlaceRequest( WIRES_PROPERTIES_SCREEN ) ) );
        panel.appendChild( Position.SOUTH,
                           propertiesPanel );

        perspective.getRoot().insertChild( Position.WEST,
                                           panel );

        perspective.setTransient( true );

        return perspective;
    }

    private void createPanelWithChild( final PerspectiveDefinition p,
                                       final Position position ) {
        final PanelDefinition actionsPanel = newPanel( p,
                                                       WIRES_ACTIONS_SCREEN );
        actionsPanel.setHeight( 150 );
        actionsPanel.setMinHeight( 80 );

        final PanelDefinition parentPanel = newPanel( p,
                                                      WIRES_LAYERS_SCREEN );
        parentPanel.setHeight( 180 );
        parentPanel.setMinHeight( 150 );
        parentPanel.appendChild( Position.SOUTH,
                                 actionsPanel );
        p.getRoot().insertChild( position,
                                 parentPanel );
    }

    private PanelDefinition newPanel( final PerspectiveDefinition p,
                                      final String identifierPanel ) {
        final PanelDefinition panel = new PanelDefinitionImpl( PanelType.MULTI_LIST );
        panel.setWidth( WIDTH_PANEL );
        panel.setMinWidth( MIN_WIDTH_PANEL );
        panel.addPart( new PartDefinitionImpl( new DefaultPlaceRequest( identifierPanel ) ) );
        return panel;
    }
}
