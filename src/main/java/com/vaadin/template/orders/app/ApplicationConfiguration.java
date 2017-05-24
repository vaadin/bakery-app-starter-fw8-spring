package com.vaadin.template.orders.app;

import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.server.DeploymentConfiguration;
import com.vaadin.server.ServiceException;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinServletService;
import com.vaadin.spring.server.SpringVaadinServlet;
import com.vaadin.template.orders.app.loadtest.LoadTestServletService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;

@Configuration
public class ApplicationConfiguration {

    /**
     * Load test mode enabled
     */
    @Value("${orders.loadtestmode.enabled}")
    private Boolean loadTestModeEnabled;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public StringToIntegerConverter stringToIntegerConverter() {
		return new StringToIntegerConverter("Unable to parse integer");
	}

    /**
     * Customized Vaadin servlet (for the load test mode).
     */
    @Bean
    VaadinServlet vaadinServlet() {
        VaadinServlet servlet = new SpringVaadinServlet() {
            @Override
            protected void servletInitialized() throws ServletException {
                super.servletInitialized();
            }

            @Override
            protected VaadinServletService createServletService(
                    DeploymentConfiguration deploymentConfiguration)
                    throws ServiceException {
                if (loadTestModeEnabled) {

                    Logger.getLogger(ApplicationConfiguration.class.getName())
                            .log(Level.WARNING,
                                    "Running on Load Test mode, do not use this in production");

                    LoadTestServletService service = new LoadTestServletService(
                            this, deploymentConfiguration,
                            this.getServiceUrlPath());
                    service.init();
                    return service;
                } else {
                    return super.createServletService(deploymentConfiguration);
                }
            }
        };
        return servlet;
    }
}
