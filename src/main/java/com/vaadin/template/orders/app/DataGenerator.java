package com.vaadin.template.orders.app;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.template.orders.backend.CustomerRepository;
import com.vaadin.template.orders.backend.OrderRepository;
import com.vaadin.template.orders.backend.PickupLocationRepository;
import com.vaadin.template.orders.backend.ProductRepository;
import com.vaadin.template.orders.backend.UserRepository;
import com.vaadin.template.orders.backend.data.OrderState;
import com.vaadin.template.orders.backend.data.Role;
import com.vaadin.template.orders.backend.data.entity.Customer;
import com.vaadin.template.orders.backend.data.entity.Order;
import com.vaadin.template.orders.backend.data.entity.OrderItem;
import com.vaadin.template.orders.backend.data.entity.PickupLocation;
import com.vaadin.template.orders.backend.data.entity.Product;
import com.vaadin.template.orders.backend.data.entity.User;

@SpringComponent
public class DataGenerator {

    private static final String[] FILLING = new String[] { "Strawberry",
            "Chocolate", "Blueberry", "Raspberry", "Vanilla", "Salami",
            "Bacon" };
    private static final String[] TYPE = new String[] { "Cake", "Pastry",
            "Tart", "Muffin", "Biscuit", "Bread", "Bagel", "Bun", "Brownie",
            "Cookie", "Cracker", "Cheese Cake" };
    private static final String[] FIRST_NAME = new String[] { "Ori", "Amanda",
            "Octavia", "Laurel", "Lael", "Delilah", "Jason", "Skyler",
            "Arsenio", "Haley", "Lionel", "Sylvia", "Jessica", "Lester",
            "Ferdinand", "Elaine", "Griffin", "Kerry", "Dominique" };
    private static final String[] LAST_NAME = new String[] { "Carter", "Castro",
            "Rich", "Irwin", "Moore", "Hendricks", "Huber", "Patton",
            "Wilkinson", "Thornton", "Nunez", "Macias", "Gallegos", "Blevins",
            "Mejia", "Pickett", "Whitney", "Farmer", "Henry", "Chen", "Macias",
            "Rowland", "Pierce", "Cortez", "Noble", "Howard", "Nixon",
            "Mcbride", "Leblanc", "Russell", "Carver", "Benton", "Maldonado",
            "Lyons" };

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final Random random = new Random(1L);

    private final List<PickupLocation> pickupLocations = new ArrayList<>();
    private final List<Product> products = new ArrayList<>();
    private final List<Customer> customers = new ArrayList<>();
    private final List<Order> orders = new ArrayList<>();

    @Bean
    public CommandLineRunner loadData(OrderRepository orders,
            UserRepository users, ProductRepository products,
            CustomerRepository customers,
            PickupLocationRepository pickupLocations,
            PasswordEncoder passwordEncoder) {
        return args -> {
            createUsers(users);
            createProducts(products);
            createCustomers(customers);
            createPickupLocations(pickupLocations);
            createOrders(orders);

            getLogger().info("Initialized repositories");
        };
    }

    private void createCustomers(CustomerRepository customerRepo) {
        for (int i = 0; i < 100; i++) {
            customers.add(createCustomer(customerRepo));
        }
    }

    private Customer createCustomer(CustomerRepository customerRepo) {
        Customer customer = new Customer();
        customer.setFirstName(getRandom(FIRST_NAME));
        customer.setLastName(getRandom(LAST_NAME));
        customer.setPhoneNumber(getRandomPhone());
        return customerRepo.save(customer);
    }

    private String getRandomPhone() {
        return "+1-555-" + String.format("%04d", random.nextInt(10000));
    }

    private void createOrders(OrderRepository orderRepo) {
        for (int i = 0; i < 1000; i++) {
            orders.add(createOrder(orderRepo));
        }
    }

