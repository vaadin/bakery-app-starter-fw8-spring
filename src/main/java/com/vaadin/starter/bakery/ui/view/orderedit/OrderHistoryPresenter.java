package com.vaadin.starter.bakery.ui.view.orderedit;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.starter.bakery.app.BeanLocator;
import com.vaadin.starter.bakery.backend.service.OrderService;
import com.vaadin.starter.bakery.ui.eventbus.ViewEventBus;

@SpringComponent
@ViewScope
public class OrderHistoryPresenter implements Serializable {

	private OrderHistory view;

	private transient OrderService orderService;

	private final ViewEventBus eventBus;

	@Autowired
	public OrderHistoryPresenter(ViewEventBus eventBus) {
		this.eventBus = eventBus;
	}

	public void init(OrderHistory view) {
		this.view = view;
	}

	public void addNewComment(String comment) {
		getOrderService().addHistoryItem(view.getOrder(), comment);
		eventBus.publish(new OrderUpdated());
	}

	protected OrderService getOrderService() {
		if (orderService == null) {
			orderService = BeanLocator.find(OrderService.class);
		}
		return orderService;
	}
}
