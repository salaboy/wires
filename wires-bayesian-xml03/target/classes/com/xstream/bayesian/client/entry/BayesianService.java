package com.xstream.bayesian.client.entry;

import org.jboss.errai.bus.server.annotations.Remote;

import com.xstream.bayesian.client.model.BayesNetwork;
import com.xstream.bayesian.client.parser.model.Bif;


@Remote
public interface BayesianService {

    BayesNetwork buildXml03(String relativePathtoXmlResource);

    Bif xmlToObject(String relativePathtoXmlResource);

}
