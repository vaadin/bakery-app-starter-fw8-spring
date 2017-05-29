package com.vaadin.template.orders.ui.navigation;

import com.vaadin.template.orders.ui.view.NavigableView;

public class NavigationEvent {

	private Runnable onNavigate;

	public NavigationEvent(Runnable onNavigate) {
		this.onNavigate = onNavigate;
	}

	/**
	 * Proceeds with navigation. This is typically called from
	 * {@link NavigableView#beforeLeave(NavigationEvent)} but it can also be
	 * delayed and called later, e.g. after showing a confirmation dialog.
	 */
	public void navigate() {
		onNavigate.run();
	}
}
