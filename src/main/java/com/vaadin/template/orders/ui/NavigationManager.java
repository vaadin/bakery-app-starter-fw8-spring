package com.vaadin.template.orders.ui;

import java.io.Serializable;

import com.vaadin.navigator.View;

/**
 * NavigationManager is the component that governs view navigation of the Orders
 * app.
 *
 * @author Peter / Vaadin
 */
public interface NavigationManager extends Serializable {

	void navigateTo(Class<? extends View> targetView);

	void navigateTo(Class<? extends View> targetView, Object parameter);

	void navigateToDefaultView();

	String getViewId(Class<? extends View> viewClass);

	/**
	 * Update the parameter of the the current view without firing any
	 * navigation events.
	 *
	 * @param parameter
	 *            the new parameter to set, never <code>null</code>,
	 *            <code>""</code> to not use any parameter
	 */
	void updateViewParameter(String parameter);
}