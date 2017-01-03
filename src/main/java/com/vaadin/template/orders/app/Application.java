package com.vaadin.template.orders.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.vaadin.template.orders.backend.data.OrderRepository;
import com.vaadin.template.orders.ui.OrdersUI;

@SpringBootApplication(scanBasePackageClasses = { OrdersUI.class })
@EnableJpaRepositories(basePackageClasses = { OrderInfo.class, OrderRepository.class })
public class Application {

	private static final Logger log = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		SpringApplication.run(Application.class);
	}

	@Bean
	public CommandLineRunner loadData(OrderRepository repository) {
		return (args) -> {
			// save a couple of customers
			repository.save(new OrderInfo("First order"));
			repository.save(new OrderInfo("A big cake"));
			repository.save(new OrderInfo("A big lie"));

			log.info("Initialized repository with " + repository.count() + " orders");
		};
	}

}
