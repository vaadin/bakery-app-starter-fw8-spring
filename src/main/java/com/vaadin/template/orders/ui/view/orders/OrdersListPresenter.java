package com.vaadin.template.orders.ui.view.orders;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.template.orders.backend.data.entity.Order;
import com.vaadin.template.orders.ui.NavigationManager;
import com.vaadin.template.orders.ui.PrototypeScope;
import com.vaadin.template.orders.ui.components.OrdersDataProvider;

@SpringComponent
@PrototypeScope
public class OrdersListPresenter {

	private OrdersListView view;

	@Autowired
	private NavigationManager navigationManager;

	@Autowired
	private OrdersDataProvider ordersDataProvider;

	public OrdersListPresenter() {
		// Nothing to do here
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
