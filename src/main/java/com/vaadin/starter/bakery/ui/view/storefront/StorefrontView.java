package com.vaadin.starter.bakery.ui.view.storefront;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.starter.bakery.backend.data.entity.Order;
import com.vaadin.starter.bakery.ui.components.search.SearchEvent;
import com.vaadin.starter.bakery.ui.navigation.NavigationManager;
import com.vaadin.starter.bakery.ui.view.NavigableView;
import com.vaadin.starter.bakery.ui.view.orderedit.OrderEditView;

@SpringView
public class StorefrontView extends StorefrontViewDesign implements NavigableView {

	@Autowired
	private NavigationManager navigationManager;

	private static final String PARAMETER_SEARCH = "search";
	private static final String PARAMETER_INCLUDE_PAST = "includePast";

	/**
	 * This method is invoked once each time an instance of the view is created.
	 * <p>
	 * This typically happens whenever a user opens the URL for the view, or
	 * refreshes the browser as long as the view is set to {@link ViewScope}. If
	 * we set the view to {@link UIScope}, the instance will be kept in memory
	 * (in the session) as long as the UI exists in memory and the same view
	 * instance will be reused whenever the user enters the view.
	 * <p>
	 * Here we set up listeners and attach data providers and otherwise
	 * configure the components for the parts which only need to be done once.
	 */
	@PostConstruct
	public void init() {
		list.addSelectionListener(e -> onOrderSelected(e.getFirstSelectedItem().get()));

		searchField.addSerchListener(e -> onSearch(e));
		newOrder.addClickListener(e -> onNewOrder());
	}

	/**
	 * This is called whenever the user enters the view, regardless of if the
	 * view instance was created right before this or if an old instance was
	 * reused.
	 * <p>
	 * Here we update the data shown in the view so the user sees the latest
	 * changes.
	 */
	@Override
	public void enter(ViewChangeEvent event) {
		String searchTerm = NavigationManager.parseParam(event.getParameters(), PARAMETER_SEARCH).orElse("");
		boolean includePast = NavigationManager.paramAvailable(event.getParameters(), PARAMETER_INCLUDE_PAST);
		filterGrid(searchTerm, includePast);
	}

	protected void filterGrid(String searchString, boolean includePast) {
		list.filterGrid(searchString, includePast);
		searchField.setSearchString(searchString);
		searchField.setIncludePast(includePast);
	}

	public void onOrderSelected(Order order) {
		navigationManager.navigateTo(OrderEditView.class, order.getId());
	}

	public void onNewOrder() {
		navigationManager.navigateTo(OrderEditView.class);
	}

	public void onSearch(SearchEvent event) {
		filterGrid(event.getSearchString(), event.isIncludePast());
		String parameters = PARAMETER_SEARCH + "=" + event.getSearchString();
		if (event.isIncludePast()) {
			parameters += "&" + PARAMETER_INCLUDE_PAST;
		}
		navigationManager.updateViewParameter(parameters);
	}
}
