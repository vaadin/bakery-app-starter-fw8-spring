package com.vaadin.template.orders.app;

import java.time.LocalDateTime;
import java.util.Arrays;

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

import com.vaadin.template.orders.backend.CustomerRepository;
import com.vaadin.template.orders.backend.OrderRepository;
import com.vaadin.template.orders.backend.PickupLocationRepository;
import com.vaadin.template.orders.backend.ProductRepository;
import com.vaadin.template.orders.backend.UserRepository;
import com.vaadin.template.orders.backend.data.Role;
import com.vaadin.template.orders.backend.data.entity.Customer;
import com.vaadin.template.orders.backend.data.entity.Order;
import com.vaadin.template.orders.backend.data.entity.OrderItem;
import com.vaadin.template.orders.backend.data.entity.PickupLocation;
import com.vaadin.template.orders.backend.data.entity.Product;
import com.vaadin.template.orders.backend.data.entity.User;
import com.vaadin.template.orders.backend.service.UserDetailsServiceImpl;
import com.vaadin.template.orders.ui.OrdersUI;

@SpringBootApplication(scanBasePackageClasses = { OrdersUI.class,
        Application.class, UserDetailsServiceImpl.class })
@EnableJpaRepositories(basePackageClasses = { OrderRepository.class })
@EntityScan(basePackageClasses = { Order.class })
public class Application {

    public static final String APP_URL = "/app/";

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
            CustomerRepository customers,
            PickupLocationRepository pickupLocations,
            PasswordEncoder passwordEncoder) {
        return args -> {

            users.save(new User("baker@vaadin.com", "Heidi",
                    passwordEncoder.encode("baker"), Role.BAKER));
            users.save(new User("barista@vaadin.com", "Malin",
                    passwordEncoder.encode("barista"), Role.BARISTA));
            users.save(new User("admin@vaadin.com", "Göran",
                    passwordEncoder.encode("admin"), Role.ADMIN));

            Product bigCake = new Product("A big cake", 12.70);
            products.save(bigCake);
            Product strawberryCheeseCake = new Product("Strawberry cheese cake",
                    5.99);
            products.save(strawberryCheeseCake);

            Customer cookieMonster = new Customer("Cookie", "Monster",
                    "+1 555 0728");
            customers.save(cookieMonster);

            PickupLocation store = new PickupLocation();
            store.setName("Store");
            pickupLocations.save(store);

            Order order = new Order();
            order.setDue(LocalDateTime.of(2016, 5, 5, 14, 0));
            order.setCustomer(cookieMonster);
            order.setPickupLocation(store);
            OrderItem item1 = new OrderItem(bigCake, 2);
            OrderItem item2 = new OrderItem(strawberryCheeseCake, 1);
            order.setItems(Arrays.asList(item1, item2));
            orders.save(order);
            getLogger().info("Initialized repositories");
        };
    }

    private static Logger getLogger() {
        return LoggerFactory.getLogger(Application.class);
    }

}
