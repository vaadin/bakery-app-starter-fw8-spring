package com.vaadin.template.orders.ui.view.orders;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.template.orders.app.BeanLocator;
import com.vaadin.template.orders.backend.data.OrderState;
import com.vaadin.template.orders.backend.data.entity.Order;
import com.vaadin.template.orders.backend.service.OrderService;
import com.vaadin.template.orders.ui.HasLogger;
import com.vaadin.template.orders.ui.PrototypeScope;
import com.vaadin.template.orders.ui.eventbus.ViewEventBus;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

@SpringComponent
@PrototypeScope
public class OrderStateWindowController implements HasLogger, Serializable {

	private transient OrderService orderService;

	private OrderStateWindow view;

	private final ViewEventBus eventBus;

	@Autowired
	public OrderStateWindowController(ViewEventBus eventBus) {
		this.eventBus = eventBus;
	}

	public void setState(Order order, OrderState state) {
		try {
			getOrderService().changeState(order, state);
		} catch (Exception e) {
			Notification.show("Unable to set state to " + state.getDisplayName() + ". Try refreshing the page",
					Type.ERROR_MESSAGE);
			getLogger().debug("Unable to change order state", e);
			return;
		}
		view.close();
		eventBus.publish(new OrderUpdated());
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
