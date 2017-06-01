package com.vaadin.starter.bakery.app;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.server.VaadinServlet;
import com.vaadin.spring.server.SpringVaadinServlet;
import com.vaadin.starter.bakery.app.loadtest.LoadTestVaadinSpringServlet;

@Configuration
public class ApplicationConfiguration {

	/**
	 * Is load test mode enabled (false by default)
	 */
	@Value("${loadtestmode.enabled}")
	private Boolean loadTestModeEnabled;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public StringToIntegerConverter stringToIntegerConverter() {
		return new StringToIntegerConverter("Unable to parse integer");
	}

	@Bean
	VaadinServlet vaadinServlet() {
		if (loadTestModeEnabled) {
			// Use Customized Vaadin servlet for the load test mode
			return new LoadTestVaadinSpringServlet();
		} else {
			return new SpringVaadinServlet();
		}
	}
}
