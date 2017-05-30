package com.vaadin.template.orders.ui.view.dashboard;

import java.io.Serializable;
import java.time.MonthDay;
import java.time.Year;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.template.orders.app.BeanLocator;
import com.vaadin.template.orders.backend.data.DashboardData;
import com.vaadin.template.orders.backend.service.OrderService;
import com.vaadin.template.orders.ui.components.OrdersDataProvider;

@SpringComponent
@ViewScope
public class DashboardPresenter implements Serializable {

	private transient OrdersDataProvider ordersDataProvider;

	private transient OrderService orderService;

	private DashboardView view;

	void init(DashboardView view) {
		this.view = view;
	}

	protected DashboardView getView() {
		return view;
	}

	public DashboardData fetchData() {
		return getOrderService().getDashboardData(MonthDay.now().getMonthValue(), Year.now().getValue());
	}

	public OrdersDataProvider getOrdersProvider() {
		if (ordersDataProvider == null) {
			ordersDataProvider = BeanLocator.find(OrdersDataProvider.class);
		}
		return ordersDataProvider;
	}

	protected OrderService getOrderService() {
		if (orderService == null) {
			orderService = BeanLocator.find(OrderService.class);
		}
		return orderService;
	}
}
