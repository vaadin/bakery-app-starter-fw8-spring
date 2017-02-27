package com.vaadin.template.orders.ui.view.orders;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.template.orders.backend.OrderRepository;
import com.vaadin.template.orders.backend.data.entity.HistoryItem;
import com.vaadin.template.orders.backend.data.entity.Order;
import com.vaadin.template.orders.backend.service.UserService;
import com.vaadin.template.orders.ui.PrototypeScope;

@SpringComponent
@PrototypeScope
public class OrderHistoryController {

    private OrderHistory view;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private EventBus.ViewEventBus eventBus;

    public void init(OrderHistory view) {
        this.view = view;
    }

    public void addNewComment(String comment) {
        addHistoryItem(view.getOrder(), comment);
        eventBus.publish(this, new OrderUpdated());
    }

    // TODO Move to service
    public Order addHistoryItem(Order order, String comment) {
        HistoryItem item = new HistoryItem(userService.getCurrentUser(),
                comment);
        if (order.getHistory() == null) {
            order.setHistory(new ArrayList<>());
        }
        order.getHistory().add(item);

        return orderRepository.save(order);

    }

}
