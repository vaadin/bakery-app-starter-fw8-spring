package com.vaadin.template.orders.ui.view.dashboard;

import java.time.MonthDay;
import java.time.Year;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.template.orders.app.BeanLocator;
import com.vaadin.template.orders.backend.service.OrderService;
import com.vaadin.template.orders.ui.PrototypeScope;
import com.vaadin.template.orders.ui.components.OrdersDataProvider;

@SpringComponent
@PrototypeScope
public class DashboardPresenter {

	@Autowired
	private OrdersDataProvider ordersDataProvider;

	private transient OrderService orderService;

	private DashboardView view;

	void init(DashboardView view) {
		this.view = view;
	}

	protected DashboardView getView() {
		return view;
	}

	public OrdersDataProvider getOrdersProvider() {
		return ordersDataProvider;
	}

	public DashboardData fetchData() {
		return getOrderService().getDashboardData(MonthDay.now().getMonthValue(), Year.now().getValue());
	}

	protected OrderService getOrderService() {
		return orderService = BeanLocator.use(orderService).orElseFindInstance(OrderService.class);
	}
}
