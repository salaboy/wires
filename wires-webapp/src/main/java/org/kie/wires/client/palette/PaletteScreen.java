/*
 * Copyright 2012 JBoss Inc
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

package org.kie.wires.client.palette;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import javax.inject.Inject;
import org.jboss.errai.ioc.client.container.SyncBeanManager;
import org.kie.wires.client.connectors.ConnectorsGroup;
import org.kie.wires.client.shapes.ShapesGroup;
import org.uberfire.client.annotations.WorkbenchPartTitle;
import org.uberfire.client.annotations.WorkbenchPartView;
import org.uberfire.client.annotations.WorkbenchScreen;

@Dependent
@WorkbenchScreen(identifier = "WiresPaletteScreen")
public class PaletteScreen
        extends Composite
        implements RequiresResize {

    interface ViewBinder
            extends
            UiBinder<Widget, PaletteScreen> {

    }

    private static ViewBinder uiBinder = GWT.create( ViewBinder.class );

    @Inject
    private SyncBeanManager iocManager;

    @UiField
    public SimplePanel shapes;

    @UiField
    public SimplePanel connectors;

   
    @PostConstruct
    public void init() {
        initWidget( uiBinder.createAndBindUi( this ) );
        shapes.add( iocManager.lookupBean( ShapesGroup.class ).getInstance() );
        connectors.add( iocManager.lookupBean( ConnectorsGroup.class ).getInstance() );
        
    }

    @WorkbenchPartTitle
    @Override
    public String getTitle() {
        return "Palette";
    }
    
    @WorkbenchPartView
    public IsWidget getView() {
        return this;
    }

    @Override
    public void onResize() {
        int height = getParent().getOffsetHeight();
        int width = getParent().getOffsetWidth();
        setPixelSize( width, height );
    }

}