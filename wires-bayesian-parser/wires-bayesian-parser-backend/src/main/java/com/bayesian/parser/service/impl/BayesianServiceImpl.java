package com.bayesian.parser.service.impl;

import java.io.InputStream;

import javax.enterprise.context.ApplicationScoped;

import org.jboss.errai.bus.server.annotations.Service;

import com.bayesian.parser.client.builder.BayesianBuilder;
import com.bayesian.parser.client.model.BayesNetwork;
import com.bayesian.parser.client.parser.Bif;
import com.bayesian.parser.client.parser.Definition;
import com.bayesian.parser.client.parser.Network;
import com.bayesian.parser.client.parser.Probability;
import com.bayesian.parser.client.service.BayesianService;
import com.thoughtworks.xstream.XStream;

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
