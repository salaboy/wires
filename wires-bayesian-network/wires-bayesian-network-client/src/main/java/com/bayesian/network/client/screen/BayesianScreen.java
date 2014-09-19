/*
 * Copyright 2014 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bayesian.network.client.screen;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.bayesian.network.client.events.BayesianTemplateSelectedEvent;
import com.bayesian.network.client.events.RenderBayesianNetworkEvent;
import com.bayesian.network.client.factory.BayesianFactory;
import com.bayesian.network.client.shapes.EditableBayesianNode;
import com.google.gwt.user.client.ui.IsWidget;
import org.kie.wires.core.api.events.ClearEvent;
import org.kie.wires.core.api.events.ShapeSelectedEvent;
import org.kie.wires.core.api.shapes.WiresBaseShape;
import org.kie.wires.core.client.canvas.WiresCanvas;
import org.uberfire.client.annotations.WorkbenchPartTitle;
import org.uberfire.client.annotations.WorkbenchPartView;
import org.uberfire.client.annotations.WorkbenchScreen;

/**
 * A custom WiresCanvas implementation to handle Bayesian Networks
 */
@Dependent
@WorkbenchScreen(identifier = "BayesianScreen")
public class BayesianScreen extends WiresCanvas {

    @Inject
    private BayesianFactory factory;

    @Inject
    private Event<ClearEvent> clearEvent;

    @Inject
    private Event<ShapeSelectedEvent> shapeSelectedEvent;

    public void onBayesianEvent( @Observes BayesianTemplateSelectedEvent event ) {
        factory.init( event.getTemplate() );
    }

    public void onReadyEvent( @Observes RenderBayesianNetworkEvent event ) {
        //ClearEvent clears Variables Panel and this Canvas
        clearEvent.fire( new ClearEvent() );
        for ( EditableBayesianNode node : event.getBayesianNodes() ) {
            addShape( node );
        }
    }

    public void onClearEvent( @Observes ClearEvent clearEvent ) {
        clear();
    }

    @WorkbenchPartTitle
    @Override
    public String getTitle() {
        return "Bayesian Network";
    }

    @WorkbenchPartView
    public IsWidget getView() {
        return this;
    }

    @Override
    public void selectShape( final WiresBaseShape shape ) {
        super.selectShape( shape );
        shapeSelectedEvent.fire( new ShapeSelectedEvent( shape ) );
    }

}
