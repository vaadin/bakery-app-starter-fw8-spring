package com.vaadin.template.orders.ui.view.orders;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;

@SpringView
public class OrdersView extends OrdersViewDesign implements View {

    @Autowired
    private OrdersPresenter presenter;

    @PostConstruct
    public void init() {
        presenter.init(this);
        list.setDataProvider(presenter.getOrdersProvider());
    }

    @Override
    public void enter(ViewChangeEvent event) {
        presenter.enter();
    }

}
