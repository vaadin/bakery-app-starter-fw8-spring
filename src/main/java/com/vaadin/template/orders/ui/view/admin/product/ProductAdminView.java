package com.vaadin.template.orders.ui.view.admin.product;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;

@SpringView
public class ProductAdminView extends ProductAdminViewDesign implements View {
    @Autowired
    private ProductAdminPresenter presenter;

    @Override
    public void enter(ViewChangeEvent event) {
        presenter.enter();
    }

}
