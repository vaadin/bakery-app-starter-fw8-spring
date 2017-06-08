package com.vaadin.starter.bakery.ui.view.dashboard;

import java.io.Serializable;
import java.time.MonthDay;
import java.time.Year;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.starter.bakery.app.BeanLocator;
import com.vaadin.starter.bakery.backend.data.DashboardData;
import com.vaadin.starter.bakery.backend.data.entity.Order;
import com.vaadin.starter.bakery.backend.service.OrderService;
import com.vaadin.starter.bakery.ui.components.OrdersDataProvider;
import com.vaadin.starter.bakery.ui.navigation.NavigationManager;
import com.vaadin.starter.bakery.ui.view.orderedit.OrderEditView;

@SpringComponent
@ViewScope
public class DashboardPresenter implements Serializable {

	private transient OrdersDataProvider ordersDataProvider;

	private transient OrderService orderService;

	private DashboardView view;

	private NavigationManager navigationManager;

	@Autowired
	public DashboardPresenter(NavigationManager navigationManager) {
		this.navigationManager = navigationManager;
	}

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

	public void selectedOrder(Order order) {
		navigationManager.navigateTo(OrderEditView.class, order.getId());
	}
}
