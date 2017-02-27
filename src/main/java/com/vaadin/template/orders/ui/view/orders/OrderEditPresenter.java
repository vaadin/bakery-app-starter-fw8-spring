package com.vaadin.template.orders.ui.view.orders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;

import com.vaadin.data.HasValue;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.template.orders.backend.CustomerRepository;
import com.vaadin.template.orders.backend.OrderRepository;
import com.vaadin.template.orders.backend.PickupLocationRepository;
import com.vaadin.template.orders.backend.data.OrderState;
import com.vaadin.template.orders.backend.data.entity.Customer;
import com.vaadin.template.orders.backend.data.entity.HistoryItem;
import com.vaadin.template.orders.backend.data.entity.Order;
import com.vaadin.template.orders.backend.data.entity.PickupLocation;
import com.vaadin.template.orders.backend.service.OrderService;
import com.vaadin.template.orders.backend.service.UserService;
import com.vaadin.template.orders.ui.OrdersUI;
import com.vaadin.template.orders.ui.PrototypeScope;
import com.vaadin.ui.Component.Focusable;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

@SpringComponent
@PrototypeScope
public class OrderEditPresenter {

    private final OrderRepository orderRepository;
    private OrderEditView view;
    private final PickupLocationRepository pickupLocationRepository;
    private final CustomerRepository customerRepository;

    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;

    private static final List<OrderState> happyPath = Arrays.asList(
            OrderState.NEW, OrderState.CONFIRMED, OrderState.READY_FOR_PICKUP,
            OrderState.DELIVERED);

    @Autowired
    public OrderEditPresenter(OrderRepository orderRepository,
            CustomerRepository customerRepository,
            PickupLocationRepository pickupLocationRepository,
            EventBus.ViewEventBus eventBus) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.pickupLocationRepository = pickupLocationRepository;

        eventBus.subscribe(this);
    }

    void init(OrderEditView view) {
        this.view = view;
    }

    /**
     * Called when the user enters the view.
     */
    public void enterView(Long id) {
        Order order;
        if (id == null) {
            // New
            order = new Order();
            order.setState(OrderState.NEW);
            order.setItems(new ArrayList<>());
        } else {
            order = orderRepository.findOne(id);
            if (order == null) {
                view.showNotFound();
                return;
            }
        }

        view.setOrder(order);
        updateTotalSum();
        if (id == null) {
            view.setMode(Mode.EDIT);
        } else {
            view.setMode(Mode.REPORT);
        }
    }

    @EventBusListenerMethod
    private void updateTotalSum(ProductInfoChange change) {
        updateTotalSum();
    }

    @EventBusListenerMethod
    private void orderUpdated(OrderUpdated event) {
        view.setOrder(orderRepository.findOne(view.getOrder().getId()));
    }

    private void updateTotalSum() {
        double sum = view.getOrder().getItems().stream()
                .filter(item -> item.getProduct() != null)
                .collect(Collectors
                        .summingDouble(item -> item.getProduct().getPrice()
                                * item.getQuantity()));
        view.setSum(sum);
    }

    public List<PickupLocation> getPickupLocations() {
        return pickupLocationRepository.findAll();
    }

    public void editBackCancelPressed() {
        if (view.getMode() == Mode.REPORT) {
            // Edit order
            view.setMode(Mode.EDIT);
        } else if (view.getMode() == Mode.CONFIRMATION) {
            // Back to edit
            view.setMode(Mode.EDIT);
        } else if (view.getMode() == Mode.EDIT) {
            // Cancel edit
            Long id = view.getOrder().getId();
            if (id == null) {
                ((OrdersUI) view.getUI()).navigateTo(OrdersListView.class);
            } else {
                enterView(id);
            }
        }
    }

    public void okPressed() {
        if (view.getMode() == Mode.REPORT) {
            // Set next state
            Order order = view.getOrder();
            Optional<OrderState> nextState = getNextHappyPathState(
                    order.getState());
            if (!nextState.isPresent()) {
                throw new IllegalStateException(
                        "The next state button should never be enabled when the state does not follow the happy path");
            }
            orderService.changeState(order, nextState.get());
            reload(order.getId());
        } else if (view.getMode() == Mode.CONFIRMATION) {
            saveOrder();
        } else if (view.getMode() == Mode.EDIT) {
            Optional<HasValue<?>> firstErrorField = view.validate().findFirst();
            if (firstErrorField.isPresent()) {
                ((Focusable) firstErrorField.get()).focus();
                return;
            }
            // New order should still show a confirmation page
            Order order = view.getOrder();
            if (order.getId() == null) {
                view.setMode(Mode.CONFIRMATION);
            } else {
                saveOrder();
            }
        }
    }

    private void reload(Long id) {
        ((OrdersUI) view.getUI()).navigateTo(OrderEditView.class, id);
    }

    private void saveOrder() {
        try {
            // FIXME service, transaction, cascade, ...
            Order order = view.getOrder();
            // FIXME Use existing customer maybe
            Customer customer = customerRepository.save(order.getCustomer());
            order.setCustomer(customer);

            if (order.getHistory() == null) {
                String comment = "Order placed";
                order.setHistory(new ArrayList<>());
                HistoryItem item = new HistoryItem(userService.getCurrentUser(),
                        comment);
                item.setNewState(OrderState.NEW);
                order.getHistory().add(item);
            }
            order = orderRepository.save(order);

            // Navigate to edit view so URL is updated correctly
            ((OrdersUI) view.getUI()).navigateTo(OrderEditView.class,
                    order.getId());
        } catch (ValidationException e) {
            // Should not get here if validation is setup properly
            Notification.show("Please check the contents of the fields.",
                    Type.ERROR_MESSAGE);
            getLogger().log(Level.FINEST, "Validation error during order save",
                    e);
            return;
        } catch (Exception e) {
            Notification.show(
                    "Somebody else might have updated the data. Please refresh and try again.",
                    Type.ERROR_MESSAGE);
            getLogger().log(Level.WARNING, "Unable to save order", e);
            return;
        }
    }

    private static Logger getLogger() {
        return Logger.getLogger(OrderEditPresenter.class.getName());
    }

    public Optional<OrderState> getNextHappyPathState(OrderState current) {
        final int currentIndex = happyPath.indexOf(current);
        if (currentIndex == -1 || currentIndex == happyPath.size() - 1) {
            return Optional.empty();
        }
        return Optional.of(happyPath.get(currentIndex + 1));
    }
}
