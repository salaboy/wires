package org.kie.wires.backend.server.impl;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.uberfire.backend.repositories.RepositoryService;
import org.uberfire.commons.services.cdi.Startup;
import org.uberfire.commons.services.cdi.StartupType;


@Startup(StartupType.BOOTSTRAP)
@ApplicationScoped
public class AppSetup {
	
    private static final String PLAYGROUND_ALIAS = "wires";

    @Inject
    private RepositoryService repositoryService;

    public AppSetup() {
    }

    @PostConstruct
    public void init(){
    	repositoryService.getRepository( PLAYGROUND_ALIAS );
    }

}
