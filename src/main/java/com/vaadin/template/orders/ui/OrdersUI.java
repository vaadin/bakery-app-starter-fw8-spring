package com.vaadin.template.orders.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.ui.UI;

@Theme("orderstheme")
@SpringUI
@SpringViewDisplay
public class OrdersUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        // Nothing to do here right now, the navigator is set up automatically
    }

}
