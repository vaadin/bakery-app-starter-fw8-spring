package com.vaadin.template.orders.ui;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.template.orders.ui.view.admin.product.ProductAdminView;
import com.vaadin.template.orders.ui.view.admin.user.UserAdminView;
import com.vaadin.template.orders.ui.view.dashboard.DashboardView;
import com.vaadin.template.orders.ui.view.orders.OrdersView;
import com.vaadin.ui.Component;

@SpringViewDisplay
public class MainView extends MainViewDesign implements ViewDisplay {

    @Autowired
    private MainPresenter presenter;

    @PostConstruct
    public void init() {
        content.addStyleName("v-scrollable");
        presenter.init(this);

        // Hide until it works
        products.setVisible(false);
    }

    @PostConstruct
    public void populateMenu() {
        storefront
                .addClickListener(e -> presenter.navigateTo(OrdersView.class));
        dashboard.addClickListener(
                e -> presenter.navigateTo(DashboardView.class));
        users.addClickListener(e -> presenter.navigateTo(UserAdminView.class));
        products.addClickListener(
                e -> presenter.navigateTo(ProductAdminView.class));
        logout.addClickListener(e -> presenter.logout());
    }

    @Override
    public void showView(View view) {
        if (view instanceof Component) {
            content.removeAllComponents();
            content.addComponent((Component) view);
        } else {
            throw new IllegalArgumentException(
                    "View is not a component: " + view);
        }
    }

}
