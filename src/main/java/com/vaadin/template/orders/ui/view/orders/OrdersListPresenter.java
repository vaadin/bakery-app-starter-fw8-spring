package com.vaadin.template.orders.ui.view.orders;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.template.orders.backend.data.entity.Order;
import com.vaadin.template.orders.ui.NavigationManager;
import com.vaadin.template.orders.ui.components.OrdersDataProvider;

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
}
