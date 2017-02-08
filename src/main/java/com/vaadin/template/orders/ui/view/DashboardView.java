package com.vaadin.template.orders.ui.view;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;

@SpringView(name = "")
public class DashboardView extends DashboardViewDesign implements View {

    @Autowired
    private DashboardPresenter presenter;

    @Override
    public void enter(ViewChangeEvent event) {
        presenter.enter();
    }

}
