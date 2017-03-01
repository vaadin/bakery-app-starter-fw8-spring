package com.vaadin.template.orders.ui.view.admin.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.template.orders.backend.data.Role;

@SpringView
@Secured(Role.ADMIN)
public class ProductAdminView extends ProductAdminViewDesign implements View {

    @Autowired
    private ProductAdminPresenter presenter;

    @Override
    public void enter(ViewChangeEvent event) {
        presenter.enter();
    }

}
