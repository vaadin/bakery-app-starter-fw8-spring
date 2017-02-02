package com.vaadin.template.orders.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.vaadin.template.orders.backend.OrderRepository;
import com.vaadin.template.orders.backend.ProductRepository;
import com.vaadin.template.orders.backend.UserRepository;
import com.vaadin.template.orders.backend.data.Role;
import com.vaadin.template.orders.backend.data.entity.Order;
import com.vaadin.template.orders.backend.data.entity.Product;
import com.vaadin.template.orders.backend.data.entity.User;
import com.vaadin.template.orders.ui.OrdersUI;

@SpringBootApplication(scanBasePackageClasses = { OrdersUI.class })
@EnableJpaRepositories(basePackageClasses = { OrderRepository.class })
@EntityScan(basePackageClasses = { Order.class })
public class Application {

    private static final Logger log = LoggerFactory
            .getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CommandLineRunner loadData(OrderRepository orders,
            UserRepository users, ProductRepository products,
            PasswordEncoder passwordEncoder) {
        return (args) -> {

            users.save(new User("baker@vaadin.com", "Heidi",
                    passwordEncoder.encode("baker"), Role.BAKER));
            users.save(new User("barista@vaadin.com", "Malin",
                    passwordEncoder.encode("barista"), Role.BARISTA));
            users.save(new User("admin@vaadin.com", "Göran",
                    passwordEncoder.encode("admin"), Role.ADMIN));

            products.save(new Product("A big cake", 12.70));
            products.save(new Product("Strawberry cheese cake", 5.99));

            log.info("Initialized repositories");
        };
    }

}
