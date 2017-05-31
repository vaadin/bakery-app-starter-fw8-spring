package com.vaadin.template.orders.ui.view.orders;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

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

	public void search(String searchTerm, boolean includePast, boolean userOriginated) {
		ordersDataProvider.setFilter(searchTerm);
		ordersDataProvider.setIncludePast(includePast);
		if (userOriginated) {
			String past = "";
			if (includePast) {
				past = "&includePast=true";
			}
			navigationManager.updateViewParameter("search=" + searchTerm + past);
		} else {
			view.updateFilters(searchTerm, includePast);
		}
	}

	public void urlChanged(String parameters) {
		MultiValueMap<String, String> params = UriComponentsBuilder.fromPath(parameters).build().getQueryParams();
		String searchTerm = params.getFirst("search");
		boolean includePast = params.containsKey("includePast");
		search(searchTerm, includePast, false);
	}
}
