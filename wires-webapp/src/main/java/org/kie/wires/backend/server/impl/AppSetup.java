package org.kie.wires.backend.server.impl;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import org.uberfire.commons.services.cdi.Startup;
import org.uberfire.commons.services.cdi.StartupType;


@Startup(StartupType.BOOTSTRAP)
@ApplicationScoped
public class AppSetup {

    public AppSetup() {
    }

    @PostConstruct
    public void init(){
    
    }

}
