package com.vaadin.starter.bakery.app;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.vaadin.server.ConnectorIdGenerator;
import com.vaadin.server.ServiceInitEvent;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinServiceInitListener;
import com.vaadin.server.VaadinServletService;
import com.vaadin.server.VaadinSession;
import com.vaadin.starter.bakery.app.loadtest.ComponentIdBasedConnectorIdGenerator;

/**
 * Configures the VaadinService instance that serves the app through a servlet.
 * <p>
 * In case of load test mode being enabled, uses a separate connector id
 * generator to make scalability tests more stable.
 * <p>
 * Uses a bootstrap listener to modify the bootstrap HTML page and include icons
 * for home screen for mobile devices.
 */
public class ApplicationInitListener implements VaadinServiceInitListener {

    @Override
    public void serviceInit(ServiceInitEvent serviceInitEvent) {
        VaadinService service = serviceInitEvent.getSource();

        service.addSessionInitListener(event -> event.getSession()
                .addBootstrapListener(new IconBootstrapListener()));

        if (isLoadTestEnabled(service)) {
            Logger.getLogger(ApplicationInitListener.class.getName()).log(
                    Level.WARNING,
                    "Running in load test mode, do not use this in production");

            serviceInitEvent.addConnectorIdGenerator(
                    event -> getGenerator(event.getSession())
                            .generateConnectorId(event));
        }
    }

    private static boolean isLoadTestEnabled(VaadinService service) {
        /*
         * Must manually find the application property since service init
         * listeners are not Spring managed beans.
         *
         * See https://github.com/vaadin/spring/issues/213 and
         * https://github.com/vaadin/spring/pull/218.
         */
        WebApplicationContext applicationContext = WebApplicationContextUtils
                .getWebApplicationContext(((VaadinServletService) service)
                        .getServlet().getServletContext());

        Boolean loadTestMode = applicationContext.getEnvironment()
                .getProperty("loadtestmode.enabled", Boolean.class);

        return Boolean.TRUE.equals(loadTestMode);
    }

    private static ConnectorIdGenerator getGenerator(VaadinSession session) {
        ConnectorIdGenerator generator = session
                .getAttribute(ConnectorIdGenerator.class);
        if (generator == null) {
            generator = new ComponentIdBasedConnectorIdGenerator();
            session.setAttribute(ConnectorIdGenerator.class, generator);
        }
        return generator;
    }
}
