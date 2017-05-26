package com.vaadin.template.orders.app.loadtest;

import com.vaadin.server.DeploymentConfiguration;
import com.vaadin.server.ServiceException;
import com.vaadin.server.VaadinServletService;
import com.vaadin.spring.server.SpringVaadinServlet;
import com.vaadin.template.orders.app.ApplicationConfiguration;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Customized SpringVaadinServlet for enabling the LoadTestServletService
 */
public class LoadTestVaadinSpringServlet extends SpringVaadinServlet {

    @Override
    protected VaadinServletService createServletService(
            DeploymentConfiguration deploymentConfiguration)
            throws ServiceException {
        Logger.getLogger(ApplicationConfiguration.class.getName()).log(
                Level.WARNING,
                "Running on Load Test mode, do not use this in production");

        LoadTestServletService service = new LoadTestServletService(this,
                deploymentConfiguration, this.getServiceUrlPath());
        service.init();
        return service;
    }
}
