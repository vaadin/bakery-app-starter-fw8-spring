package com.vaadin.template.orders.ui.view;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;

@SpringView
public class OrderInfoView extends OrderInfoViewDesign implements View {

    @Autowired
    private OrderInfoPresenter presenter;

    @PostConstruct
    public void initPresenter() {
        presenter.init(this);
    }

    @Override
    public void enter(ViewChangeEvent event) {
        presenter.enter();
    }

}
