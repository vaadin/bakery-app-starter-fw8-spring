package com.vaadin.template.orders.ui.view.orders;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.template.orders.backend.OrderRepository;
import com.vaadin.template.orders.ui.PrototypeScope;
import com.vaadin.template.orders.ui.components.OrdersDataProvider;

@SpringComponent
@PrototypeScope
public class OrdersPresenter {

    private final OrderRepository orderRepository;
    private OrdersView view;

    @Autowired
    private OrdersDataProvider ordersDataProvider;

    @Autowired
    public OrdersPresenter(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    void init(OrdersView view) {
        this.view = view;
    }

    /**
     * Called when the user enters the view.
     */
    public void enter() {
        // Placeholder for the future
    }

    protected OrdersView getView() {
        return view;
    }

    public OrdersDataProvider getOrdersProvider() {
        return ordersDataProvider;
    }

}
