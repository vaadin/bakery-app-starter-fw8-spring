package com.vaadin.template.orders.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.vaadin.spring.events.annotation.EnableEventBus;

import com.vaadin.template.orders.app.security.SecurityConfig;
import com.vaadin.template.orders.backend.OrderRepository;
import com.vaadin.template.orders.backend.data.entity.Order;
import com.vaadin.template.orders.backend.service.UserService;
import com.vaadin.template.orders.ui.OrdersUI;
import com.vaadin.template.orders.ui.eventbus.ViewEventBusImpl;

@SpringBootApplication(scanBasePackageClasses = { OrdersUI.class, Application.class, UserService.class,
		SecurityConfig.class, ViewEventBusImpl.class })
@EnableJpaRepositories(basePackageClasses = { OrderRepository.class })
@EntityScan(basePackageClasses = { Order.class })
@EnableEventBus
public class Application extends SpringBootServletInitializer {

	public static final String APP_URL = "/";
	public static final String LOGIN_URL = "/login.html";
	public static final String LOGOUT_URL = "/login.html?logout";
	public static final String LOGIN_FAILURE_URL = "/login.html?error";
	public static final String LOGIN_PROCESSING_URL = "/login";

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}
}
