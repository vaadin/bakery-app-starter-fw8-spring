package com.vaadin.template.orders.ui.view.admin.user;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;

@SpringView
public class UserAdminView extends UserAdminViewDesign implements View {

    @Autowired
    private UserAdminPresenter presenter;

    @Override
    public void enter(ViewChangeEvent event) {
        presenter.enter();
    }
}