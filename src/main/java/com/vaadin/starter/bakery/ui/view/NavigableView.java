package com.vaadin.starter.bakery.ui.view;

import com.vaadin.navigator.View;

/**
 * A view which is connected to a URL and can be navigated to by the user.
 * <p>
 * Note that this {@code view} is not related to a {@code view} in MVP/MVC or
 * other patterns.
 */
public interface NavigableView extends View {

	/**
	 * Called before leaving a view.
	 * <p>
	 * To proceed with navigation, the {@link NavigationEvent#navigate()} method
	 * must be called, either immediately or later.
	 *
	 * @param event
	 *            the navigation event object
	 * @return <code>true</code> if navigation should take place,
	 *         <code>false</code> to prevent navigation (can be invoked later
	 *         using {@link NavigationEvent#navigate()})
	 */
	public default boolean beforeLeave(Runnable event) {
		return true;
	}

}
