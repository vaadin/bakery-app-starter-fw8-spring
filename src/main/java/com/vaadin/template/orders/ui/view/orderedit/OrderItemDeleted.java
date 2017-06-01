package com.vaadin.template.orders.ui.view.orderedit;

import com.vaadin.template.orders.backend.data.entity.OrderItem;

public class OrderItemDeleted {

	private OrderItem orderItem;

	public OrderItemDeleted(OrderItem orderItem) {
		this.orderItem = orderItem;
	}

	public OrderItem getOrderItem() {
		return orderItem;
	}
}
