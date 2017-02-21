package com.vaadin.template.orders.ui.view.orders;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.template.orders.backend.OrderRepository;
import com.vaadin.template.orders.backend.data.entity.Order;
import com.vaadin.template.orders.ui.OrdersUI;
import com.vaadin.template.orders.ui.PrototypeScope;
import com.vaadin.template.orders.ui.components.OrdersDataProvider;

@SpringComponent
@PrototypeScope
public class OrdersListPresenter {

    private final OrderRepository orderRepository;
    private OrdersListView view;

    @Autowired
    private OrdersDataProvider ordersDataProvider;

    @Autowired
    public OrdersListPresenter(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    void init(OrdersListView view) {
        this.view = view;
    }

    /**
     * Called when the user enters the view.
     */
    public void enter() {
        // Placeholder for the future
    }

    protected OrdersListView getView() {
        return view;
    }

    public OrdersDataProvider getOrdersProvider() {
        return ordersDataProvider;
    }

    public void selectedOrder(Order order) {
        String viewId = OrdersUI.getViewId(OrderView.class);
        view.getUI().getNavigator().navigateTo(viewId + "/" + order.getId());
    }

    public void newOrder() {
        String viewId = OrdersUI.getViewId(OrderView.class);
        view.getUI().getNavigator().navigateTo(viewId);
    }

}
