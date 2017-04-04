package com.vaadin.template.orders.ui.view.orders;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.template.orders.backend.data.OrderState;
import com.vaadin.template.orders.backend.data.entity.Order;
import com.vaadin.template.orders.ui.PrototypeScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@SpringComponent
@PrototypeScope
public class OrderStateWindow extends Window {

    @Autowired
    private OrderStateWindowController controller;

    private final VerticalLayout layout = new VerticalLayout();

    private Order order;

    @PostConstruct
    public void init() {
        controller.init(this);
        setContent(layout);
        setModal(true);
        setResizable(false);
    }

    public void setOrder(Order order) {
        this.order = order;
        layout.removeAllComponents();
        for (OrderState state : OrderState.values()) {
            Button button = new Button(state.getDisplayName());
            button.setId(state.name());
            button.addClickListener(e -> controller.setState(order, state));
            layout.addComponent(button);

            button.setEnabled(state != order.getState());
        }

    }
}
