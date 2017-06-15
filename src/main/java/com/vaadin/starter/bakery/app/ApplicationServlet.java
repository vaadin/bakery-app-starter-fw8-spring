package com.vaadin.starter.bakery.app;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Value;

import com.vaadin.server.DeploymentConfiguration;
import com.vaadin.server.ServiceException;
import com.vaadin.server.VaadinServletService;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.server.SpringVaadinServlet;
import com.vaadin.starter.bakery.app.loadtest.LoadTestServletService;

/**
 * Servlet for serving the whole application.
 * <p>
 * Delegates all heavy lifting to SpringVaadinServlet.
 * <p>
 * In case of load test mode being enabled, uses a separate connector id
 * generator to make scalability tests more stable.
 * <p>
 * Uses a bootstrap listener to modify the bootstrap HTML page and include icons
 * for home screen for mobile devices.
 * <p>
 * The bean name is {@literal vaadinServlet} because Vaadin Spring integration
 * will automatically search for a bean named "vaadinServlet" and use that.
 */
@SpringComponent("vaadinServlet")
public class ApplicationServlet extends SpringVaadinServlet {

	/**
	 * Is load test mode enabled (false by default)
	 */
	@Value("${loadtestmode.enabled}")
	private boolean loadTestModeEnabled;

	@Override
	protected void servletInitialized() throws ServletException {
		super.servletInitialized();
		getService()
				.addSessionInitListener(event -> event.getSession().addBootstrapListener(new IconBootstrapListener()));
	}

	@Override
	protected VaadinServletService createServletService(DeploymentConfiguration deploymentConfiguration)
			throws ServiceException {
		if (loadTestModeEnabled) {
			Logger.getLogger(ApplicationConfiguration.class.getName()).log(Level.WARNING,
					"Running in load test mode, do not use this in production");

			LoadTestServletService service = new LoadTestServletService(this, deploymentConfiguration,
					getServiceUrlPath());
			service.init();
			return service;
		} else {
			return super.createServletService(deploymentConfiguration);
		}
	}

}
