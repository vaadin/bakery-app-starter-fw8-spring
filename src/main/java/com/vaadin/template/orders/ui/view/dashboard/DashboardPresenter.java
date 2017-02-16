package com.vaadin.template.orders.ui.view.dashboard;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.template.orders.ui.PrototypeScope;
import com.vaadin.template.orders.ui.components.OrdersDataProvider;

@SpringComponent
@PrototypeScope
public class DashboardPresenter {

    @Autowired
    private OrdersDataProvider ordersDataProvider;

    private DashboardView view;

    void init(DashboardView view) {
        this.view = view;
    }

    protected DashboardView getView() {
        return view;
    }

    /**
     * Called when the user enters the view.
     */
    public void enter() {
        // Placeholder for the future
    }

    public OrdersDataProvider getOrdersProvider() {
        return ordersDataProvider;
    }
}
