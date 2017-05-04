package com.vaadin.template.orders.ui.view.orders;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.template.orders.backend.data.entity.Order;
import com.vaadin.template.orders.ui.OrdersUI;
import com.vaadin.template.orders.ui.PrototypeScope;
import com.vaadin.template.orders.ui.components.OrdersDataProvider;

@SpringComponent
@PrototypeScope
public class OrdersListPresenter {

	private OrdersListView view;

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
		((OrdersUI) view.getUI()).navigateTo(OrderEditView.class, order.getId());
	}

	public void newOrder() {
		((OrdersUI) view.getUI()).navigateTo(OrderEditView.class);
	}

}
