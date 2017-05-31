package com.vaadin.template.orders.ui.view.orders;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.template.orders.backend.data.entity.Order;
import com.vaadin.template.orders.ui.components.OrdersDataProvider;
import com.vaadin.template.orders.ui.navigation.NavigationManager;

@SpringComponent
@ViewScope
public class OrdersListPresenter implements Serializable {

	private OrdersListView view;

	private final NavigationManager navigationManager;

	private final OrdersDataProvider ordersDataProvider;

	@Autowired
	public OrdersListPresenter(NavigationManager navigationManager, OrdersDataProvider ordersDataProvider) {
		this.navigationManager = navigationManager;
		this.ordersDataProvider = ordersDataProvider;
	}

	void init(OrdersListView view) {
		this.view = view;
	}

	protected OrdersListView getView() {
		return view;
	}

	public OrdersDataProvider getOrdersProvider() {
		return ordersDataProvider;
	}

	public void selectedOrder(Order order) {
		navigationManager.navigateTo(OrderEditView.class, order.getId());
	}

	public void newOrder() {
		navigationManager.navigateTo(OrderEditView.class);
	}

	public void search(String searchTerm, Boolean includePast, boolean userOriginated) {
		ordersDataProvider.setFilter(searchTerm);
		ordersDataProvider.setIncludePast(includePast);
		if (userOriginated) {
			String past = "";
			if (includePast) {
				past = "&includePast=true";
			}
			navigationManager.updateViewParameter("search=" + searchTerm + past);
			System.out.println("user originater: " + String.valueOf(userOriginated));

		} else {
			view.updateFilters(searchTerm, includePast);
		}
	}

	public void urlChanged(String parameters) {
		Map<String, String> map = new HashMap<>();
		String[] parametersTokenized = parameters.split("&");
		for (int i = 0; i < parametersTokenized.length; i++) {
			String[] parameter = parametersTokenized[i].split("=");
			if (parameter.length > 1) {
				map.put(parameter[0], parameter[1]);
			} else {
				map.put(parameter[0], "");
			}
		}
		String searchTerm = map.get("search");
		boolean includePast = map.get("includePast") != null ? true : false;
		search(searchTerm, includePast, false);
	}
}