    private Order createOrder(OrderRepository orderRepo) {
        Order order = new Order();

        order.setCustomer(getRandomCustomer());
        order.setPickupLocation(getRandomPickupLocation());
        order.setDue(getRandomDueDate());
        order.setState(getRandomState(order.getDue()));

        int itemCount = random.nextInt(3);
        List<OrderItem> items = new ArrayList<>();
        for (int i = 0; i <= itemCount; i++) {
            OrderItem item = new OrderItem();
            item.setProduct(getRandomProduct());
            item.setQuantity(random.nextInt(10) + 1);
            items.add(item);
        }
        order.setItems(items);
        return orderRepo.save(order);
    }

    private LocalDateTime getRandomDueDate() {
        LocalDate now = LocalDate.now();
        // Roughly one month into the future
        // Roughly six months into the past
        int dateOffset = random.nextInt(7 * 30) - 30;
        int time = 8 + 4 * random.nextInt(3);

        return now.minusDays(dateOffset).atTime(time, 0);
    }

    private OrderState getRandomState(LocalDateTime due) {
        LocalDateTime today = LocalDate.now().atStartOfDay();
        LocalDateTime tomorrow = today.plusDays(1);
        LocalDateTime twoDays = today.plusDays(2);

        if (due.isBefore(today)) {
            if (random.nextDouble() < 0.9) {
                return OrderState.DELIVERED;
            } else {
                return OrderState.CANCELLED;
            }
        } else {
            if (due.isAfter(twoDays)) {
                return OrderState.NEW;
            } else if (due.isAfter(tomorrow)) {
                // in 1-2 days
                double resolution = random.nextDouble();
                if (resolution < 0.8) {
                    return OrderState.NEW;
                } else if (resolution < 0.9) {
                    return OrderState.PROBLEM;
                } else {
                    return OrderState.CANCELLED;
                }
            } else {
                double resolution = random.nextDouble();
                if (resolution < 0.6) {
                    return OrderState.READY_FOR_PICKUP;
                } else if (resolution < 0.8) {
                    return OrderState.DELIVERED;
                } else if (resolution < 0.9) {
                    return OrderState.PROBLEM;
                } else {
                    return OrderState.CANCELLED;
                }
            }

        }
    }

    private Product getRandomProduct() {
        return getRandom(products);
    }

    private PickupLocation getRandomPickupLocation() {
        return getRandom(pickupLocations);
    }

    private Customer getRandomCustomer() {
        return getRandom(customers);
    }

    private <T> T getRandom(List<T> items) {
        return items.get(random.nextInt(items.size()));
    }

    private <T> T getRandom(T[] array) {
        return array[random.nextInt(array.length)];
    }

    private void createPickupLocations(PickupLocationRepository pickupRepo) {
        PickupLocation store = new PickupLocation();
        store.setName("Store");
        pickupLocations.add(pickupRepo.save(store));
        PickupLocation bakery = new PickupLocation();
        store.setName("Bakery");
        pickupLocations.add(pickupRepo.save(bakery));
    }

    private void createProducts(ProductRepository productsRepo) {
        for (int i = 0; i < 100; i++) {
            Product product = new Product();
            product.setName(getRandomProductName());
            product.setPrice(2.0 + random.nextDouble() * 100.0);
            products.add(productsRepo.save(product));
        }
    }

    private String getRandomProductName() {
        String name = getRandom(FILLING);
        if (random.nextBoolean()) {
            name += " " + getRandom(FILLING);
        }
        name += " " + getRandom(TYPE);

        return name;
    }

    private void createUsers(UserRepository users) {
        users.save(new User("baker@vaadin.com", "Heidi",
                passwordEncoder.encode("baker"), Role.BAKER));
        users.save(new User("barista@vaadin.com", "Malin",
                passwordEncoder.encode("barista"), Role.BARISTA));
        users.save(new User("admin@vaadin.com", "Göran",
                passwordEncoder.encode("admin"), Role.ADMIN));
    }

    private static Logger getLogger() {
        return LoggerFactory.getLogger(DataGenerator.class);
    }

}
