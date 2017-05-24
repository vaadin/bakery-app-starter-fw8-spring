package com.vaadin.template.orders.app.loadtest;

import com.vaadin.server.*;
import org.vaadin.spring.servlet.*;

/**
 * Extended VaadinServletService to create LoadTestServletSession to enable
 * stable load testing with component ids used for communication
 */
public class LoadTestServletService extends Vaadin4SpringServletService {

    public LoadTestServletService(VaadinServlet servlet,
                                  DeploymentConfiguration deploymentConfiguration, String serviceUrl)
            throws ServiceException {
        super(servlet, deploymentConfiguration, serviceUrl);
    }

    @Override
    protected VaadinSession createVaadinSession(VaadinRequest request)
            throws ServiceException {
        return new LoadTestServletSession(this);
    }
}
