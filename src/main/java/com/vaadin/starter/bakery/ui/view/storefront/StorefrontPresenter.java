package com.vaadin.starter.bakery.ui.view.storefront;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.starter.bakery.backend.data.entity.Order;
import com.vaadin.starter.bakery.ui.components.OrdersDataProvider;
import com.vaadin.starter.bakery.ui.navigation.NavigationManager;
import com.vaadin.starter.bakery.ui.view.orderedit.OrderEditController;

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
		navigationManager.navigateTo(OrderEditController.class, order.getId());
	}

	public void newOrder() {
		navigationManager.navigateTo(OrderEditController.class);
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

	public void enter(String parameterString) {
		Map<String, String> params = parameterStringToMap(parameterString);
		String searchTerm = params.get(PARAMETER_SEARCH);
		if (searchTerm == null) {
			searchTerm = "";
		}
		boolean includePast = params.containsKey(PARAMETER_INCLUDE_PAST);
		filterGrid(searchTerm, includePast);
	}

	private Map<String, String> parameterStringToMap(String parameterString) {
		Map<String, String> parameterMap = new HashMap<>();
		String[] parameters = parameterString.split("&");
		for (int i = 0; i < parameters.length; i++) {
			String[] keyAndValue = parameters[i].split("=");
			if (keyAndValue.length > 1) {
				parameterMap.put(keyAndValue[0], keyAndValue[1]);
			} else {
				parameterMap.put(keyAndValue[0], "");
			}
		}

		return parameterMap;
	}
}
