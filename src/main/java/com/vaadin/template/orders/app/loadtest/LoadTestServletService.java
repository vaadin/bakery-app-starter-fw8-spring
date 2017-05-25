package com.vaadin.template.orders.app.loadtest;

import com.vaadin.server.DeploymentConfiguration;
import com.vaadin.server.ServiceException;
import com.vaadin.server.VaadinRequest;
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
	protected VaadinSession createVaadinSession(VaadinRequest request) throws ServiceException {
		return new LoadTestServletSession(this);
	}
}
