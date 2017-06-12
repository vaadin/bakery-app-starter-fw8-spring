package com.vaadin.starter.bakery.app.loadtest;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.vaadin.server.DeploymentConfiguration;
import com.vaadin.server.ServiceException;
import com.vaadin.server.VaadinServletService;
import com.vaadin.starter.bakery.app.ApplicationConfiguration;
import com.vaadin.starter.bakery.app.ApplicationServlet;

/**
 * Customized SpringVaadinServlet for enabling the LoadTestServletService
 */
public class LoadTestVaadinSpringServlet extends ApplicationServlet {

	@Override
	protected VaadinServletService createServletService(DeploymentConfiguration deploymentConfiguration)
			throws ServiceException {
		Logger.getLogger(ApplicationConfiguration.class.getName()).log(Level.WARNING,
				"Running on Load Test mode, do not use this in production");

		LoadTestServletService service = new LoadTestServletService(this, deploymentConfiguration,
				this.getServiceUrlPath());
		service.init();
		return service;
	}
}
