package com.bayesian.parser.client.service;

import org.jboss.errai.bus.server.annotations.Remote;

import com.bayesian.parser.client.model.BayesNetwork;
import com.bayesian.parser.client.parser.Bif;


@Remote
public interface BayesianService {

    BayesNetwork buildXml03(String relativePathtoXmlResource);

    Bif xmlToObject(String relativePathtoXmlResource);

}
