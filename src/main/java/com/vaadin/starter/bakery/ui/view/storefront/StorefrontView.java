package com.vaadin.starter.bakery.ui.view.storefront;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.starter.bakery.ui.components.OrdersGrid;
import com.vaadin.starter.bakery.ui.components.search.SearchField;
import com.vaadin.starter.bakery.ui.view.NavigableView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SpringView
public class StorefrontView extends StorefrontViewDesign implements NavigableView {

	private StorefrontPresenter presenter;

	@Autowired
	public StorefrontView(StorefrontPresenter presenter) {
		this.presenter = presenter;
	}

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
		presenter.init(this);

		list.setDataProvider(presenter.getOrdersProvider());
		list.addSelectionListener(e -> presenter.selectedOrder(e.getFirstSelectedItem().get()));

		searchField.addSerchListener(e -> presenter.search(e));

		newOrder.addClickListener(e -> presenter.newOrder());
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
		presenter.enter(event.getParameters());
	}
}
