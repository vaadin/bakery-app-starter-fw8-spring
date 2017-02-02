package com.vaadin.template.orders.ui.view;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.template.orders.backend.OrderRepository;

@SpringComponent
public class OrderInfoPresenter {

    private final OrderRepository repo;
    private OrderInfoView view;

    @Autowired
    public OrderInfoPresenter(OrderRepository repo) {
        this.repo = repo;
    }

    void init(OrderInfoView view) {
        this.view = view;
    }

    public void enter() {
        getView().list.setItems(repo.findAll());
    }

    protected OrderInfoView getView() {
        return view;
    }

}
