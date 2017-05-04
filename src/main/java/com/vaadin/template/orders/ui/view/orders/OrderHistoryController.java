package com.vaadin.template.orders.ui.view.orders;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.template.orders.backend.service.OrderService;
import com.vaadin.template.orders.ui.PrototypeScope;

@SpringComponent
@PrototypeScope
public class OrderHistoryController {

	private OrderHistory view;

	@Autowired
	private OrderService orderService;

	@Autowired
	private EventBus.ViewEventBus eventBus;

	public void init(OrderHistory view) {
		this.view = view;
	}

	public void addNewComment(String comment) {
		orderService.addHistoryItem(view.getOrder(), comment);
		eventBus.publish(this, new OrderUpdated());
	}

}
