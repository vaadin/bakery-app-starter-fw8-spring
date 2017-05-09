package com.vaadin.template.orders.ui;

import javax.annotation.PostConstruct;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.spring.internal.Conventions;
import com.vaadin.spring.navigator.SpringNavigator;
import com.vaadin.template.orders.backend.data.Role;
import com.vaadin.template.orders.ui.view.dashboard.DashboardView;
import com.vaadin.template.orders.ui.view.orders.OrdersListView;

@Component
@UIScope
public class NavigationManagerBean extends SpringNavigator implements NavigationManager {

	@PostConstruct
	protected void initialize() {
		System.out.println("Initializing navigation manager");
	}

	/**
	 * Find the view id (URI fragment) used for a given view class.
	 *
	 * @param viewClass
	 *            the view class to find the id for
	 * @return the URI fragment for the view
	 */
	@Override
	public String getViewId(Class<? extends View> viewClass) {
		SpringView springView = viewClass.getAnnotation(SpringView.class);
		if (springView == null) {
			throw new IllegalArgumentException("The target class must be a @SpringView");
		}

		return Conventions.deriveMappingForView(viewClass, springView);
	}

	@Override
	public void navigateTo(Class<? extends View> targetView) {
		String viewId = getViewId(targetView);
		navigateTo(viewId);
	}

	@Override
	public void navigateTo(Class<? extends View> targetView, Object parameter) {
		String viewId = getViewId(targetView);
		navigateTo(viewId + "/" + parameter.toString());
	}

	@Override
	public void navigateToDefaultView() {
		// If the user wants a speficic view, it's in the URL.
		// Otherwise admin goes to DashboardView and everybody else to
		// OrderListView

		if (!getState().isEmpty()) {
			return;
		}

		navigateTo(isCurrentUserAdmin() ? DashboardView.class : OrdersListView.class);
	}

	private boolean isCurrentUserAdmin() {
		SecurityContext context = SecurityContextHolder.getContext();
		return context.getAuthentication().getAuthorities().stream()
				.filter(authority -> authority.getAuthority().equals(Role.ADMIN)).findAny().isPresent();
	}
}
