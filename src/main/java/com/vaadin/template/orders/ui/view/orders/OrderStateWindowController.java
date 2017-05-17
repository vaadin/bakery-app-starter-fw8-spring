package com.vaadin.template.orders.ui.view.orders;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.template.orders.app.BeanLocator;
import com.vaadin.template.orders.backend.data.OrderState;
import com.vaadin.template.orders.backend.data.entity.Order;
import com.vaadin.template.orders.backend.service.OrderService;
import com.vaadin.template.orders.ui.PrototypeScope;

@SpringComponent
@PrototypeScope
public class OrderStateWindowController {

	private transient OrderService orderService;

	private OrderStateWindow view;

	@Autowired
	private EventBus.ViewEventBus eventBus;

	public void setState(Order order, OrderState state) {
		getOrderService().changeState(order, state);
		view.close();
		eventBus.publish(this, new OrderUpdated());
	}

	public void init(OrderStateWindow view) {
		this.view = view;
	}

	protected OrderService getOrderService() {
		if (orderService == null) {
			orderService = BeanLocator.find(OrderService.class);
		}
		return orderService;
	}
}
