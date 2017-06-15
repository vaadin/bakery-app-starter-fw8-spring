package com.vaadin.starter.bakery.ui.view.storefront;

import java.io.Serializable;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.starter.bakery.backend.data.entity.Order;
import com.vaadin.starter.bakery.ui.components.OrdersDataProvider;
import com.vaadin.starter.bakery.ui.navigation.NavigationManager;
import com.vaadin.starter.bakery.ui.view.orderedit.OrderEditView;

@SpringComponent
@ViewScope
public class StorefrontPresenter implements Serializable {

	private static final String PARAMETER_SEARCH = "search";

	private static final String PARAMETER_INCLUDE_PAST = "includePast";

	private StorefrontView view;

	private final NavigationManager navigationManager;

	private final OrdersDataProvider ordersDataProvider;

	@Autowired
	public StorefrontPresenter(NavigationManager navigationManager, OrdersDataProvider ordersDataProvider) {
		this.navigationManager = navigationManager;
		this.ordersDataProvider = ordersDataProvider;
	}

	void init(StorefrontView view) {
		this.view = view;
	}

	protected StorefrontView getView() {
		return view;
	}

	public OrdersDataProvider getOrdersProvider() {
		return ordersDataProvider;
	}

	public void selectedOrder(Order order) {
		navigationManager.navigateTo(OrderEditView.class, order.getId());
	}

	public void newOrder() {
		navigationManager.navigateTo(OrderEditView.class);
	}

	public void search(String searchTerm, boolean includePast) {
		filterGrid(searchTerm, includePast);
		String parameters = PARAMETER_SEARCH + "=" + searchTerm;
		if (includePast) {
			parameters += "&" + PARAMETER_INCLUDE_PAST;
		}
		navigationManager.updateViewParameter(parameters);
	}

	private void filterGrid(String searchTerm, boolean includePast) {
		ordersDataProvider.setFilter(searchTerm);
		ordersDataProvider.setIncludePast(includePast);
		view.updateFilters(searchTerm, includePast);
	}

	public void enter(ViewChangeEvent event) {
		Map<String, String> params = event.getNavigator().getStateParameterMap();
		String searchTerm = params.getOrDefault(PARAMETER_SEARCH, "");
		boolean includePast = params.containsKey(PARAMETER_INCLUDE_PAST);
		filterGrid(searchTerm, includePast);
	}

}
