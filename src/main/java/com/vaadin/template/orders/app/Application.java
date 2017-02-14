package com.vaadin.template.orders.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.vaadin.template.orders.backend.OrderRepository;
import com.vaadin.template.orders.backend.data.entity.Order;
import com.vaadin.template.orders.backend.service.UserDetailsServiceImpl;
import com.vaadin.template.orders.ui.OrdersUI;

@SpringBootApplication(scanBasePackageClasses = { OrdersUI.class,
        Application.class, UserDetailsServiceImpl.class })
@EnableJpaRepositories(basePackageClasses = { OrderRepository.class })
@EntityScan(basePackageClasses = { Order.class })
public class Application extends SpringBootServletInitializer {

    public static final String APP_URL = "/";
    public static final String LOGIN_URL = "/login.html";
    public static final String LOGOUT_URL = "/login.html?logout";
    public static final String LOGIN_FAILURE_URL = "/login.html?error";
    public static final String LOGIN_PROCESSING_URL = "/login";

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

    @Override
    protected SpringApplicationBuilder configure(
            SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
