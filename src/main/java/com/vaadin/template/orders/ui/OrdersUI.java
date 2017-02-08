package com.vaadin.template.orders.ui;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Viewport;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.template.orders.app.Application;
import com.vaadin.ui.UI;

@Theme("orderstheme")
@SpringUI(path = Application.APP_URL)
@Viewport("width=device-width, initial-scale=1.0")
public class OrdersUI extends UI {

    @Autowired
    private MainView mainView;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        setContent(mainView);
    }

}
