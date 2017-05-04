package com.vaadin.template.orders.ui;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.template.orders.ui.view.OrdersView;
import com.vaadin.template.orders.ui.view.admin.product.ProductAdminView;
import com.vaadin.template.orders.ui.view.admin.user.UserAdminView;
import com.vaadin.template.orders.ui.view.dashboard.DashboardView;
import com.vaadin.template.orders.ui.view.orders.OrdersListView;
import com.vaadin.ui.Button;

@SpringViewDisplay
@UIScope
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

	public void populateMenu() {
		addMenuItem(storefront, OrdersListView.class);
		addMenuItem(dashboard, DashboardView.class);
		addMenuItem(users, UserAdminView.class);
		addMenuItem(products, ProductAdminView.class);
		logout.addClickListener(e -> presenter.logout());
	}

	private void addMenuItem(Button navigationButton, Class<? extends View> targetView) {
		navigationButton.setVisible(presenter.hasAccess(targetView));
		navigationButton.addClickListener(e -> presenter.navigateTo(targetView));

	}

	@Override
	public void showView(View view) {
		if (!(view instanceof OrdersView)) {
			throw new IllegalArgumentException("Only OrdersView implementations are supported");
		}
		content.removeAllComponents();
		content.addComponent(((OrdersView) view).getViewComponent());
	}

}
