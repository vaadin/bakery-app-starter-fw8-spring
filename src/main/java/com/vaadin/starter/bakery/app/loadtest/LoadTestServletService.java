package com.vaadin.starter.bakery.app.loadtest;

import java.util.List;

import com.vaadin.server.ConnectorIdGenerator;
import com.vaadin.server.DeploymentConfiguration;
import com.vaadin.server.ServiceException;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.server.SpringVaadinServletService;

/**
 * Extended VaadinServletService to create LoadTestServletSession to enable
 * stable load testing with component ids used for communication
 */
public class LoadTestServletService extends SpringVaadinServletService {

	public LoadTestServletService(VaadinServlet servlet, DeploymentConfiguration deploymentConfiguration,
			String serviceUrl) throws ServiceException {
		super(servlet, deploymentConfiguration, serviceUrl);
	}

	@Override
	protected ConnectorIdGenerator initConenctorIdGenerator(List<ConnectorIdGenerator> addedConnectorIdGenerators)
			throws ServiceException {
		return event -> getGenerator(event.getSession()).generateConnectorId(event);
	}

	protected ConnectorIdGenerator getGenerator(VaadinSession session) {
		ConnectorIdGenerator generator = session.getAttribute(ConnectorIdGenerator.class);
		if (generator == null) {
			generator = new ComponentIdBasedConnectorIdGenerator();
			session.setAttribute(ConnectorIdGenerator.class, generator);
		}
		return generator;
	}

}
