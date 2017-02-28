package com.vaadin.template.orders.backend.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vaadin.template.orders.backend.OrderRepository;
import com.vaadin.template.orders.backend.data.OrderState;
import com.vaadin.template.orders.backend.data.entity.HistoryItem;
import com.vaadin.template.orders.backend.data.entity.Order;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserService userService;

    public Order changeState(Order order, OrderState state) {
        if (order.getState() == state) {
            throw new IllegalArgumentException(
                    "Order state is already " + state);
        }
        order.setState(state);
        addHistoryItem(order, state);

        return orderRepository.save(order);
    }

    private void addHistoryItem(Order order, OrderState newState) {
        String comment = "Order " + newState.getDisplayName();

        HistoryItem item = new HistoryItem(userService.getCurrentUser(),
                comment);
        item.setNewState(newState);
        if (order.getHistory() == null) {
            order.setHistory(new ArrayList<>());
        }
        order.getHistory().add(item);
    }

}
