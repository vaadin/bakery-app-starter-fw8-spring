package com.vaadin.template.orders.ui;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.template.orders.ui.view.NavigableView;
import com.vaadin.template.orders.ui.view.admin.product.ProductAdminView;
import com.vaadin.template.orders.ui.view.admin.user.UserAdminView;
import com.vaadin.template.orders.ui.view.dashboard.DashboardView;
import com.vaadin.template.orders.ui.view.storefront.StorefrontView;
import com.vaadin.ui.Button;

@SpringViewDisplay
@UIScope
public class MainView extends MainViewDesign implements ViewDisplay {

	private final MainPresenter presenter;
	private final Map<Class<? extends View>, Button> navigationButtons = new HashMap<>();

	@Autowired
	public MainView(MainPresenter presenter) {
		this.presenter = presenter;
	}

	@PostConstruct
	public void init() {
		content.addStyleName("v-scrollable");
		presenter.init(this);

		// Hide until it works
		products.setVisible(false);
	}

	public void populateMenu() {
		attachNavigation(storefront, StorefrontView.class);
		attachNavigation(dashboard, DashboardView.class);
		attachNavigation(users, UserAdminView.class);
		attachNavigation(products, ProductAdminView.class);
		logout.addClickListener(e -> presenter.logout());
	}

	private void attachNavigation(Button navigationButton, Class<? extends View> targetView) {
		navigationButtons.put(targetView, navigationButton);
		navigationButton.setVisible(presenter.hasAccess(targetView));
		navigationButton.addClickListener(e -> presenter.navigateTo(targetView));

	}

	@Override
	public void showView(View view) {
		if (!(view instanceof NavigableView)) {
			throw new IllegalArgumentException("Only OrdersView implementations are supported");
		}
		content.removeAllComponents();
		content.addComponent(((NavigableView) view).getViewComponent());

		navigationButtons.forEach((viewClass, button) -> button.setStyleName("selected", viewClass == view.getClass()));
		Button menuItem = navigationButtons.get(view.getClass());
		String viewName = "";
		if (menuItem != null) {
			viewName = menuItem.getCaption();
		}
		activeViewName.setValue(viewName);
	}

}
