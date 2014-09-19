package com.bayesian.network.client.perspectives;

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
 * A Perspective to show Bayesian related panels
 */
@ApplicationScoped
@WorkbenchPerspective(identifier = "WiresBayesianPerspective")
public class WiresBayesianPerspective {

    private static final String WIRES = "Wires";

    private static final String BAYESIAN_SCREEN = "BayesianScreen";
    private static final String WIRES_LAYERS_SCREEN = "WiresLayersScreen";
    private static final String WIRES_TEMPLATE_SCREEN = "WiresTemplateScreen";
    private static final String WIRES_ACTIONS_SCREEN = "WiresActionsScreen";
    private static final String BAYESIAN_SOUTH_SCREEN = "bayesianSouthScreen";

    private static final int MIN_WIDTH_PANEL = 200;
    private static final int WIDTH_PANEL = 300;

    private PerspectiveDefinition perspective;

    @Perspective
    public PerspectiveDefinition buildPerspective() {
        this.perspective = new PerspectiveDefinitionImpl( ROOT_STATIC );
        perspective.setName( WIRES );

        perspective.getRoot().addPart( new PartDefinitionImpl( new DefaultPlaceRequest( BAYESIAN_SCREEN ) ) );

        this.createPanelWithChild( perspective,
                                   Position.EAST );
        this.drawPanel( perspective,
                        Position.SOUTH, BAYESIAN_SOUTH_SCREEN );

        perspective.setTransient( true );

        return perspective;
    }

    private void drawPanel( final PerspectiveDefinition p,
                            final Position position,
                            final String identifierPanel ) {
        p.getRoot().insertChild( position,
                                 newPanel( p,
                                           position,
                                           identifierPanel ) );
    }

    private void createPanelWithChild( final PerspectiveDefinition p,
                                       final Position position ) {
        final PanelDefinition actionsPanel = newPanel( p,
                                                       position,
                                                       WIRES_ACTIONS_SCREEN );
        actionsPanel.setHeight( 150 );
        actionsPanel.setMinHeight( 80 );

        final PanelDefinition templatePanel = newPanel( p,
                                                        position,
                                                        WIRES_TEMPLATE_SCREEN );
        templatePanel.setHeight( 380 );
        templatePanel.setMinHeight( 250 );

        final PanelDefinition parentPanel = newPanel( p,
                                                      position,
                                                      WIRES_LAYERS_SCREEN );
        parentPanel.setHeight( 180 );
        parentPanel.setMinHeight( 150 );
        parentPanel.appendChild( Position.SOUTH,
                                 templatePanel );
        parentPanel.appendChild( Position.SOUTH,
                                 actionsPanel );
        p.getRoot().insertChild( position,
                                 parentPanel );

    }

    private PanelDefinition newPanel( final PerspectiveDefinition p,
                                      final Position position,
                                      final String identifierPanel ) {
        final PanelDefinition panel = new PanelDefinitionImpl( PanelType.MULTI_LIST );
        panel.setWidth( WIDTH_PANEL );
        panel.setMinWidth( MIN_WIDTH_PANEL );
        panel.addPart( new PartDefinitionImpl( new DefaultPlaceRequest( identifierPanel ) ) );
        return panel;
    }
}
