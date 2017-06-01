package com.vaadin.starter.bakery.ui.view.orderedit;

import com.vaadin.starter.bakery.backend.data.entity.OrderItem;

public class OrderItemDeleted {

	private OrderItem orderItem;

	public OrderItemDeleted(OrderItem orderItem) {
		this.orderItem = orderItem;
	}

	public OrderItem getOrderItem() {
		return orderItem;
	}
}
