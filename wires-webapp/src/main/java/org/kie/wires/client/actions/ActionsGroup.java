package org.kie.wires.client.actions;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.emitrom.lienzo.client.core.shape.Layer;
import com.emitrom.lienzo.client.widget.LienzoPanel;
import com.google.gwt.user.client.ui.Composite;
import org.kie.wires.client.events.ImageReadyEvent;
import org.kie.wires.client.factoryShapes.ActionFactory;
import org.kie.wires.client.factoryShapes.categories.ActionCategory;
import org.kie.wires.core.api.events.ClearEvent;
import org.kie.wires.core.client.factories.ShapeFactoryCache;
import org.kie.wires.core.client.shapes.ActionShape;
import org.kie.wires.core.client.util.ShapeFactoryUtil;
import org.kie.wires.core.client.util.ShapesUtils;

@Dependent
public class ActionsGroup extends Composite {

    private Layer layer;
    private LienzoPanel panel;

    @Inject
    private Event<ClearEvent> clearEvent;

    @Inject
    private Event<ImageReadyEvent> imageReadyEvent;

    @Inject
    private ShapeFactoryCache factoriesCache;

    @PostConstruct
    public void init() {
        panel = new LienzoPanel( ShapeFactoryUtil.WIDTH_PANEL,
                                 ShapesUtils.calculateHeight( ShapesUtils.getNumberOfShapesInCategory( ActionCategory.CATEGORY,
                                                                                                       factoriesCache.getShapeFactories() ) ) );
        layer = new Layer();
        panel.getScene().add( layer );
        initWidget( panel );

        drawActions();
    }

    private void drawActions() {
        new ActionFactory( clearEvent, imageReadyEvent );
    }

    public void readyEvent( @Observes ImageReadyEvent event ) {
        for ( ActionShape action : event.getActionsShape() ) {
            layer.add( action );
        }
        layer.draw();
    }

}
