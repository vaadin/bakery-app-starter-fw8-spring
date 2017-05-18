package com.vaadin.template.orders.ui.view;

import com.vaadin.navigator.View;
import com.vaadin.ui.Component;

/**
 * An improved view interface which can provide the component for the view.
 */
public interface OrdersView extends View {

	/**
	 * Gets the component to show when navigating to the view.
	 *
	 * By default casts this View to a {@link Component}.
	 *
	 * @return the component to show, by default the view instance itself
	 */
	public default Component getViewComponent() {
		return (Component) this;
	}

	/**
	 * Called before leaving a view.
	 * <p>
	 * To proceed with navigation, the {@link NavigationEvent#navigate()} method
	 * must be called, either immediately or later. The view can prevent
	 * navigation away by doing nothing in this method.
	 *
	 * @param event
	 *            the navigation event object
	 *
	 */
	public default void beforeLeave(NavigationEvent event) {
		event.navigate();
	}

}
