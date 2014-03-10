package com.bayesian.xstream.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import javax.enterprise.context.ApplicationScoped;

import org.jboss.errai.bus.server.annotations.Service;

import com.thoughtworks.xstream.XStream;
import com.xstream.bayesian.client.entry.BayesianService;
import com.xstream.bayesian.client.model.BayesNetwork;
import com.xstream.bayesian.client.parser.model.Bif;
import com.xstream.bayesian.client.parser.model.Definition;
import com.xstream.bayesian.client.parser.model.Network;
import com.xstream.bayesian.client.parser.model.Probability;
import com.xstream.bayesian.client.services.BayesianBuilder;

@Service
@ApplicationScoped
public class BayesianServiceImpl implements BayesianService {

    @Override
    public BayesNetwork buildXml03(String relativePathtoXmlResource) {
        return new BayesianBuilder().build(xmlToObject(relativePathtoXmlResource));
    }

    @Override
    public Bif xmlToObject(String relativePathtoXmlResource) {
        InputStream resourceAsStream = loadResource( relativePathtoXmlResource );
        return processXML( resourceAsStream );
    }

    private Bif processXML( InputStream resourceAsStream ) {
        XStream xstream = new XStream();
        xstream.processAnnotations(Bif.class);
        xstream.processAnnotations(Network.class);
        xstream.processAnnotations(Probability.class);
        xstream.processAnnotations(Definition.class);
        return (Bif) xstream.fromXML(resourceAsStream);
    }

    private InputStream loadResource( String xmlFileName ) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        return loader.getResourceAsStream(xmlFileName);
    }

}
