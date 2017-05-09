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
}